package com.nqh.validex.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Convenience runtime entrypoints for invoking generated validators.
 *
 * <p>Features:
 * - Convention lookup: uses <ClassName>Validator with {@code INSTANCE.validate(T)}
 * - Caching: reflective lookups are cached for performance
 * - Provider SPI: optional {@link ValidatorProvider} via {@link ServiceLoader}
 * - Parent support: walks superclasses and interfaces for reusable validators
 * - Fail-safe strategy: configurable to swallow reflection errors or throw
 */
public final class Validators {

    private Validators() {}

    /**
     * Logger facade for diagnostics. Defaults to no-op.
     */
    public interface Logger {
        void warn(String message, Throwable error);
    }

    private static volatile boolean failSafe = true;
    private static volatile Logger logger = (msg, err) -> {};

    private static final ConcurrentMap<Class<?>, Optional<Invoker>> CACHE = new ConcurrentHashMap<>();
    private static volatile List<ValidatorProvider> providers;

    /**
     * Configure whether to swallow internal reflection errors (default true).
     */
    public static void setFailSafe(boolean enabled) {
        failSafe = enabled;
    }

    /** Configure logger used for internal warnings. */
    public static void setLogger(Logger customLogger) {
        logger = (customLogger == null) ? (msg, err) -> {} : customLogger;
    }

    /** Validate an object using generated validator or SPI providers. */
    public static <T> ValidationResult validate(T obj) {
        if (obj == null) {
            return ValidationResult.failure(List.of(new Violation("", "object must not be null", null)));
        }

        // 1) Convention based invoker (cached)
        Invoker invoker = resolveInvoker(obj.getClass());
        if (invoker != null) {
            try {
                return invoker.invoke(obj);
            } catch (Exception err) {
                if (!failSafe) {
                    throw propagate(err);
                }
                logger.warn("Validator invocation failed; returning ok() due to failSafe", err);
            }
        }

        // 2) SPI providers (cached list)
        List<ValidatorProvider> ps = loadProviders();
        for (ValidatorProvider p : ps) {
            try {
                @SuppressWarnings("unchecked")
                ValidationResult r = p.validate(obj);
                if (r != null) {
                    return r;
                }
            } catch (Exception err) {
                if (!failSafe) {
                    throw propagate(err);
                }
                logger.warn("ValidatorProvider failed; continuing due to failSafe", err);
            }
        }
        return ValidationResult.ok();
    }

    /** Validate and throw {@link ValidationException} if invalid. */
    public static <T> void validateOrThrow(T obj) {
        ValidationResult result = validate(obj);
        if (!result.isValid()) {
            throw new ValidationException(result);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        if (t instanceof RuntimeException re) return re;
        return new RuntimeException(t);
    }

    private static List<ValidatorProvider> loadProviders() {
        List<ValidatorProvider> ps = providers;
        if (ps == null) {
            synchronized (Validators.class) {
                if (providers == null) {
                    List<ValidatorProvider> list = new ArrayList<>();
                    for (ValidatorProvider p : ServiceLoader.load(ValidatorProvider.class)) {
                        list.add(p);
                    }
                    providers = Collections.unmodifiableList(list);
                }
                ps = providers;
            }
        }
        return ps;
    }

    private static Invoker resolveInvoker(Class<?> type) {
        Optional<Invoker> cached = CACHE.computeIfAbsent(type, Validators::lookupInvoker);
        return cached.orElse(null);
    }

    private static Optional<Invoker> lookupInvoker(Class<?> type) {
        // Walk type hierarchy: class -> superclasses -> interfaces
        ClassLoader cl = type.getClassLoader();
        for (Class<?> t : walkTypes(type)) {
            String validatorClassName = t.getName() + "Validator";
            try {
                Class<?> validatorClass = Class.forName(validatorClassName, true, cl);
                Object instance = validatorClass.getField("INSTANCE").get(null);
                Method m = validatorClass.getMethod("validate", t);
                return Optional.of(new Invoker(instance, m));
            } catch (ClassNotFoundException e) {
                // continue
            } catch (Exception err) {
                if (!failSafe) {
                    throw propagate(err);
                }
                logger.warn("Failed to bind validator '" + validatorClassName + "'", err);
            }
        }
        return Optional.empty();
    }

    private static List<Class<?>> walkTypes(Class<?> start) {
        List<Class<?>> order = new ArrayList<>();
        // classes
        for (Class<?> c = start; c != null && c != Object.class; c = c.getSuperclass()) {
            order.add(c);
        }
        // interfaces of each
        int initial = order.size();
        for (int i = 0; i < initial; i++) {
            for (Class<?> itf : order.get(i).getInterfaces()) {
                if (!order.contains(itf)) {
                    order.add(itf);
                }
            }
        }
        return order;
    }

    private record Invoker(Object instance, Method method) {
        ValidationResult invoke(Object arg) throws Exception {
            Objects.requireNonNull(arg, "arg");
            @SuppressWarnings("unchecked")
            ValidationResult result = (ValidationResult) method.invoke(instance, arg);
            return result;
        }
    }
}

