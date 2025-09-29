package com.nqh.validex.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    @Test
    void validatorShouldReturnOkWhenAllRulesPass() {
        Rule<String> notEmpty = s ->
                (s != null && !s.isEmpty())
                        ? null
                        : new Violation("self", "must not be empty", s);

        Validator<String> validator = new Validator<>(List.of(notEmpty));

        ValidationResult result = validator.validate("hello");
        assertTrue(result.isValid());
    }

    @Test
    void validatorShouldReturnFailureWhenRuleFails() {
        Rule<String> notEmpty = s ->
                (s != null && !s.isEmpty())
                        ? null
                        : new Violation("self", "must not be empty", s);

        Validator<String> validator = new Validator<>(List.of(notEmpty));

        ValidationResult result = validator.validate("");
        assertFalse(result.isValid());
        assertEquals(1, result.violations().size());
        assertEquals("must not be empty", result.violations().get(0).message());
        assertEquals("self", result.violations().get(0).path());
        assertEquals("", result.violations().get(0).invalidValue());
    }

    @Test
    void validatorShouldAggregateMultipleViolations() {
        Rule<String> notNull = s ->
                (s != null)
                        ? null
                        : new Violation("self", "must not be null", null);

        Rule<String> notEmpty = s ->
                (s != null && !s.isEmpty())
                        ? null
                        : new Violation("self", "must not be empty", s);

        Validator<String> validator = new Validator<>(List.of(notNull, notEmpty));

        ValidationResult result = validator.validate("");
        assertFalse(result.isValid());
        assertEquals(1, result.violations().size()); // only notEmpty fails here

        result = validator.validate(null);
        assertFalse(result.isValid());
        assertEquals(2, result.violations().size()); // notNull + notEmpty fail
    }

    @Test
    void emptyValidatorShouldAlwaysReturnOk() {
        Validator<String> validator = new Validator<>(List.of());

        ValidationResult result = validator.validate(null);
        assertTrue(result.isValid());
    }

}