package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.PositiveOrZero;
import com.nqh.validex.processor.handler.AnnotationHandler;

public final class PositiveOrZeroHandler implements AnnotationHandler<PositiveOrZero> {

    @Override
    public String generateValidationCode(String fieldName, String accessor, PositiveOrZero ann) {
        String message = ann.message().replace("\\", "\\\\").replace("\"", "\\\"");
        return String.format(
                "{ Object __v = %s; if (__v != null && ((Number)__v).doubleValue() < 0d) { violations.add(new Violation(\"%s\", \"%s\", __v)); } }%n",
                accessor, fieldName, message
        );
    }
}


