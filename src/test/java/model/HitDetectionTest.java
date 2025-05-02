package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Globals.Collidable;
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

        asteroid = new Asteroid();
        asteroid.setPosition(new FloatPair(-5f, -5f));
        laser = new Bullet("bullet", "a bullet", 3, 3, 0, 0, 0.25f, false);
        player = new Player(ShipFactory.playerShip(), "player", "the player's ship", 8, 8);
        enemyShip = new EnemyShip(ShipFactory.simpleShip(), "dreadnought", "an enemy ship",
                0, 5, 0);

        hitDetection = new HitDetection(model);
        hitDetection.addCollider(asteroid);
        hitDetection.addCollider(player);
        hitDetection.addCollider(laser);
        hitDetection.addCollider(enemyShip);
    }

    @Test
    public void addColliderTest() {
        assertTrue(hitDetection.colliders.contains(asteroid));
        assertTrue(hitDetection.colliders.contains(laser));
        assertTrue(hitDetection.colliders.contains(player));
        assertTrue(hitDetection.colliders.contains(enemyShip));

        List<Collidable> colliders = new ArrayList<>();
        Asteroid newAsteroid1 = new Asteroid();
        Asteroid newAsteroid2 = new Asteroid();
        colliders.add(newAsteroid1);
        colliders.add(newAsteroid2);
        hitDetection.addColliders(colliders);
        assertTrue(hitDetection.colliders.contains(newAsteroid1));
        assertTrue(hitDetection.colliders.contains(newAsteroid2));

        // check other collideables are still present
        assertTrue(hitDetection.colliders.contains(asteroid));
        assertTrue(hitDetection.colliders.contains(laser));
        assertTrue(hitDetection.colliders.contains(player));
        assertTrue(hitDetection.colliders.contains(enemyShip));
    }

    @Test
    public void removeColliderTest() {
        assertTrue(hitDetection.colliders.contains(asteroid));
        hitDetection.removeCollider(asteroid);
        assertFalse(hitDetection.colliders.contains(asteroid));

        // check other collideables aren't removed
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

        FloatPair pos1 = new FloatPair(1000f, 1000f);
        FloatPair pos2 = new FloatPair(2000f, 2000f);
        player.setPosition(pos1);
        laser.setPosition(pos1);

        hitDetection.checkCollisions();
        verify(model, times(1)).handleCollision(any(), any());

        laser.setPosition(pos2);
        hitDetection.checkCollisions();
        verify(model, times(1)).handleCollision(any(), any());

        enemyShip.setPosition(pos2);
        hitDetection.checkCollisions();
        verify(model, times(2)).handleCollision(any(), any());
    }

    @Test
    public void doubleShipCollisionTest() {
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

    @Test
    public void isFriendlyFireTest() {
        assertFalse(HitDetection.isFriendlyFire(player, asteroid));
        assertFalse(HitDetection.isFriendlyFire(asteroid, asteroid));
        assertFalse(HitDetection.isFriendlyFire(laser, asteroid));
        assertFalse(HitDetection.isFriendlyFire(asteroid, laser));
        assertFalse(HitDetection.isFriendlyFire(asteroid, player));

        laser.setSourceID("");

        assertFalse(HitDetection.isFriendlyFire(laser, player));
        assertFalse(HitDetection.isFriendlyFire(laser, enemyShip));
        assertFalse(HitDetection.isFriendlyFire(player, laser));
        assertFalse(HitDetection.isFriendlyFire(enemyShip, laser));

        laser.setSourceID(enemyShip.getID());
        assertFalse(HitDetection.isFriendlyFire(laser, player));
        assertTrue(HitDetection.isFriendlyFire(laser, enemyShip));
        assertFalse(HitDetection.isFriendlyFire(player, laser));
        assertTrue(HitDetection.isFriendlyFire(enemyShip, laser));
        assertTrue(HitDetection.isFriendlyFire(laser, laser));
    }
}
