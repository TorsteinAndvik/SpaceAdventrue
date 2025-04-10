package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.SpaceCharacters.Asteroid;
import model.utils.SpaceCalculator;

public class DirectionalAsteroidFactory extends AsteroidFactory {

    private Random rng = new Random();

    /**
     * Generates a single asteroid at a psuedo-random position at the spawn
     * perimeter.
     * 
     * @return A randomly spawned Asteroid object.
     */
    public Asteroid getAsteroid() {
        return spawnDirectionalAsteroid();
    }

    /**
     * Generates a shower of multiple asteroids at random positions at a
     * certain angle from the player
     *
     * @return A list of randomly spawned Asteroid objects with unique positions.
     */

    public List<Asteroid> getAsteroidShower() {

        int asteroidNumber = rng.nextInt(4) + 1; // [1, 8]
        List<Asteroid> showerList = new ArrayList<Asteroid>();
        while (showerList.size() < asteroidNumber) {
            Asteroid potential = spawnDirectionalAsteroid();
            List<Asteroid> collisionCheck = new ArrayList<Asteroid>();
            for (int i = 0; i < showerList.size(); i++) {
                if (SpaceCalculator.collisionCalculator(potential, showerList.get(i))) {
                    collisionCheck.add(showerList.get(i));
                }
            }
            if (collisionCheck.size() == 0) {
                showerList.add(potential);
            }
        }
        return showerList;

    }
}