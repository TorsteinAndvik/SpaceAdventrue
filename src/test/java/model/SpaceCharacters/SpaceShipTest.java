package model.SpaceCharacters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShipComponents.Components.ShipStructure;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceShipTest {

    private SpaceShip spaceShip;

    @BeforeEach()
    void setup() {
        spaceShip = new Player(null, "The Black Swan", "A beautiful pirate ship.",
                100, 0, 100);
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
        SpaceShip enemyShip = new Player(null, "The White Swan", "A boring ship.",
                49, 0, 49);
        assertEquals(49, enemyShip.getHitPoints());
        spaceShip.dealDamage(enemyShip, spaceShip.getDamage());
        assertEquals(0, enemyShip.getHitPoints());
    }

    @Test
    void radiusTest() {
        assertEquals(0, spaceShip.getRadius());

        SpaceShip ship = new Player(new ShipStructure(6, 8), "name", "description", 100, 0, 100);
        assertEquals(5f, ship.getRadius());
    }

}
