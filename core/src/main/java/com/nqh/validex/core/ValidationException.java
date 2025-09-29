package com.nqh.validex.core;

public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final transient ValidationResult result;

    public ValidationException(ValidationResult result) {
        super(result == null ? "Validation failed" : result.toString());
        this.result = result;
    }

    public ValidationException(String message, ValidationResult result) {
        super(message);
        this.result = result;
    }

    public ValidationResult getResult() {
        return result;
    }
}

