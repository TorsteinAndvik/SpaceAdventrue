package inf112.skeleton.model.SpaceCharacters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CollisionTest {
  @Test
  void CrashTest() {
    Projectile asteroid1 = new Asteroid(
            "Ceres", "Ceres is a dwarf planet",
            100, 37, 68, 600, 10_000, 45, 200);
    Projectile asteroid2 = new Asteroid(
        "Hygia", "Ceres is a dwarf planet",
        100, 0, 1, 1, 0, 45, 100);
    assertTrue(asteroid1.checkCollision(asteroid2));
  }

  @Test
  void NoCrashTest() {
    Projectile asteroid1 = new Asteroid(
            "Ceres", "Ceres is a dwarf planet",
            100, 37, 68, 600, 10_000, 45, 10);
    Projectile asteroid2 = new Asteroid(
        "Hygia", "Hygia is a dwarf planet",
        100, 0, 1, 1, 0, 45, 10);
    assertTrue(!asteroid1.checkCollision(asteroid2));

  }
}
