package view;

import java.util.List;

import model.ScreenBoundsProvider;
import model.Animation.AnimationCallback;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.SpaceShip;

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
    SpaceShip getPlayer();

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
     * Sets the <code>AnimationCallback</code> for the model.
     *
     * @param animationCallback an AnimationCallback object.
     */
    void setAnimationCallback(AnimationCallback animationCallback);

    /**
     * Sets the <code>ScreenBoundsProvider</code> for the model.
     *
     * @param screenBoundsProvider a ScreenBoundsProvider object.
     */
    void setScreenBoundsProvider(ScreenBoundsProvider screenBoundsProvider);
}
