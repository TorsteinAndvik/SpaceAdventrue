package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.SpaceCharacters.Asteroid;

public class RandomAsteroidFactory extends AsteroidFactory {

    private final Random rng = new Random();

    /**
     * Generates a shower of multiple asteroids at random positions in a set radius
     * from the player.
     *
     * @return A list of randomly spawned Asteroid objects with unique positions.
     */

    public List<Asteroid> getAsteroidShower() {

        int asteroidNumber = rng.nextInt(4, 8);
        List<Asteroid> showerList = new ArrayList<>();

        for (int i = 0; i < asteroidNumber; i++) {
            showerList.add(spawnAsteroid());
        }

        return showerList;
    }
}
