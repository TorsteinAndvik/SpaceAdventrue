package model;


import java.util.ArrayList;
import java.util.List;

import model.SpaceCharacters.Asteroid;


public class RandomAsteroidFactory extends AsteroidFactory{


    /**
     * Generates a single asteroid at a random position along at a set radius from the player.
     *
     * @return A randomly spawned Asteroid object.
     */
    public Asteroid getAsteroid(){
        float circleRng = (float)(Math.random());
        Asteroid spawn = spawnAsteroidFromAngle(circleRng);
        return spawn;
    }

    /**
     * Generates a shower of multiple asteroids at random positions in a set radius from the player.
     *
     * @return A list of randomly spawned Asteroid objects with unique positions.
     */

    public List<Asteroid> getAsteroidShower(){
        float asteroidNumber = (int) (Math.random() * (8 - 1)) + 1;
        List<Asteroid> showerList = new ArrayList<Asteroid>();
        List<Float> rngList = new ArrayList<Float>();
        while (showerList.size()<asteroidNumber){
            float circleRng = (float)(Math.random());
            if (!rngList.contains((float)Math.round(circleRng*10))){
                rngList.add((float)Math.round(circleRng*10));
                showerList.add(spawnAsteroidFromAngle(circleRng));
            }
        }
        return showerList;

    }




}
