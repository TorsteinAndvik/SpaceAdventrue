package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.SpaceCharacters.Asteroid;
import model.utils.SpaceCalculator;

public class DirectionalAsteroidFactory extends AsteroidFactory {

    private final Random rng = new Random();

    /**
     * Generates a single asteroid at a random position at a certain angle from the
     * player
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

        int asteroidNumber = rng.nextInt(4, 8);
        List<Asteroid> showerList = new ArrayList<>();
        while (showerList.size() < asteroidNumber) {
            Asteroid potential = spawnDirectionalAsteroid();
            List<Asteroid> collisionCheck = new ArrayList<>();
            for (Asteroid asteroid : showerList) {
                if (SpaceCalculator.collisionCalculator(potential, asteroid)) {
                    collisionCheck.add(asteroid);
                }
            }
            if (collisionCheck.size() == 0) {
                showerList.add(potential);
            }
        }
        return showerList;

    }
}