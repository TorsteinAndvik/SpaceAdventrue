package model.SpaceCharacters;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import model.utils.FloatPair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpaceBodyTest {

    @Test
    void createSpaceBodyWithoutStatsTest() {
        SpaceBody bullet = new SpaceBody("name", "description", CharacterType.BULLET) {
        };

        assertEquals("name", bullet.getName());
        assertEquals("description", bullet.getDescription());
        assertEquals(0f, bullet.getX());
        assertEquals(0f, bullet.getY());
        assertEquals(0f, bullet.getVelocity().x);
        assertEquals(0f, bullet.getVelocity().y);
        assertEquals(0f, bullet.getMass());
        assertEquals(0f, bullet.getRotationAngle());
        assertEquals(0f, bullet.getRadius());
        assertEquals(0f, bullet.getRotationSpeed());
    }

    @Test
    void createSpaceBodyForRenderingTest() {
        SpaceBody bullet = new SpaceBody("name", "description", CharacterType.BULLET, 5f, -2f,
                -3f, 15f) {
        };

        assertEquals("name", bullet.getName());
        assertEquals("description", bullet.getDescription());
        assertEquals(5f, bullet.getX());
        assertEquals(-2f, bullet.getY());
        assertEquals(0f, bullet.getVelocity().x);
        assertEquals(0f, bullet.getVelocity().y);
        assertEquals(0f, bullet.getMass());
        assertEquals(357f, bullet.getRotationAngle());
        assertEquals(15f, bullet.getRadius());
        assertEquals(0f, bullet.getRotationSpeed());
    }

    @Test
    void createSpaceBodyTest() {
        SpaceBody asteroid = new Asteroid(
                "Ceres", "Ceres is a dwarf planet",
                37, 68, 600, 200, 10_000, 45, 1, 0);
        assertEquals("Ceres", asteroid.getName());
        assertEquals("Ceres is a dwarf planet", asteroid.getDescription());
        assertEquals(37, asteroid.getX());
        assertEquals(68, asteroid.getY());
        assertEquals(600, asteroid.getVelocity().x);
        assertEquals(200, asteroid.getVelocity().y);
        assertEquals(10_000, asteroid.getMass());
        assertEquals(45, asteroid.getRotationAngle());
        assertEquals(1, asteroid.getRadius());
        assertEquals(0, asteroid.getRotationSpeed());
    }

    @Test
    void changeAngleSpaceBodyTest() {
        SpaceBody asteroid = new Asteroid(
                "Ceres", "Ceres is a dwarf planet",
                100, 0, 1, 1, 10, 45, 100, 1);
        assertEquals(45, asteroid.getRotationAngle());
        asteroid.rotate(314);
        assertEquals(359, asteroid.getRotationAngle());
        asteroid.rotate(1);
        assertEquals(0, asteroid.getRotationAngle());
    }

    @Test
    void setterGetterTests() {
        SpaceBody asteroid = new Asteroid(
                "Ceres", "Ceres is a dwarf planet",
                100, 68, 100, 600, 10_000, 45, 100, 1);
        asteroid.setName("Hygiea");
        asteroid.setDescription("Hygiea is a major asteroid located in the main asteroid belt");
        asteroid.setX(0);
        asteroid.setY(1);
        asteroid.setMass(500);
        asteroid.setRotation(90);
        asteroid.setRadius(200);
        assertEquals("Hygiea", asteroid.getName());
        assertEquals("Hygiea is a major asteroid located in the main asteroid belt",
                asteroid.getDescription());
        assertEquals(0, asteroid.getX());
        assertEquals(1, asteroid.getY());
        assertEquals(500, asteroid.getMass());
        assertEquals(90, asteroid.getRotationAngle());
        assertEquals(200, asteroid.getRadius());
        assertEquals(CharacterType.ASTEROID, asteroid.getCharacterType());

        asteroid.setPosition(new FloatPair(150f, 30f));
        assertEquals(150f, asteroid.getX());
        assertEquals(30f, asteroid.getY());

        assertTrue(Math.abs(Math.sqrt(Math.pow(100f, 2f) + Math.pow(600f, 2f)) - asteroid.getSpeed()) < 0.0001f);

        assertEquals(1f, asteroid.getRotationSpeed());
        asteroid.setRotationSpeed(3f);
        assertEquals(3f, asteroid.getRotationSpeed());
        asteroid.scaleRotationSpeed(0.5f);
        assertEquals(1.5f, asteroid.getRotationSpeed());
        asteroid.addRotationSpeed(-1f);
        assertEquals(0.5f, asteroid.getRotationSpeed());

        assertEquals(100f, asteroid.getVelocity().x);
        asteroid.setVelocityX(5f);
        assertEquals(5f, asteroid.getVelocity().x);

        assertEquals(600f, asteroid.getVelocity().y);
        asteroid.setVelocityY(-2f);
        assertEquals(-2f, asteroid.getVelocity().y);

        asteroid.addVelocityX(15f);
        asteroid.addVelocityY(32f);
        assertEquals(new Vector2(20f, 30f), asteroid.getVelocity());

        Vector2 newVelocity = new Vector2(33f, -33f);
        asteroid.setVelocity(newVelocity);

        assertEquals(newVelocity, asteroid.getVelocity());

        asteroid.scaleVelocity(2f);
        assertEquals(new Vector2(66f, -66f), asteroid.getVelocity());
    }

    @Test
    void translateTest() {
        SpaceBody asteroid = new Asteroid(
                "Ceres", "Ceres is a dwarf planet",
                100f, 68f, 100f, 600f, 10_000f, 45f, 100f, 1f);

        asteroid.translate(new FloatPair(100f, 20f));
        assertEquals(200f, asteroid.getX());
        assertEquals(88f, asteroid.getY());

        asteroid.translate(new FloatPair(-40f, 0f));
        assertEquals(160f, asteroid.getX());
        assertEquals(88, asteroid.getY());
    }

    @Test
    void updateTest() {
        SpaceBody asteroid = new Asteroid(
                "Ceres", "Ceres is a dwarf planet",
                100f, 68f, 100f, 600f, 10_000f, 45f, 100f, 1.5f);

        asteroid.update(2f);
        assertEquals(300f, asteroid.getX());
        assertEquals(1268f, asteroid.getY());
        assertEquals(48f, asteroid.getRotationAngle());
    }

    @Test
    void toStringTest() {
        SpaceBody asteroid = new Asteroid(
                "Ceres", "Ceres is a dwarf planet",
                100, 68, 100, 600, 10_000, 45, 100, 1);
        assertEquals("Ceres", asteroid.toString());
    }

    @Test
    void resourceValueTest() {
        SpaceBody body = new SpaceBody("name", "description", CharacterType.BULLET) {
        };

        assertEquals(0, body.getResourceValue());
    }
}
