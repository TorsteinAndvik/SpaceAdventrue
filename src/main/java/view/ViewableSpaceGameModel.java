package view;

import java.io.InvalidClassException;
import java.util.List;

import model.Globals.Collectable;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.Ships.SpaceShip;
import model.SpaceCharacters.Ships.ViewablePlayer;

public interface ViewableSpaceGameModel {

    /**
     * Returns the current game score.
     *
     * @return the game score.
     */
    int getScore();

    /**
     * Returns an integer representation of the game progression @TODO are we
     * changing this?
     *
     * @return an integer representation of the game score.
     */
    int getProgression();

    /**
     * @return a <code>List</code> containing all <code>SpaceShip</code> objects in
     *         the model.
     */

    List<SpaceShip> getSpaceShips();

    /**
     * @return the player's <code>SpaceShip</code> object.
     */
    ViewablePlayer getPlayer() throws InvalidClassException;

    /**
     * Returns all <code>Asteroid</code> in the model
     *
     * @return a List of all Asteroid objects.
     */
    List<Asteroid> getAsteroids();

    /**
     * Return all <code>Bullet</code> objects in the model.
     *
     * @return a <code>List</code> of <code>Bullet</code> objects
     */
    List<Bullet> getLasers();

    /**
     * Return all <code>Collectable</code> objects in the model.
     *
     * @return a <code>List</code> of <code>Collectable</code> objects
     */
    List<Collectable> getCollectables();

}
