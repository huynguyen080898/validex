package com.nqh.validex.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {
    @Test
    void okResultShouldBeValid() {
        ValidationResult result = ValidationResult.ok();

        assertTrue(result.isValid());
        assertEquals(List.of(), result.violations());
        assertSame(ValidationResult.SUCCESS, result); // singleton check
    }

    @Test
    void failureShouldContainViolations() {
        Violation v = new Violation("name", "must not be null", null);
        ValidationResult result = ValidationResult.failure(List.of(v));

        assertFalse(result.isValid());
        assertEquals(1, result.violations().size());
        assertEquals("must not be null", result.violations().get(0).message());
        assertEquals("name", result.violations().get(0).path());
        assertNull(result.violations().get(0).invalidValue());
    }

    @Test
    void failureShouldDefensivelyCopyList() {
        Violation v = new Violation("f", "bad", "x");
        List<Violation> list = List.of(v);

        ValidationResult result = ValidationResult.failure(list);

        List<Violation> copy = result.violations();
        assertThrows(UnsupportedOperationException.class, () -> copy.add(v));
    }
}