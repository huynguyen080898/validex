package com.nqh.validex.processor.handler;

import java.lang.annotation.Annotation;

public interface AnnotationHandler<A extends Annotation> {
    /**
     * Generate validation code chunk for a single field with this annotation
     * @param fieldName name of the field (for path in violations)
     * @param accessor Java expression to access the field value from `obj` (e.g., obj.name(), obj.getName())
     * @param annotation annotation instance
     * @return String chunk of Java code
     */
    String generateValidationCode(String fieldName, String accessor, A annotation);
}