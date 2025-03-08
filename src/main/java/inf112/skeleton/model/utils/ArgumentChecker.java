package inf112.skeleton.model.utils;

import com.badlogic.gdx.math.Vector2;

public class ArgumentChecker {

  /**
   * Checks if the value is null.
   *
   * @param value   The value to check.
   * @param message The message to throw if the value is null.
   * @param <T>     The type of the value.
   */
  public static <T> void requireNonNull(T value, String message) {
    if (value == null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Ensures that the value is greater than or equal to zero.
   *
   * @param value   The value to check.
   * @param message The message to throw if the value is negative.
   */
  public static <T extends Number> void greaterOrEqualToZero(T value, String message) {
    if (value.doubleValue() < 0) {
      throw new IllegalArgumentException(message);
    }
  }


  /**
   * Ensures that a string is not empty or null.
   *
   * @param value   The string to check.
   * @param message The message to throw if the string is invalid.
   */
  public static void requireNonEmptyString(String value, String message) {
    requireNonNull(value, message);
    if (value.trim().isEmpty()) {
      throw new IllegalArgumentException(message);
    }
  }
}
