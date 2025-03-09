package inf112.skeleton.model.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RotationTest {

  @Test
  void SetterGetterTest() {
    Rotation r = new Rotation(0);
    assertEquals(0, r.getAngle());
    r.setAngle(45);
    assertEquals(45, r.getAngle());
  }

  @Test
  void normalizationTest() {
    Rotation r = new Rotation(-1);
    assertEquals(359, r.getAngle());
    r.rotate(1);
    assertEquals(0, r.getAngle());
    r.rotate(3 * 360 + 180);
    assertEquals(180, r.getAngle());
    r.rotate(-3 * 360 - 180);
    assertEquals(0, r.getAngle());
  }

  @Test
  void rotationSpeedTest() {
    Rotation r = new Rotation(0, 2);
    r.update(0.5f);
    assertEquals(1, r.getAngle());
    r.update(1f);
    assertEquals(3, r.getAngle());
  }


  @Test
  void negativeRotationSpeedTest() {
    Rotation r = new Rotation(5, -10);
    r.update(1f);
    assertEquals(355, r.getAngle());
    r.update(1f);
    assertEquals(345, r.getAngle());
  }


  @Test
  void toStringTest() {
    Rotation r = new Rotation(180);
    assertEquals("Rotation{angle=180.0}", r.toString());
  }
}
