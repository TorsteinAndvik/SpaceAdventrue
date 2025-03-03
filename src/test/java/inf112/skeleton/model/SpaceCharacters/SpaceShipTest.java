package inf112.skeleton.model.SpaceCharacters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceShipTest {
  private SpaceShip spaceShip;

  @BeforeEach()
  void setup() {
    spaceShip = new Player("The Black Swan", "A beautiful pirate ship.",
            100, 0, 0, 100, 40, 53, 60);
  }

  @Test
  void takeDamageTest() {
    assertEquals(100, spaceShip.getHitPoints());
    assertEquals(100, spaceShip.getMaxHitPoints());
    spaceShip.takeDamage(50);
    assertEquals(50, spaceShip.getHitPoints());
    assertEquals(100, spaceShip.getMaxHitPoints());

  }

  @Test
  void repairTest() {
    assertEquals(100, spaceShip.getHitPoints());
    spaceShip.takeDamage(99);
    assertEquals(1, spaceShip.getHitPoints());
    spaceShip.repair(1);
    assertEquals(2, spaceShip.getHitPoints());
    spaceShip.repair(2000);
    assertEquals(100, spaceShip.getHitPoints());
  }

  @Test
  void destroyedTest() {
    assertFalse(spaceShip.isDestroyed());
    spaceShip.takeDamage(100);
    assertTrue(spaceShip.isDestroyed());
  }

  @Test
  void dealDamageTest() {
    SpaceShip enemyShip = new Player("The White Swan", "A boring ship.",
            49, 0, 0, 100, 40, 53, 60);
    assertEquals(49, enemyShip.getHitPoints());
    spaceShip.dealDamage(enemyShip);
    assertEquals(0, enemyShip.getHitPoints());
  }

}
