package model.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ArgumentCheckerTest {

    @Test
    void requireNonNull_valueIsNonNull() {
        String string = "Hello";
        assertDoesNotThrow(() -> ArgumentChecker.requireNonNull(string, "Value should not be null"));
    }

    @Test
    void requireNonNull_valueIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ArgumentChecker.requireNonNull(null, "Value is null"));
        assertEquals("Value is null", exception.getMessage());
    }

    @Test
    void greaterOrEqualToZero_zero() {
        assertDoesNotThrow(() -> ArgumentChecker.greaterOrEqualToZero(0, "Must be >= 0"));
    }

    @Test
    void greaterOrEqualToZero_positive() {
        assertDoesNotThrow(() -> ArgumentChecker.greaterOrEqualToZero(5.5, "Must be >= 0"));
    }

    @Test
    void greaterOrEqualToZero_negative() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ArgumentChecker.greaterOrEqualToZero(-1, "Negative value"));
        assertEquals("Negative value", exception.getMessage());
    }

    @Test
    void requireNonEmptyString_validString() {
        assertDoesNotThrow(() -> ArgumentChecker.requireNonEmptyString("test", "String must not be empty"));
    }

    @Test
    void requireNonEmptyString_stringIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ArgumentChecker.requireNonEmptyString(null, "Null string"));
        assertEquals("Null string", exception.getMessage());
    }

    @Test
    void requireNonEmptyString_stringIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ArgumentChecker.requireNonEmptyString("", "Empty string"));
        assertEquals("Empty string", exception.getMessage());
    }

    @Test
    void requireNonEmptyString_stringIsWhitespaceOnly() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ArgumentChecker.requireNonEmptyString("   ", "Whitespace only"));
        assertEquals("Whitespace only", exception.getMessage());
    }
}

