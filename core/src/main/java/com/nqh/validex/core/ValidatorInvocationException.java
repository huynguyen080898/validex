package com.nqh.validex.core;

/**
 * Thrown when invoking a generated validator fails due to reflection or binding issues.
 */
public class ValidatorInvocationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ValidatorInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorInvocationException(Throwable cause) {
        super(cause == null ? null : cause.getMessage(), cause);
    }
}

