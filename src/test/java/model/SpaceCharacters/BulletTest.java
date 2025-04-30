package model.SpaceCharacters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BulletTest {

    @Test
    void testBulletCreation() {
        Bullet b = new Bullet("Ball", "rusty", 1, 2, 3, 4, 5, false);
        assertSame("Ball", b.getName());
        assertSame("rusty", b.getDescription());
        assertEquals(1, b.getX());
        assertEquals(2, b.getY());
        assertEquals(3, b.getSpeed());
        assertEquals(4, b.getRotationAngle());
        assertEquals(5, b.getRadius());
        assertFalse(b.isPlayerBullet());

    }

    @Test
    void testConstructorDifferences() {
        Bullet b1 = new Bullet("Ball", "rusty", 1, 2, 3, 45, 5, false);
        Bullet b2 = new Bullet("Ball", "rusty", 1, 2, 3, 0, 100, 45, 5, false);

        assertEquals(b1.getSpeed(), b2.getSpeed(), 0.00001f);
        assertEquals(1, b1.getHitPoints());
        assertEquals(100, b2.getHitPoints());

    }

    @Test
    void testInit() {
        Bullet b = new Bullet("Ball", "rusty", 10, 20, 30, 40, 50, false);
        b.init(1, 2, 3, 4, 5, true);

        assertEquals(1, b.getX());
        assertEquals(2, b.getY());
        assertEquals(3, b.getSpeed());
        assertEquals(4, b.getRotationAngle());
        assertEquals(5, b.getRadius());
        assertTrue(b.isPlayerBullet());
    }

    @Test
    void testReset() {
        Bullet b = new Bullet("Ball", "rusty", 10, 20, 30, 40, 50, true);
        b.reset();

        assertEquals(0, b.getX());
        assertEquals(0, b.getY());
        assertEquals(0, b.getSpeed());
        assertEquals(0, b.getRotationAngle());
        assertEquals(0, b.getRadius());
        assertEquals(0, b.getHitPoints());
        assertEquals(1, b.getMaxHitPoints());
        assertFalse(b.isPlayerBullet());
    }
}
