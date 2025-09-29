package com.nqh.validex.core;

/**
 * Represents a single rule applied to type T.
 */
@FunctionalInterface
public interface Rule<T> {
    /**
     * @param value value to validate
     * @return a Violation if validation fails, otherwise null
     */
    Violation validate(T value);
}
