package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.Email;
import com.nqh.validex.processor.handler.AnnotationHandler;

public final class EmailHandler implements AnnotationHandler<Email> {

    private static final String DEFAULT_MESSAGE = "must be a valid email";
    // Simple pragmatic regex (not RFC-perfect, but practical)
    private static final String REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public String generateValidationCode(String fieldName, String accessor, Email ann) {
        String message = ann.message().equals(DEFAULT_MESSAGE) ? DEFAULT_MESSAGE : ann.message();
        String escapedMessage = message.replace("\\", "\\\\").replace("\"", "\\\"");
        String escapedRegex = REGEX.replace("\\", "\\\\").replace("\"", "\\\"");
        return String.format(
                "if (%s != null && !java.util.regex.Pattern.matches(\"%s\", %s instanceof CharSequence ? %s.toString() : String.valueOf(%s))) { violations.add(new Violation(\"%s\", \"%s\", %s)); }%n",
                accessor,
                escapedRegex,
                accessor, accessor, accessor,
                fieldName,
                escapedMessage,
                accessor
        );
    }
}


