package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Diamond;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DiamondFactoryTest {

    DiamondFactory diamondFactory;
    Asteroid asteroid;

    @BeforeEach
    void setup() {
        diamondFactory = new DiamondFactory();
        asteroid = new Asteroid("Asteroid", "Asteroid", 5, 10, 15, 20, 25, 30, 35, 40);
    }

    @Test
    void testSpawn() {

        Diamond diamond = diamondFactory.spawn(asteroid);

        assertEquals(diamond.getX(), asteroid.getX(), asteroid.getRadius());
        assertEquals(diamond.getY(), asteroid.getY(), asteroid.getRadius());

        assertTrue(diamond.getValue() > 0);
        assertTrue(diamond.getValue() < asteroid.getResourceValue());

    }

    @Test
    void testFree() {
        Diamond diamond = diamondFactory.spawn(asteroid);
        Diamond diamond_reset = new Diamond();
        diamond_reset.reset();

        assertNotEquals(diamond.getX(), diamond_reset.getX());
        assertNotEquals(diamond.getY(), diamond_reset.getY());
        assertNotEquals(diamond.getValue(), diamond_reset.getValue());

        diamondFactory.free(diamond);

        assertEquals(diamond.getX(), diamond_reset.getX());
        assertEquals(diamond.getY(), diamond_reset.getY());
        assertEquals(diamond.getValue(), diamond_reset.getValue());
    }
}
