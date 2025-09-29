package com.nqh.validex.core;

import java.util.List;

public sealed interface Validated<T> permits Validated.Valid, Validated.Invalid {
    record Valid<T>(T value) implements Validated<T> {}
    record Invalid<T>(List<Violation> violations) implements Validated<T> {}
}
