# Architecture

## Overview

Validex consists of:
- Annotations: marker annotations for fields
- Processor: generates `<Type>Validator` classes per annotated type
- Core: runtime utilities to invoke validators and represent results

## Processor flow

1. Scan for annotations (`@NotNull`, `@Size`, `@Min`, `@Max`, `@Valid`)
2. Group by enclosing type
3. For each type, generate `<Type>Validator`:
   - Null object check
   - For each field, emit checks based on annotations
   - If `@Valid`, call `Validators.validate(field)` and prefix child paths
4. Write sources to target/generated-sources/annotations

## Accessors support

- Records: use component accessors (e.g., `obj.name()`)
- Classes: public fields or JavaBean getters `getX()`/`isX()`
- Private fields without getters are skipped

## Runtime invocation

`Validators` resolves the validator via naming convention `<Type>Validator`, caches a MethodHandle invoker, and supports superclasses/interfaces. Optional `ValidatorProvider` SPI allows custom handlers.

Thread-safety:
- Concurrent caches for invokers
- AtomicReference for logger and provider list

## Error strategy

- Fail-safe by default: log and continue
- `setFailSafe(false)`: throw `ValidatorInvocationException` on invocation errors


