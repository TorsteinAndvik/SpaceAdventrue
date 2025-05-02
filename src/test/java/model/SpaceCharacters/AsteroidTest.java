package model.SpaceCharacters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AsteroidTest {


    @Test
    void createEmptyAsteroidTest() {
        Asteroid asteroid = new Asteroid("Ceres", "Ceres is a dwarf planet");

        assertEquals("Ceres", asteroid.getName());
        assertEquals("Ceres is a dwarf planet", asteroid.getDescription());
        assertEquals(0, asteroid.getX());
        assertEquals(0, asteroid.getY());
        assertEquals(0, asteroid.getVelocity().x);
        assertEquals(0, asteroid.getVelocity().y);
        assertEquals(1, asteroid.getHitPoints());
        assertEquals(1, asteroid.getMaxHitPoints());
        assertEquals(0, asteroid.getMass());
        assertEquals(0, asteroid.getRotationAngle());
        assertEquals(0, asteroid.getRadius());
        assertEquals(0, asteroid.getRotationSpeed());
        assertFalse(asteroid.isLarge());
    }

    @Test
    void createAsteroidTest() {
        Asteroid asteroid = new Asteroid("Ceres", "Ceres is a dwarf planet");
        asteroid.init(37, 68, 600, 200, 5, 10_000, 45, 1, 0, true);

        assertEquals("Ceres", asteroid.getName());
        assertEquals("Ceres is a dwarf planet", asteroid.getDescription());
        assertEquals(37, asteroid.getX());
        assertEquals(68, asteroid.getY());
        assertEquals(600, asteroid.getVelocity().x);
        assertEquals(200, asteroid.getVelocity().y);
        assertEquals(5, asteroid.getHitPoints());
        assertEquals(5, asteroid.getMaxHitPoints());
        assertEquals(10_000, asteroid.getMass());
        assertEquals(45, asteroid.getRotationAngle());
        assertEquals(1, asteroid.getRadius());
        assertEquals(0, asteroid.getRotationSpeed());
        assertTrue(asteroid.isLarge());
    }

    @Test
    void testResourceValue() {
        Asteroid a = new Asteroid();
        a.setMass(10);
        a.setRadius(5);
        int multiplier = 30;
        int value = (int) (a.getMass() * a.radius) * multiplier;
        assertEquals(value, a.getResourceValue());

    }
}
