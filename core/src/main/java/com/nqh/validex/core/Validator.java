package com.nqh.validex.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Validator composed of multiple rules.
 * <p>
 * Record-based, immutable, thread-safe.
 */
public record Validator<T>(List<Rule<T>> rules) {

    public Validator {
        rules = (rules == null || rules.isEmpty())
                ? Collections.emptyList()
                : List.copyOf(rules);
    }

    public ValidationResult validate(T value) {
        if (rules.isEmpty()) {
            return ValidationResult.ok();
        }
        List<Violation> violations = null;
        for (Rule<T> rule : rules) {
            Violation v = rule.validate(value);
            if (v != null) {
                if (violations == null) {
                    violations = new ArrayList<>();
                }
                violations.add(v);
            }
        }
        return (violations == null)
                ? ValidationResult.ok()
                : ValidationResult.failure(violations);
    }
}