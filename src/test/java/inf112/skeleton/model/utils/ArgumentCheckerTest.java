package inf112.skeleton.model.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentCheckerTest {

  @Test
  void testValidArguments() {
    assertDoesNotThrow(() -> {
      ArgumentChecker.greaterOrEqualToZero(0, "This should not throw an exception.");
    });
    assertDoesNotThrow(() -> {
      ArgumentChecker.greaterOrEqualToZero(100, "This should not throw an exception.");
    });
    assertDoesNotThrow(() -> {
      Rotation r = new Rotation(0);
      ArgumentChecker.requireNonNull(r, "This should not throw an exception.");
    });
    assertDoesNotThrow(() -> {
      String s = "I am not null";
      ArgumentChecker.requireNonNull(s, "This should not throw an exception.");
    });
  }

  @Test
  void testInvalidGreaterOrEqualToZero() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      ArgumentChecker.greaterOrEqualToZero(-1, "This will throw an exception.");
    });
    assertEquals("This will throw an exception.", exception.getMessage());
  }

  @Test
  void testInvalidRequiresNotNull() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      ArgumentChecker.requireNonNull(null, "This will throw an exception.");
    });
    assertEquals("This will throw an exception.", exception.getMessage());
  }

  @Test
  void testInvalidRequireNonEmptyString() {
    Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
      ArgumentChecker.requireNonEmptyString(null, "Not allowed to be null.");
    });
    assertEquals("Not allowed to be null.", exception1.getMessage());
    Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
      ArgumentChecker.requireNonEmptyString("", "No empty strings!");
    });
    assertEquals("No empty strings!", exception2.getMessage());
    Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
      ArgumentChecker.requireNonEmptyString("    ", "No strings with only spaces!");
    });
    assertEquals("No strings with only spaces!", exception3.getMessage());


  }
}