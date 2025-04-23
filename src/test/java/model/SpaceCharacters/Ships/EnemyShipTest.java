package model.SpaceCharacters.Ships;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.ShipComponents.ShipFactory;
import model.ai.LerpBrain;
import model.utils.FloatPair;

public class EnemyShipTest {

    @Test
    public void constructorTest() {
        EnemyShip enemy = new EnemyShip(ShipFactory.simpleShip(), "name", "desc", 0, 0, 0);
        Player player = new Player(ShipFactory.playerShip(), "player", "decription", 1, 1);
        assertTrue(enemy.isAccelerating());

        LerpBrain brain = new LerpBrain(enemy, player);

        assertNotEquals(brain, enemy.brain);
        enemy.setBrain(brain);
        assertEquals(brain, enemy.brain);

        FloatPair oldPos = enemy.getAbsoluteCenterOfMass();
        float oldRotation = enemy.getRotationAngle();

        enemy.update(0.1f);
        assertNotEquals(oldPos, enemy.getAbsoluteCenterOfMass());
        assertNotEquals(oldRotation, enemy.getRotationAngle());
    }
}
