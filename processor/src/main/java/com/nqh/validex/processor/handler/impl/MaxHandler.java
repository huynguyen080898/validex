package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.Max;
import com.nqh.validex.processor.handler.AnnotationHandler;

import static com.nqh.validex.processor.ValidationProcessor.MSG_MAX;

public final class MaxHandler implements AnnotationHandler<Max> {
    @Override
    public String generateValidationCode(String fieldName, String accessor, Max ann) {
        return String.format(
                "{ Object __v = %s; if (__v != null && ((Number)__v).longValue() > %dL) { violations.add(new Violation(\"%s\", String.format(\"%s\", %d), __v)); } }%n",
                accessor, ann.value(), fieldName, MSG_MAX, ann.value()
        );
    }
}
