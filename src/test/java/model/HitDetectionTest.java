package model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShipComponents.ShipFactory;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;

public class HitDetectionTest {

    SpaceGameModel model;
    HitDetection hitDetection;
    Asteroid asteroid;
    Bullet laser;
    Player player;
    EnemyShip enemyShip;

    @BeforeEach
    public void setup() {
        model = mock(SpaceGameModel.class);

        asteroid = new Asteroid("asteroid", "an asteroid", -5, -5, 0, 0, 1f, 0f, 1f, 0f);
        laser = new Bullet("bullet", "a bullet", 3, 3, 0, 0, 0.25f, false);
        player = new Player(ShipFactory.playerShip(), "player", "the player's ship", 8, 8);
        enemyShip = new EnemyShip(ShipFactory.simpleShip(), "dreadnought", "an enemy ship",
                0, 5, 0);

        hitDetection = new HitDetection(model);
        hitDetection.addCollider(asteroid);
        hitDetection.addCollider(laser);
        hitDetection.addCollider(player);
        hitDetection.addCollider(enemyShip);
    }

    @Test
    public void addColliderTest() {
        assertTrue(hitDetection.colliders.contains(asteroid));
        assertTrue(hitDetection.colliders.contains(laser));
        assertTrue(hitDetection.colliders.contains(player));
        assertTrue(hitDetection.colliders.contains(enemyShip));
    }

    @Test
    public void asteroidLaserCollisionTest() {
        // ensure no collisions initially
        hitDetection.checkCollisions();
        verify(model, never()).handleCollision(any(), any());

        // make asteroid and laser overlap
        FloatPair pos = new FloatPair(1000f, 1000f);
        asteroid.setPosition(pos);
        laser.setPosition(pos);

        hitDetection.checkCollisions();
        verify(model, times(1)).handleCollision(any(), any());

        // make laser and asteroid barely overlap
        float newX = pos.x() + asteroid.getRadius() + 0.99f * laser.getRadius();
        laser.setPosition(new FloatPair(newX, pos.y()));
        hitDetection.checkCollisions();
        verify(model, times(2)).handleCollision(any(), any());

        // make a tiny gap between laser and asteroid
        laser.setPosition(new FloatPair(newX + 0.011f * laser.getRadius(), pos.y()));
        verify(model, times(2)).handleCollision(any(), any());
    }

    @Test
    public void shipCollisionTest() {
        // ensure no collisions initially
        hitDetection.checkCollisions();
        verify(model, never()).handleCollision(any(), any());

        // make player and enemy overlap
        FloatPair pos = new FloatPair(1000f, 1000f);
        player.setPosition(pos);
        enemyShip.setPosition(pos);

        hitDetection.checkCollisions();
        verify(model, times(1)).handleCollision(any(), any());

        // both ships should be 1 wide, 2 high
        // make ships barely overlap
        FloatPair newPos = new FloatPair(pos.x() + 1.99f * PhysicsParameters.fuselageRadius, pos.y());
        player.setPosition(newPos);

        hitDetection.checkCollisions();
        verify(model, times(2)).handleCollision(any(), any());

        // make a tiny gap between the ships
        FloatPair nonOverlapPos = new FloatPair(newPos.x() + 0.011f * PhysicsParameters.fuselageRadius, newPos.y());
        player.setPosition(nonOverlapPos);
        hitDetection.checkCollisions();
        verify(model, times(2)).handleCollision(any(), any());

        // rotating the player should make it overlap with enemy again
        player.rotate(90f);
        hitDetection.checkCollisions();
        verify(model, times(3)).handleCollision(any(), any());
    }
}
