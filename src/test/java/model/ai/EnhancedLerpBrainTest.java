package model.ai;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShipComponents.ShipFactory;
import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;
import model.utils.SpaceCalculator;

public class EnhancedLerpBrainTest {

    private EnhancedLerpBrain enhancedBrain;
    private float startAngle = 0f;
    private float startX = 0f;
    private float startY = 0f;
    private LerpBrain lerpBrain;
    private EnemyShip lerpEnemy;
    private EnemyShip enhancedEnemy;
    private Player player;

    @BeforeEach
    public void setup() {
        lerpEnemy = new EnemyShip(ShipFactory.simpleShip(), "enemy", "see name", startX, startY, startAngle);
        enhancedEnemy = new EnemyShip(ShipFactory.simpleShip(), "enemy", "see name", startX, startY, startAngle);
        player = new Player(ShipFactory.playerShip(), "player", "same here", -100f, -100f);
        enhancedBrain = new EnhancedLerpBrain(enhancedEnemy, player);
        lerpBrain = new LerpBrain(lerpEnemy, player);
    }

    @Test
    public void newAngleTest() {
        // simulate 1 frame at 30FPS
        float delta = 1f / 30f;
        enhancedBrain.update(delta);
        lerpBrain.update(delta);

        float maxRotationAngle = delta * lerpEnemy.maxRotationalVelocity();
        float lerpAngleDelta = Math.abs(startAngle - lerpEnemy.getRotationAngle());
        float enhancedAngleDelta = Math.abs(startAngle - enhancedEnemy.getRotationAngle());
        assertTrue(lerpAngleDelta > maxRotationAngle);
        assertFalse(enhancedAngleDelta == maxRotationAngle);
    }

    @Test
    public void newPositionDeltaTest() {
        // simulate 1 frame at 30FPS
        float delta = 1f / 30f;
        enhancedBrain.update(delta);
        lerpBrain.update(delta);

        float maxDistance = delta * lerpEnemy.maxSpeed();
        float lerpDistance = SpaceCalculator.distance(lerpEnemy.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass());
        float enhancedDistance = SpaceCalculator.distance(enhancedEnemy.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass());
        assertTrue(lerpDistance > maxDistance);
        assertFalse(enhancedDistance == maxDistance);
    }

    @Test
    public void avoidCollisionTest() {
        float spawnOffset = 2f;
        EnemyShip enhancedEnemy2 = new EnemyShip(
                ShipFactory.simpleShip(), "enemy", "see name", startX + spawnOffset, startY + spawnOffset, startAngle);

        EnemyShip lerpEnemy2 = new EnemyShip(
                ShipFactory.simpleShip(), "enemy", "see name", startX + spawnOffset, startY + spawnOffset, startAngle);

        EnhancedLerpBrain enhancedBrain2 = new EnhancedLerpBrain(enhancedEnemy2, player);
        LerpBrain lerpBrain2 = new LerpBrain(lerpEnemy2, player);

        // simulate 100 frames at 30FPS
        float delta = 1f / 30f;
        for (int i = 0; i < 100; i++) {
            // make enhanced brains avoid each other
            enhancedBrain.nearCollision(enhancedEnemy2);
            enhancedBrain2.nearCollision(enhancedEnemy);

            // update all ships
            enhancedBrain.update(delta);
            enhancedBrain2.update(delta);
            lerpBrain.update(delta);
            lerpBrain2.update(delta);

            // check that enhanced ships never get too close
            float distance = SpaceCalculator.distance(enhancedEnemy.getAbsoluteCenterOfMass(),
                    enhancedEnemy2.getAbsoluteCenterOfMass());
            assertTrue(distance > 0.5f * (enhancedEnemy.getProximityRadius() + enhancedEnemy2.getProximityRadius()));
        }

        // for comparison, LerpBrains should cause overlapping by now
        float distance = SpaceCalculator.distance(lerpEnemy.getAbsoluteCenterOfMass(),
                lerpEnemy2.getAbsoluteCenterOfMass());
        assertTrue(distance < 0.1f * (lerpEnemy.getProximityRadius() + lerpEnemy2.getProximityRadius()));
    }
}
