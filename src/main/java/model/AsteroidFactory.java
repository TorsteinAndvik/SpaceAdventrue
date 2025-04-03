package model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.SpaceBody;
import model.SpaceCharacters.SpaceShip;

public abstract class AsteroidFactory {

    private float bufferRadius = 20;
    private SpaceShip player;
    private Pool<Asteroid> asteroidPool;

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
        int sizeRng = (int) (Math.random() * (3 - 1)) + 1;
        float interceptTimeRng = (int) (Math.random() * (20 - 5)) + 5;
        float rotationRng = (int) (Math.random() * (5 - 0)) + 0;
        List<Float> interceptVelocity = interceptFromPosition(interceptTimeRng, x, y, this.player);
        boolean size = false;
        float radius = 0.5f;
        if (sizeRng == 4) {
            radius = 1f;
            size = true;
        }
        Asteroid spawn = asteroidPool.obtain();
        spawn.init(x, y, interceptVelocity.get(0), interceptVelocity.get(1),
                2 * sizeRng, 2 * sizeRng, 0, radius, 10 * rotationRng, size);
        return spawn;
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
        int sizeRng = (int) (Math.random() * (4 - 1)) + 1;
        float interceptTimeRng = (int) (Math.random() * (20 - 10)) + 10;
        float rotationRng = (int) (Math.random() * (20)) - 10;
        List<Float> spawnPos = spawnLocation(spawnRng);
        List<Float> interceptVelocity = interceptFromPosition((Math.min(interceptTimeRng * Math.min(sizeRng, 1f), 30)),
                spawnPos.get(0), spawnPos.get(1),
                this.player);
        boolean size = false;
        float radius = 0.5f;
        if (sizeRng == 4) {
            radius = 1f;
            size = true;
        }
        Asteroid spawn = asteroidPool.obtain();
        spawn.init(spawnPos.get(0), spawnPos.get(1), interceptVelocity.get(0), interceptVelocity.get(1),
                sizeRng * ((int) interceptTimeRng / 10), sizeRng, 0, radius, 10 * rotationRng, size);
        return spawn;

    }

    public void setShip(SpaceShip player) {
        this.player = player;
    }

    /**
     * Calculates the velocity vector required for an interceptor object to reach an
     * interceptee in a given time.
     *
     * @param deltaTime   The time in which the interception should occur.
     * @param interceptor The object that is attempting to intercept.
     * @param interceptee The object being intercepted.
     * @param <T>         A type extending <code>SpaceBody</code>, representing the
     *                    interceptor.
     * @param <V>         A type extending <code>SpaceBody</code>, representing the
     *                    interceptee.
     * @return A list containing the X and Y components of the required velocity
     *         vector.
     */
    private <T extends SpaceBody, V extends SpaceBody> List<Float> interceptFromObject(float deltaTime, T interceptor,
            V interceptee) {
        float velocityX = ((interceptee.getX() - interceptor.getX()) / deltaTime) + interceptee.getVelocity().x;
        float velocityY = ((interceptee.getY() - interceptor.getX()) / deltaTime) + interceptee.getVelocity().y;
        List<Float> velocity = new ArrayList<>();
        velocity.add(velocityX);
        velocity.add(velocityY);
        return velocity;

    }

    /**
     * Calculates the velocity vector required for an object at the given position
     * to reach an interceptee in a given time.
     *
     * @param deltaTime   The time in which the interception should occur.
     * @param x           The x coordinate of the object that is attempting to
     *                    intercept.
     * @param x           The y coordinate of the object that is attempting to
     *                    intercept.
     * @param interceptee The object being intercepted.
     * @param <V>         A type extending <code>SpaceBody</code>, representing the
     *                    interceptee.
     * @return A list containing the X and Y components of the required velocity
     *         vector.
     */
    private <V extends SpaceBody> List<Float> interceptFromPosition(float deltaTime, float x, float y, V interceptee) {
        float velocityX = ((interceptee.getX() + (((int) Math.random() * 2 * (10) - 10) / 10) - x) / deltaTime)
                + interceptee.getVelocity().x;
        float velocityY = ((interceptee.getY() + (((int) Math.random() * 2 * (10) - 10) / 10) - y) / deltaTime)
                + interceptee.getVelocity().y;
        List<Float> velocity = new ArrayList<>();
        velocity.add(velocityX);
        velocity.add(velocityY);
        return velocity;
    }

    /**
     * Calculates a spawn location for an asteroid along a circular buffer around
     * the player.
     *
     * @param spawnRNG A random value between 0 and 1 used to determine the position
     *                 on the circle.
     * @return A list containing the x and y coordinates of the spawn location.
     */
    private List<Float> spawnLocation(float spawnRNG) {
        float spawnX = this.player.getX() + this.bufferRadius * (float) Math.cos(2 * Math.PI * spawnRNG + Math.PI / 2);
        float spawnY = this.player.getY() + this.bufferRadius * (float) Math.sin(2 * Math.PI * spawnRNG + Math.PI / 2);
        List<Float> pos = new ArrayList<>();
        pos.add(spawnX);
        pos.add(spawnY);
        return pos;

    }

    public void setBufferRadius(Rectangle bounds) {
        float diagonal = bounds.getHeight() * bounds.getHeight() + bounds.getWidth() * bounds.getWidth();
        this.bufferRadius = (float) (Math.sqrt(diagonal) / 0.8);
    }

    public void setPool(Pool<Asteroid> asteroidPool) {
        this.asteroidPool = asteroidPool;
    }

}
