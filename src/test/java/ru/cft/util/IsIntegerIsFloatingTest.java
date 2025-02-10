package ru.cft.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.cft.util.NumericUtil.isSimpleFloatingPointNumber;
import static ru.cft.util.NumericUtil.isSimpleInteger;

public class IsIntegerIsFloatingTest {
    @Test
    void regular_integer_true() {
        assertTrue(isSimpleInteger("1"));
    }

    @Test
    void signed_integer_true() {
        assertTrue(isSimpleInteger("+1"));
        assertTrue(isSimpleInteger("-1"));
    }

    @Test
    void empty_or_null_integer_false() {
        assertFalse(isSimpleInteger(""));
        assertFalse(isSimpleInteger(null));
    }

    @Test
    void not_an_integer_false() {
        assertFalse(isSimpleInteger("-1.1"));
        assertFalse(isSimpleInteger("f"));
        assertFalse(isSimpleInteger("-"));
    }

    @Test
    void regular_floating_true() {
        assertTrue(isSimpleFloatingPointNumber("1.1"));
        assertTrue(isSimpleFloatingPointNumber("235234.123"));
    }

    @Test
    void signed_floating_true() {
        assertTrue(isSimpleFloatingPointNumber("+1.1"));
        assertTrue(isSimpleFloatingPointNumber("-1.1"));
    }

    @Test
    void empty_or_null_floating_false() {
        assertFalse(isSimpleFloatingPointNumber(""));
        assertFalse(isSimpleFloatingPointNumber(null));
    }

    @Test
    void not_floating_point_false() {
        assertFalse(isSimpleFloatingPointNumber("1.1D"));
        assertFalse(isSimpleFloatingPointNumber("f"));
        assertFalse(isSimpleFloatingPointNumber("1."));
        assertFalse(isSimpleFloatingPointNumber("1-.1"));
        assertFalse(isSimpleFloatingPointNumber("1..1"));
        assertFalse(isSimpleFloatingPointNumber("+"));
    }
}
