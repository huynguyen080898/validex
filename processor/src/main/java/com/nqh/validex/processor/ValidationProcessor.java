package com.nqh.validex.processor;

import com.nqh.validex.annotations.Max;
import com.nqh.validex.annotations.Min;
import com.nqh.validex.annotations.NotNull;
import com.nqh.validex.annotations.Size;
import com.nqh.validex.annotations.NotEmpty;
import com.nqh.validex.annotations.Email;
import com.nqh.validex.annotations.NotBlank;
import com.nqh.validex.annotations.Positive;
import com.nqh.validex.annotations.PositiveOrZero;
import com.nqh.validex.annotations.Negative;
import com.nqh.validex.annotations.NegativeOrZero;
import com.nqh.validex.annotations.Pattern;
import com.nqh.validex.annotations.Valid;
import com.nqh.validex.processor.handler.AnnotationHandler;
import com.nqh.validex.processor.handler.impl.MaxHandler;
import com.nqh.validex.processor.handler.impl.MinHandler;
import com.nqh.validex.processor.handler.impl.NotNullHandler;
import com.nqh.validex.processor.handler.impl.PatternHandler;
import com.nqh.validex.processor.handler.impl.SizeHandler;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;

@SupportedAnnotationTypes({
        "com.nqh.validex.annotations.NotNull",
        "com.nqh.validex.annotations.Size",
        "com.nqh.validex.annotations.Min",
        "com.nqh.validex.annotations.Max",
        "com.nqh.validex.annotations.Valid",
        "com.nqh.validex.annotations.Pattern",
        "com.nqh.validex.annotations.Email",
        "com.nqh.validex.annotations.NotBlank",
        "com.nqh.validex.annotations.Positive",
        "com.nqh.validex.annotations.PositiveOrZero",
        "com.nqh.validex.annotations.Negative",
        "com.nqh.validex.annotations.NegativeOrZero"
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ValidationProcessor extends AbstractProcessor {

    public static final String MSG_NOT_NULL = "must not be null";
    public static final String MSG_SIZE_BETWEEN = "size must be between %d and %d";
    public static final String MSG_MIN = "must be >= %d";
    public static final String MSG_MAX = "must be <= %d";
    public static final String MSG_OBJ_NULL = "object must not be null";
    public static final String MSG_PATTERN = "must match pattern";

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    // Cache field list per class
    private final Map<String, List<VariableElement>> classFieldsCache = new HashMap<>();

    private Map<Class<? extends Annotation>, AnnotationHandler<?>> handlers;

    public ValidationProcessor() {
        //Default
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
        messager = env.getMessager();
        elementUtils = env.getElementUtils();

        // Deterministic handler order
        handlers = new LinkedHashMap<>();
        handlers.put(NotNull.class, new com.nqh.validex.processor.handler.impl.NotNullHandler());
        handlers.put(Size.class, new com.nqh.validex.processor.handler.impl.SizeHandler());
        handlers.put(Min.class, new com.nqh.validex.processor.handler.impl.MinHandler());
        handlers.put(Max.class, new com.nqh.validex.processor.handler.impl.MaxHandler());
        handlers.put(Pattern.class, new PatternHandler());
        handlers.put(NotEmpty.class, new com.nqh.validex.processor.handler.impl.NotEmptyHandler());
        handlers.put(Email.class, new com.nqh.validex.processor.handler.impl.EmailHandler());
        handlers.put(NotBlank.class, new com.nqh.validex.processor.handler.impl.NotBlankHandler());
        handlers.put(Positive.class, new com.nqh.validex.processor.handler.impl.PositiveHandler());
        handlers.put(PositiveOrZero.class, new com.nqh.validex.processor.handler.impl.PositiveOrZeroHandler());
        handlers.put(Negative.class, new com.nqh.validex.processor.handler.impl.NegativeHandler());
        handlers.put(NegativeOrZero.class, new com.nqh.validex.processor.handler.impl.NegativeOrZeroHandler());
        messager.printMessage(Diagnostic.Kind.NOTE, "ValidationProcessor initialized with " + handlers.size() + " handlers");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "ValidationProcessor invoked! annotations=" + annotations.size());

        Set<TypeElement> classesToGenerate = new HashSet<>();

        for (TypeElement ann : annotations) {
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ann)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Found annotated element: " + annotatedElement.getSimpleName());
                Element enclosing = (annotatedElement.getKind() == ElementKind.CLASS
                        || annotatedElement.getKind() == ElementKind.RECORD)
                        ? annotatedElement
                        : annotatedElement.getEnclosingElement();
                if (enclosing instanceof TypeElement te) {
                    classesToGenerate.add(te);
                }
            }
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "Total classes to generate: " + classesToGenerate.size());

        for (TypeElement classElement : classesToGenerate) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Processing class: " + classElement.getQualifiedName());
            String packageName = elementUtils.getPackageOf(classElement).getQualifiedName().toString();
            String className = classElement.getSimpleName().toString();
            String validatorClassName = className + "Validator";

            try {
                JavaFileObject file = filer.createSourceFile(packageName + "." + validatorClassName, classElement);
                try (Writer writer = file.openWriter()) {
                    writer.write(generateValidatorCode(packageName, className, validatorClassName, classElement));
                }
                messager.printMessage(Diagnostic.Kind.NOTE, "Generated: " + packageName + "." + validatorClassName);
            } catch (Exception ex) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        "Failed to generate validator for " + className + ": " + ex.getMessage());
            }
        }

        return true;
    }

    private String generateValidatorCode(String packageName, String className, String validatorClassName, TypeElement classElement) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("import com.nqh.validex.core.ValidationResult;\n");
        sb.append("import com.nqh.validex.core.Violation;\n");
        sb.append("import com.nqh.validex.core.Validators;\n");
        sb.append("import java.util.*;\n\n");

        sb.append("public final class ").append(validatorClassName).append(" {\n\n");
        sb.append("    private ").append(validatorClassName).append("() {}\n\n");
        sb.append("    public static final ").append(validatorClassName).append(" INSTANCE = new ")
                .append(validatorClassName).append("();\n\n");

        sb.append("    public ValidationResult validate(").append(className).append(" obj) {\n");
        sb.append("        if (obj == null) {\n");
        sb.append("            return ValidationResult.failure(List.of(new Violation(\"\", \"").append(MSG_OBJ_NULL).append("\", null)));\n");
        sb.append("        }\n\n");
        sb.append("        List<Violation> violations = new ArrayList<>();\n");

        List<VariableElement> fields = classFieldsCache.computeIfAbsent(
                classElement.getQualifiedName().toString(),
                key -> getFields(classElement)
        );

        for (VariableElement field : fields) {
            String fieldName = field.getSimpleName().toString();
            String accessor = resolveAccessor(classElement, field);
            if (accessor == null) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Skip field (no accessor): " + fieldName);
                continue; // skip fields without supported accessor
            }
            for (Map.Entry<Class<? extends Annotation>, AnnotationHandler<?>> entry : handlers.entrySet()) {
                Annotation ann = field.getAnnotation(entry.getKey());
                if (ann != null) {
                    @SuppressWarnings("unchecked")
                    AnnotationHandler<Annotation> handler = (AnnotationHandler<Annotation>) entry.getValue();
                    sb.append(handler.generateValidationCode(fieldName, accessor, ann));
                }
            }
            // Nested validation with @Valid
            if (field.getAnnotation(Valid.class) != null) {
                sb.append("{ ");
                sb.append("ValidationResult __child = Validators.validate(").append(accessor).append("); ");
                sb.append("if (!__child.isValid()) { for (Violation __v : __child.violations()) { ");
                sb.append("String __p = (__v.path() == null || __v.path().isEmpty()) ? \"").append(fieldName).append("\" : \"").append(fieldName).append(".\" + __v.path(); ");
                sb.append("violations.add(new Violation(__p, __v.message(), __v.invalidValue())); } } ");
                sb.append("}\n");
            }
        }

        sb.append("        return violations.isEmpty() ? ValidationResult.ok() : ValidationResult.failure(violations);\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        return sb.toString();
    }

    private List<VariableElement> getFields(TypeElement classElement) {
        List<VariableElement> fields = new ArrayList<>();
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.FIELD) {
                fields.add((VariableElement) enclosed);
            }
        }
        return fields;
    }

    private String resolveAccessor(TypeElement classElement, VariableElement field) {
        String fieldName = field.getSimpleName().toString();
        boolean isRecord = classElement.getKind() == ElementKind.RECORD;
        if (isRecord) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Using record accessor for field: " + fieldName);
            return "obj." + fieldName + "()";
        }

        // Public field access
        if (field.getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Using public field for: " + fieldName);
            return "obj." + fieldName;
        }

        // Getter method access (getX or isX for booleans)
        String capitalized = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        boolean isBoolean = Objects.equals(field.asType().toString(), "boolean")
                || Objects.equals(field.asType().toString(), "java.lang.Boolean");
        String getterName = "get" + capitalized;
        String booleanGetterName = "is" + capitalized;

        boolean hasGetter = hasMethod(classElement, getterName) || (isBoolean && hasMethod(classElement, booleanGetterName));
        if (hasGetter) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Using getter for field: " + fieldName);
            return "obj." + (hasMethod(classElement, getterName) ? getterName : booleanGetterName) + "()";
        }

        // No supported accessor found -> skip this field
        return null;
    }

    private boolean hasMethod(TypeElement classElement, String methodName) {
        for (Element e : classElement.getEnclosedElements()) {
            if (e.getKind() == ElementKind.METHOD && e.getSimpleName().contentEquals(methodName)) {
                return true;
            }
        }
        return false;
    }
}