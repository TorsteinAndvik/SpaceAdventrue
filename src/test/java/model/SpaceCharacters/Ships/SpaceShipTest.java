package model.SpaceCharacters.Ships;

import grid.CellPosition;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.ShipFactory;
import model.utils.FloatPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShipComponents.ShipStructure;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceShipTest {

    private SpaceShip spaceShip;
    private final int startHealth = 8;

    @BeforeEach()
    void setup() {
        spaceShip = new Player(ShipFactory.simpleShip(), "The Black Swan", "A beautiful pirate ship.", 0f, 100f);
    }

    @Test
    void takeDamageTest() {
        assertEquals(startHealth, spaceShip.getHitPoints());
        assertEquals(startHealth, spaceShip.getMaxHitPoints());
        spaceShip.takeDamage(5);
        assertEquals(startHealth - 5, spaceShip.getHitPoints());
        assertEquals(startHealth, spaceShip.getMaxHitPoints());

    }

    @Test
    void repairTest() {
        assertEquals(startHealth, spaceShip.getHitPoints());
        spaceShip.takeDamage(startHealth - 1);
        assertEquals(1, spaceShip.getHitPoints());
        spaceShip.repair(1);
        assertEquals(2, spaceShip.getHitPoints());
        spaceShip.repair(2000);
        assertEquals(startHealth, spaceShip.getHitPoints());
    }

    @Test
    void destroyedTest() {
        assertFalse(spaceShip.isDestroyed());
        spaceShip.takeDamage(100);
        assertTrue(spaceShip.isDestroyed());
    }

    @Test
    void dealDamageTest() {
        SpaceShip enemyShip = new EnemyShip(ShipFactory.simpleShip(), "The White Swan", "A boring ship.", 0f, 49f, 0f);
        assertEquals(startHealth, enemyShip.getHitPoints());
        spaceShip.dealDamage(enemyShip, spaceShip.getDamage());
        assertEquals(0, enemyShip.getHitPoints());
    }

    @Test
    void testAbsoluteCenterOfMass() {
        SpaceShip ship = new Player(new ShipStructure(3, 3), "name", "description", 0f, 0f);
        ShipStructure structure = ship.getShipStructure();

        assertTrue(structure.addUpgrade(new CellPosition(0, 0), new Fuselage()));
        assertEquals(new FloatPair(0, 0), ship.getAbsoluteCenterOfMass());

        assertTrue(structure.addUpgrade(new CellPosition(1, 0), new Fuselage()));
        assertEquals(new FloatPair(0, 0.5f), ship.getAbsoluteCenterOfMass());

        assertTrue(structure.addUpgrade(new CellPosition(2, 0), new Fuselage()));
        assertEquals(new FloatPair(0, 1f), ship.getAbsoluteCenterOfMass());
    }
}
