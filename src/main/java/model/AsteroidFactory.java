package model;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.SpaceBody;
import model.SpaceCharacters.SpaceShip;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;

public abstract class AsteroidFactory {

    private Random rng = new Random();

    private float bufferRadius;
    private SpaceShip player;
    private final Pool<Asteroid> asteroidPool = new Pool<Asteroid>() {
        @Override
        protected Asteroid newObject() {
            return new Asteroid();
        }
    };

    /**
     * Spawns an asteroid at a given position and sets its velocity to intercept the
     * player.
     *
     * @param x The x-coordinate where the asteroid should spawn.
     * @param y The y-coordinate where the asteroid should spawn.
     * @return A new Asteroid object with randomized size, rotation, and velocity
     *         aimed at the player.
     */
    public Asteroid spawnAsteroidFromPos(float x, float y) {
        int sizeRng = rng.nextInt(4) + 1; // [1, 4]
        float interceptTimeRng = rng.nextFloat(5, 20); // [5, 20)
        float rotationRng = rng.nextFloat(0, 5); // [0, 5)

        Vector2 interceptVelocity = interceptFromPosition(interceptTimeRng, x, y, this.player);

        return createAsteroid(x, y, interceptVelocity, sizeRng, rotationRng);
    }

    /**
     * Spawns an asteroid at a random position based on an angle and sets its
     * velocity to intercept the player.
     *
     * @param spawnRng a number between 0 and 1 that determines the angle the
     *                 asteroid is spawned at relative to the player.
     * @return A new Asteroid object with randomized size, rotation, and velocity
     *         aimed at the player.
     */
    public Asteroid spawnAsteroidFromAngle(float spawnRng) {
        int sizeRng = rng.nextInt(4) + 1; // [1, 4]
        float interceptTimeRng = rng.nextFloat(10, 20); // [10, 20)
        float rotationRng = rng.nextFloat(-10, 10); // [-10, 10)

        FloatPair spawnPos = spawnLocation(spawnRng);
        Vector2 interceptVelocity = interceptFromPosition((Math.min(interceptTimeRng * Math.max(sizeRng, 1f), 30)),
                spawnPos.x(), spawnPos.y(), this.player);

        return createAsteroid(spawnPos.x(), spawnPos.y(), interceptVelocity, sizeRng, rotationRng);
    }

    private Asteroid createAsteroid(float x, float y, Vector2 velocity, int size, float rotationSpeed) {
        boolean isLarge = false;
        float radius = 0.5f;
        if (size == 4) {
            radius = 1f;
            isLarge = true;
        }

        int hitPoints = 2 * size;
        float mass = 2f * size;

        Asteroid spawn = asteroidPool.obtain();
        spawn.init(x, y, velocity.x, velocity.y, hitPoints, mass, 0, radius, rotationSpeed, isLarge);
        return spawn;
    }

    public void setShip(SpaceShip player) {
        this.player = player;
    }

    /**
     * Calculates the velocity vector required for an object at the given position
     * to reach an interceptee in a given time.
     *
     * @param deltaTime   The time in which the interception should occur.
     * @param x           The x coordinate of the object that is attempting to
     *                    intercept.
     * @param y           The y coordinate of the object that is attempting to
     *                    intercept.
     * @param interceptee The <code>SpaceBody</code> object being intercepted.
     * 
     * @return a <code>Vector2</code> velocity vector.
     */
    private Vector2 interceptFromPosition(float deltaTime, float x, float y, SpaceBody interceptee) {

        float targetX, targetY;
        if (interceptee instanceof SpaceShip) {
            targetX = ((SpaceShip) interceptee).getAbsoluteCenterOfMass().x()
                    + interceptee.getVelocity().x * deltaTime;

            targetY = ((SpaceShip) interceptee).getAbsoluteCenterOfMass().y()
                    + interceptee.getVelocity().y * deltaTime;
        } else {
            targetX = interceptee.getX() + interceptee.getVelocity().x * deltaTime;
            targetY = interceptee.getY() + interceptee.getVelocity().y * deltaTime;
        }

        float distance = SpaceCalculator.distance(targetX - x, targetY - y);
        float speed = distance / (2f * deltaTime); // TODO: Why does dividing by 2 fix collision course?

        float angle = (float) Math.toDegrees(Math.atan2(targetY - y, targetX - x));

        Vector2 velocity = SpaceCalculator.velocityFromAngleSpeed(angle, speed);
        return velocity;

    }

    /**
     * Calculates a spawn location for an asteroid along a circular buffer around
     * the player.
     *
     * @param spawnRNG A random value between 0 and 1 used to determine the position
     *                 on the circle.
     * @return A <code>FloatPair</code> describing the spawn location.
     */
    private FloatPair spawnLocation(float spawnRNG) {
        float angle = (float) Math.PI * (2f * spawnRNG + 0.5f);

        float spawnX = this.player.getAbsoluteCenterOfMass().x()
                + this.bufferRadius * (float) Math.cos(angle);

        float spawnY = this.player.getAbsoluteCenterOfMass().y()
                + this.bufferRadius * (float) Math.sin(angle);

        return new FloatPair(spawnX, spawnY);

    }

    public void setBufferRadius(Rectangle bounds) {
        float diagonal = (float) Math
                .sqrt(bounds.getHeight() * bounds.getHeight() + bounds.getWidth() * bounds.getWidth());
        this.bufferRadius = diagonal / 2f;
    }

    public void free(Asteroid asteroid) {
        asteroidPool.free(asteroid);
    }

    public void fill(int asteroidPreFill) {
        asteroidPool.fill(asteroidPreFill);
    }
}
