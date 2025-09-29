package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.NotBlank;
import com.nqh.validex.processor.handler.AnnotationHandler;

public final class NotBlankHandler implements AnnotationHandler<NotBlank> {

    @Override
    public String generateValidationCode(String fieldName, String accessor, NotBlank ann) {
        String message = ann.message().replace("\\", "\\\\").replace("\"", "\\\"");
        return String.format(
                "{ Object __v = %s; if (__v == null) { violations.add(new Violation(\"%s\", \"%s\", null)); } else if (__v instanceof CharSequence) { boolean __blank = true; CharSequence __cs = (CharSequence)__v; for (int __i=0; __i<__cs.length(); __i++) { if (!Character.isWhitespace(__cs.charAt(__i))) { __blank = false; break; } } if (__blank) { violations.add(new Violation(\"%s\", \"%s\", __v)); } } }%n",
                accessor, fieldName, message, fieldName, message
        );
    }
}


