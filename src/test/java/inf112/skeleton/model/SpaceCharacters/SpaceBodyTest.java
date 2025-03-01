package inf112.skeleton.model.SpaceCharacters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceBodyTest {

  @Test
  void createSpaceBodyTest() {
    SpaceBody asteroid = new Asteroid(
            "Ceres", "Ceres is a dwarf planet",
            37, 68, 100, 600, 10_000, 45);
    assertEquals("Ceres", asteroid.getName());
    assertEquals("Ceres is a dwarf planet", asteroid.getDescription());
    assertEquals(37, asteroid.getX());
    assertEquals(68, asteroid.getY());
    assertEquals(600, asteroid.getMass());
    assertEquals(10_000, asteroid.getSpeed());
    assertEquals(45, asteroid.getRotationAngle());

  }

  @Test
  void ChangeAngleSpaceBodyTest() {
    SpaceBody asteroid = new Asteroid(
            "Ceres", "Ceres is a dwarf planet",
            0, 0, 1, 1, 0, 45);
    assertEquals(45, asteroid.getRotationAngle());
    asteroid.rotate(314);
    assertEquals(359, asteroid.getRotationAngle());
    asteroid.rotate(1);
    assertEquals(0, asteroid.getRotationAngle());

  }

  @Test
  void SetterGetterTests() {
    SpaceBody asteroid = new Asteroid(
            "Ceres", "Ceres is a dwarf planet",
            37, 68, 100, 600, 10_000, 45);
    asteroid.setName("Hygiea");
    asteroid.setDescription("Hygiea is a major asteroid located in the main asteroid belt");
    asteroid.setX(0);
    asteroid.setY(1);
    asteroid.setMass(500);
    asteroid.setSpeed(10);
    asteroid.setRotation(90);
    assertEquals("Hygiea", asteroid.getName());
    assertEquals("Hygiea is a major asteroid located in the main asteroid belt", asteroid.getDescription());
    assertEquals(0, asteroid.getX());
    assertEquals(1, asteroid.getY());
    assertEquals(500, asteroid.getMass());
    assertEquals(10, asteroid.getSpeed());
    assertEquals(90, asteroid.getRotationAngle());
  }

}
