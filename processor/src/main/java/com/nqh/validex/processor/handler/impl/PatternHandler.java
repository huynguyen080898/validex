package com.nqh.validex.processor.handler.impl;

import com.nqh.validex.annotations.Pattern;
import com.nqh.validex.processor.handler.AnnotationHandler;

public final class PatternHandler implements AnnotationHandler<Pattern> {

    @Override
    public String generateValidationCode(String fieldName, String accessor, Pattern ann) {
        String escapedRegex = ann.regex()
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        String message = ann.message();
        String escapedMessage = message.replace("\\", "\\\\").replace("\"", "\\\"");
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


