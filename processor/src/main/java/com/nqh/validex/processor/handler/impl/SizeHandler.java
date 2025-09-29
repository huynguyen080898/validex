package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.Size;
import com.nqh.validex.processor.handler.AnnotationHandler;

import static com.nqh.validex.processor.ValidationProcessor.MSG_SIZE_BETWEEN;

public final class SizeHandler implements AnnotationHandler<Size> {
    @Override
    public String generateValidationCode(String fieldName, String accessor, Size ann) {
        return String.format(
                "{ Object __v = %s; if (__v != null) { int len = ((CharSequence)__v).length(); if (len < %d || len > %d) { violations.add(new Violation(\"%s\", String.format(\"%s\", %d, %d), __v)); } } }%n",
                accessor, ann.min(), ann.max(), fieldName, MSG_SIZE_BETWEEN, ann.min(), ann.max()
        );
    }
}
