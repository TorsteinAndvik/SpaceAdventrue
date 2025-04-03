package model;

import java.util.ArrayList;
import java.util.List;

import model.SpaceCharacters.Asteroid;

public class DirectionalAsteriodFactory extends AsteroidFactory {

    private float direction = (float) 0.15;

    /**
     * Generates a single asteroid at a seni-random position, constrained by the
     * direction, along at a set radius from the player.
     * 
     * @return A randomly spawned Asteroid object.
     */
    public Asteroid getAsteroid() {
        float circleRng = (float) ((Math.random() * 2 * (direction)) - direction);
        Asteroid spawn = spawnAsteroidFromAngle(circleRng);
        return spawn;
    }

    /**
     * Generates a shower of multiple asteroids at semi-random positions,
     * constrained by direction, in a set radius from the player.
     *
     * @return A list of randomly spawned Asteroid objects with unique positions.
     */

    public List<Asteroid> getAsteroidShower() {
        float asteroidNumber = (int) (Math.random() * (4 - 1)) + 1;
        List<Asteroid> showerList = new ArrayList<Asteroid>();
        List<Float> rngList = new ArrayList<Float>();
        while (showerList.size() < asteroidNumber) {
            float circleRng = (float) ((Math.random() * 2 * (direction)) - direction);
            if (!rngList.contains((float) Math.round(circleRng * 10))) {
                rngList.add((float) Math.round(circleRng * 10));
                Asteroid newAsteroid = spawnAsteroidFromAngle(circleRng);
                showerList.add(newAsteroid);
            }
        }
        return showerList;

    }

    /**
     * Sets the directional constraint used for spawning asteroids.
     *
     * @param direction a float in the <code>[0,0.5)</code> range that contrains the
     *                  available angle. Here 0 represents straight infront and 0.5
     *                  is the whole 360 degrees.
     */
    public void setDirection(float direction) {
        this.direction = (float) Math.min(direction, 0.5);

    }

    public float getDirection() {
        return this.direction;
    }

}
