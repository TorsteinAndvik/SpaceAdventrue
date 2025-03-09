package inf112.skeleton.model.SpaceCharacters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceBodyTest {

  @Test
  void createSpaceBodyTest() {
    SpaceBody asteroid = new Asteroid(
            "Ceres", "Ceres is a dwarf planet",
            37, 68, 0, 17, 100, 600, 45, 200);
    assertEquals("Ceres", asteroid.getName());
    assertEquals("Ceres is a dwarf planet", asteroid.getDescription());
    assertEquals(37, asteroid.getX());
    assertEquals(68, asteroid.getY());
    assertEquals(0, asteroid.getVelocity().x);
    assertEquals(17, asteroid.getVelocity().y);
    assertEquals(600, asteroid.getMass());
    assertEquals(45, asteroid.getRotationAngle());
    assertEquals(200, asteroid.getRadius());

  }

  @Test
  void ChangeAngleSpaceBodyTest() {
    SpaceBody asteroid = new Asteroid(
            "Ceres", "Ceres is a dwarf planet",
            100, 0, 0, 0, 1, 1, 45, 100);
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
            100, 68, 0, 0, 100, 600, 45, 100);
    asteroid.setName("Hygiea");
    asteroid.setDescription("Hygiea is a major asteroid located in the main asteroid belt");
    asteroid.setX(0);
    asteroid.setY(1);
    asteroid.setVelocityX(2);
    asteroid.setVelocityY(3);
    asteroid.setMass(500);
    asteroid.setRotation(90);
    asteroid.setRadius(200);
    assertEquals("Hygiea", asteroid.getName());
    assertEquals("Hygiea is a major asteroid located in the main asteroid belt", asteroid.getDescription());
    assertEquals(0, asteroid.getX());
    assertEquals(1, asteroid.getY());
    assertEquals(2, asteroid.getVelocity().x);
    assertEquals(3, asteroid.getVelocity().y);
    assertEquals(500, asteroid.getMass());
    assertEquals(90, asteroid.getRotationAngle());
    assertEquals(200, asteroid.getRadius());
  }

}
