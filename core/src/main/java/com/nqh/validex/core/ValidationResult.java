package com.nqh.validex.core;

import java.util.Collections;
import java.util.List;


/**
 * Result of a validation: either valid or containing violations.
 * Immutable & allocation-optimized.
 */
public record ValidationResult(List<Violation> violations) {

    public static final ValidationResult SUCCESS =
            new ValidationResult(Collections.emptyList());

    public boolean isValid() {
        return violations.isEmpty();
    }

    public static ValidationResult ok() {
        return SUCCESS;
    }

    public static ValidationResult failure(List<Violation> violations) {
        if (violations == null || violations.isEmpty()) {
            return SUCCESS;
        }

        //Defensive clones to avoid outside mutations
        return new ValidationResult(List.copyOf(violations));
    }
}