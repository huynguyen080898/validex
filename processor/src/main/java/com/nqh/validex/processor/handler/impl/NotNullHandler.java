package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.NotNull;
import com.nqh.validex.processor.handler.AnnotationHandler;

import static com.nqh.validex.processor.ValidationProcessor.MSG_NOT_NULL;

public final class NotNullHandler implements AnnotationHandler<NotNull> {

    @Override
    public String generateValidationCode(String fieldName, String accessor, NotNull ann) {
        String message = ann.message().equals(MSG_NOT_NULL) ? MSG_NOT_NULL : ann.message();
        return String.format(
                "if (%s == null) { violations.add(new Violation(\"%s\", \"%s\", null)); }%n",
                accessor, fieldName, message
        );
    }
}
