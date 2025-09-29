package com.nqh.validex.core;

/**
 * Optional provider SPI for runtime validators.
 */
public interface ValidatorProvider {
    /**
     * If this provider can validate obj's type, return a result; otherwise return null.
     */
    <T> ValidationResult validate(T obj);
}

