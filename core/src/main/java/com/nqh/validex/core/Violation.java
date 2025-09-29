package com.nqh.validex.core;


/**
 * Represents a single validation violation.
 *
 * @param path         Path to the invalid value (e.g., "user.email")
 * @param message      Human-readable error message
 * @param invalidValue The rejected value (may be null)
 */
public record Violation(
        String path,
        String message,
        Object invalidValue
) {
}
