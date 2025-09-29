package com.nqh.validex.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ViolationTest {
    @Test
    void recordShouldExposeFields() {
        Violation v = new Violation("user.name", "must not be null", null);

        assertEquals("must not be null", v.message());
        assertEquals("user.name", v.path());
        assertNull(v.invalidValue());
    }

    @Test
    void recordEqualityShouldWork() {
        Violation v1 = new Violation("field", "err", 123);
        Violation v2 = new Violation("field", "err", 123);

        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }
}