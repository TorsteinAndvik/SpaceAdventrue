package view;

import java.util.List;

import grid.GridCell;
import grid.IGridDimension;
import model.ScreenBoundsProvider;
import model.Animation.AnimationCallback;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.SpaceShip;

public interface ViewableSpaceGameModel {

    /**
     * Gets the dimensions of the grid for use by the SpaceGameView.
     *
     * @return the dimensions of the game.
     */
    IGridDimension getDimension();

    /**
     * Creates an iterable of the pixels in the game board.
     *
     * @return An iterable that contains all pixels on the board, their position and
     * symbol.
     */
    Iterable<GridCell<Character>> getPixels();

    /**
     * Creates an iterable of the pixels in a SpaceBody.
     *
     * @return An iterable that contains the pixels of a SpaceBody, their positions
     * and symbols.
     */
    Iterable<GridCell<Character>> getPixelsInSpaceBody();

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
     * the model.
     */

    List<SpaceShip> getSpaceShips();

    /**
     * @return the player's <code>SpaceShip</code> object.
     */
    SpaceShip getPlayer();

    /**
     * Returns all Asteroid objects of the MVP //TODO: Remove this once a proper
     * model is in place.
     *
     * @return a List of all Asteroid objects.
     */
    List<Asteroid> getAsteroids();

    /**
     * Return all lasers in the model.
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
