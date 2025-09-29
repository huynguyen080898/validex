package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.Size;
import com.nqh.validex.processor.handler.AnnotationHandler;

import static com.nqh.validex.processor.ValidationProcessor.MSG_SIZE_BETWEEN;

public final class SizeHandler implements AnnotationHandler<Size> {
    @Override
    public String generateValidationCode(String fieldName, String accessor, Size ann) {
        return String.format(
                "{ Object __v = %s; if (__v != null) { int __len = -1; " +
                        "if (__v instanceof CharSequence) { __len = ((CharSequence)__v).length(); } " +
                        "else if (__v instanceof java.util.Collection) { __len = ((java.util.Collection<?>)__v).size(); } " +
                        "else if (__v instanceof java.util.Map) { __len = ((java.util.Map<?,?>)__v).size(); } " +
                        "else if (__v.getClass().isArray()) { __len = java.lang.reflect.Array.getLength(__v); } " +
                        "if (__len != -1 && (__len < %d || __len > %d)) { violations.add(new Violation(\"%s\", String.format(\"%s\", %d, %d), __v)); } } }%n",
                accessor, ann.min(), ann.max(), fieldName, MSG_SIZE_BETWEEN, ann.min(), ann.max()
        );
    }
}
