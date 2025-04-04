package model;

import java.util.ArrayList;
import java.util.List;

import model.SpaceCharacters.Asteroid;

public class DirectionalAsteroidFactory extends AsteroidFactory {

    private float direction = (float) 0.15;

    /**
     * Generates a single asteroid at a psuedo-random position at the spawn
     * perimeter.
     * 
     * @return A randomly spawned Asteroid object.
     */
    public Asteroid getAsteroid() {
        return spawnAsteroid();
    }

    /**
     * Generates a shower of multiple asteroids at psuedo-random positions at the
     * spawn perimeter.
     *
     * @return A list of randomly spawned Asteroid objects with unique positions.
     */
    /*
     * //TODO: Needs refactor, see method in RandomAsteroidFactory, and consider
     * spawnLocation refactor
     * public List<Asteroid> getAsteroidShower() {
     * float asteroidNumber = (int) (Math.random() * (4 - 1)) + 1;
     * List<Asteroid> showerList = new ArrayList<Asteroid>();
     * List<Float> rngList = new ArrayList<Float>();
     * 
     * while (showerList.size() < asteroidNumber) {
     * float circleRng = ((float) ((int) Math.random() * 2 * (direction * 100) -
     * direction * 100)) / 100;
     * if (!rngList.contains((float) Math.round(circleRng * 10))) {
     * rngList.add((float) Math.round(circleRng * 10));
     * Asteroid newAsteroid = spawnAsteroid(circleRng);
     * showerList.add(newAsteroid);
     * }
     * }
     * return showerList;
     * 
     * }
     */

    /**
     * Sets the directional constraint used for spawning asteroids.
     *
     * @param direction a float in the <code>[0,0.5)</code> range that contrains the
     *                  available angle. Here 0 represents straight infront and 0.5
     *                  is the whole 360 degrees.
     */
    public void setDirection(float direction) {
        this.direction = Math.min(direction, 0.5f);

    }

    public float getDirection() {
        return this.direction;
    }
}
