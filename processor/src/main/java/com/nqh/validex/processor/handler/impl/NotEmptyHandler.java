package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.NotEmpty;
import com.nqh.validex.processor.handler.AnnotationHandler;

public final class NotEmptyHandler implements AnnotationHandler<NotEmpty> {

    @Override
    public String generateValidationCode(String fieldName, String accessor, NotEmpty ann) {
        String message = ann.message().replace("\\", "\\\\").replace("\"", "\\\"");
        return String.format(
                "{ Object __v = %s; if (__v == null) { violations.add(new Violation(\"%s\", \"must not be empty\", null)); } else { boolean __empty = false; " +
                        "if (__v instanceof CharSequence) { __empty = ((CharSequence)__v).length() == 0; } " +
                        "else if (__v instanceof java.util.Collection) { __empty = ((java.util.Collection<?>)__v).isEmpty(); } " +
                        "else if (__v instanceof java.util.Map) { __empty = ((java.util.Map<?,?>)__v).isEmpty(); } " +
                        "else if (__v.getClass().isArray()) { __empty = java.lang.reflect.Array.getLength(__v) == 0; } " +
                        "if (__empty) { violations.add(new Violation(\"%s\", \"%s\", __v)); } } }%n",
                accessor, fieldName, fieldName, message
        );
    }
}


