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

    private final int largeSize = 4;

    private Rectangle spawnPerimeter;
    private SpaceShip player;
    private final Pool<Asteroid> asteroidPool = new Pool<Asteroid>() {
        @Override
        protected Asteroid newObject() {
            return new Asteroid();
        }
    };

    /**
     * Spawns an asteroid at a random position on the spawn perimeter.
     *
     * @return A new Asteroid object with randomized size, rotation, and velocity
     *         aimed at the player.
     */
    public Asteroid spawnAsteroid() {
        int sizeRng = rng.nextInt(largeSize) + 1; // [1, largeSize]
        float interceptTimeRng = rng.nextFloat(10, 20); // [10, 20)
        float rotationRng = rng.nextFloat(-10, 10); // [-10, 10)

        float radius = getRadius(sizeRng);

        FloatPair spawnPos = spawnLocation(radius);
        Vector2 interceptVelocity = interceptFromPosition(interceptTimeRng, spawnPos.x(), spawnPos.y(), this.player);

        return createAsteroid(spawnPos.x(), spawnPos.y(), interceptVelocity, sizeRng, radius, rotationRng);
    }

    private float getRadius(int size) {
        return size == largeSize ? 1f : 0.5f;
    }

    private boolean getLarge(int size) {
        return size == largeSize;
    }

    private Asteroid createAsteroid(float x, float y, Vector2 velocity, int size, float radius, float rotationSpeed) {
        boolean isLarge = getLarge(size);

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
    private FloatPair spawnLocation(float radius) {
        int side = rng.nextInt(4);

        float spawnX, spawnY;
        if (side == 0) { // Bot
            spawnY = spawnPerimeter.y - radius;
            spawnX = rng.nextFloat(spawnPerimeter.x, spawnPerimeter.x + spawnPerimeter.width);
        } else if (side == 1) { // Right
            spawnX = spawnPerimeter.x + spawnPerimeter.width + radius;
            spawnY = rng.nextFloat(spawnPerimeter.y, spawnPerimeter.y + spawnPerimeter.height);
        } else if (side == 2) { // Top
            spawnY = spawnPerimeter.y + spawnPerimeter.height + radius;
            spawnX = rng.nextFloat(spawnPerimeter.x, spawnPerimeter.x + spawnPerimeter.width);
        } else { // Left
            spawnX = spawnPerimeter.x - radius;
            spawnY = rng.nextFloat(spawnPerimeter.y, spawnPerimeter.y + spawnPerimeter.height);
        }

        return new FloatPair(spawnX, spawnY);
    }

    public void setSpawnPerimeter(Rectangle spawnPerimeter) {
        this.spawnPerimeter = spawnPerimeter;
    }

    public void free(Asteroid asteroid) {
        asteroidPool.free(asteroid);
    }

    public void fill(int asteroidPreFill) {
        asteroidPool.fill(asteroidPreFill);
    }
}
