package com.nqh.validex.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorCoreTest {

    @Test
    void validationResult_ok_and_failure() {
        ValidationResult ok = ValidationResult.ok();
        assertTrue(ok.isValid());
        assertEquals(List.of(), ok.violations());

        Violation v = new Violation("path", "msg", 1);
        ValidationResult fail = ValidationResult.failure(List.of(v));
        assertFalse(fail.isValid());
        assertEquals(1, fail.violations().size());
        assertEquals(v, fail.violations().get(0));
    }

    @Test
    void validator_with_rules_collects_violations() {
        Rule<String> notNull = s -> s == null ? new Violation("", "must not be null", null) : null;
        Rule<String> minLen = s -> s != null && s.length() < 3 ? new Violation("", "size must be >= 3", s) : null;
        Validator<String> validator = new Validator<>(List.of(notNull, minLen));

        ValidationResult r1 = validator.validate(null);
        assertFalse(r1.isValid());
        assertEquals(1, r1.violations().size());

        ValidationResult r2 = validator.validate("ab");
        assertFalse(r2.isValid());
        assertEquals(1, r2.violations().size());

        ValidationResult r3 = validator.validate("abcd");
        assertTrue(r3.isValid());
    }
}

