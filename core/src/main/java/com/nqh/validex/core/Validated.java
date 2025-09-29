package com.nqh.validex.core;

import java.util.List;

/**
 * Sum type representing either a valid value or a list of violations.
 */
public sealed interface Validated<T> permits Validated.Valid, Validated.Invalid {
    /** Holds a valid value. */
    record Valid<T>(T value) implements Validated<T> {}
    /** Holds a list of violations. */
    record Invalid<T>(List<Violation> violations) implements Validated<T> {}
}
