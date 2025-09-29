package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.Positive;
import com.nqh.validex.processor.handler.AnnotationHandler;

public final class PositiveHandler implements AnnotationHandler<Positive> {

    @Override
    public String generateValidationCode(String fieldName, String accessor, Positive ann) {
        String message = ann.message().replace("\\", "\\\\").replace("\"", "\\\"");
        return String.format(
                "{ Object __v = %s; if (__v != null && ((Number)__v).doubleValue() <= 0d) { violations.add(new Violation(\"%s\", \"%s\", __v)); } }%n",
                accessor, fieldName, message
        );
    }
}


