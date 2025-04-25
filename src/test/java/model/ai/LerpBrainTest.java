package model.ai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShipComponents.ShipFactory;
import model.ShipComponents.Components.Turret;
import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;

public class LerpBrainTest {

    private LerpBrain lerpBrain;
    private EnemyShip enemy;
    private Player player;

    @BeforeEach
    public void setup() {
        enemy = new EnemyShip(ShipFactory.simpleShip(), "enemy", "see name", 0, 0, 0);
        player = new Player(ShipFactory.playerShip(), "player", "same here", 4f, 4f);
        lerpBrain = new LerpBrain(enemy, player);
    }

    @Test
    public void hoverDistanceTest() {
        float expected = enemy.getProximityRadius() + player.getProximityRadius();
        assertEquals(expected, lerpBrain.hoverDistance());
    }

    @Test
    public void inFiringRangeTest() {
        assertFalse(lerpBrain.inFiringRange());
        enemy.setPosition(new FloatPair(3f, 3f));
        assertTrue(lerpBrain.inFiringRange());
        enemy.setPosition(new FloatPair(100f, 100f));
        assertFalse(lerpBrain.inFiringRange());
    }

    @Test
    public void shootTest() {
        for (Turret turret : enemy.getShipStructure().getTurrets()) {
            assertFalse(turret.isShooting());
        }

        for (Turret turret : enemy.getShipStructure().getTurrets()) {
            assertFalse(turret.isShooting());
        }

        lerpBrain.shoot(true);

        for (Turret turret : enemy.getShipStructure().getTurrets()) {
            assertTrue(turret.isShooting());
        }

        lerpBrain.shoot(false);

        for (Turret turret : enemy.getShipStructure().getTurrets()) {
            assertFalse(turret.isShooting());
        }

        for (Turret turret : enemy.getShipStructure().getTurrets()) {
            assertFalse(turret.isShooting());
        }
    }

    @Test
    public void updateTest() {

        float distance = SpaceCalculator.distance(player.getAbsoluteCenterOfMass(), enemy.getAbsoluteCenterOfMass());
        float rotation = enemy.getRotationAngle();

        for (Turret turret : enemy.getShipStructure().getTurrets()) {
            assertFalse(turret.isShooting());
        }

        // simulate 10 frames at 30FPS
        for (int i = 0; i < 10; i++) {
            lerpBrain.update(1f / 30f);
        }

        for (Turret turret : enemy.getShipStructure().getTurrets()) {
            assertTrue(turret.isShooting());
        }

        float newDistance = SpaceCalculator.distance(player.getAbsoluteCenterOfMass(), enemy.getAbsoluteCenterOfMass());
        float newRotation = enemy.getRotationAngle();

        assertTrue(distance > newDistance);
        assertTrue(newRotation > rotation); // enemy starts at 0 rotation
    }
}
