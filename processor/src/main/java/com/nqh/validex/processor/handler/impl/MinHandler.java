package com.nqh.validex.processor.handler.impl;


import com.nqh.validex.annotations.Min;
import com.nqh.validex.processor.handler.AnnotationHandler;

import static com.nqh.validex.processor.ValidationProcessor.MSG_MIN;

public final class MinHandler implements AnnotationHandler<Min> {
    @Override
    public String generateValidationCode(String fieldName, String accessor, Min ann) {
        return String.format(
                "{ Object __v = %s; if (__v != null && ((Number)__v).longValue() < %dL) { violations.add(new Violation(\"%s\", String.format(\"%s\", %d), __v)); } }%n",
                accessor, ann.value(), fieldName, MSG_MIN, ann.value()
        );
    }
}

