package inf112.skeleton.view;

import inf112.skeleton.grid.GridCell;
import inf112.skeleton.grid.IGridDimension;
import inf112.skeleton.model.GameState;
import inf112.skeleton.model.SpaceCharacters.Asteroid;
import inf112.skeleton.model.SpaceCharacters.Bullet;
import inf112.skeleton.model.SpaceCharacters.EnemyShip;
import inf112.skeleton.model.SpaceCharacters.Player;

public interface ViewableSpaceGameModel {

    /**
     * Gets the current game state. There are @TODO options?
     *
     * @return The current game state
     */
    GameState getGameState();

    /**
     * Gets the dimensions of the grid for use by the SpaceGameView
     *
     * @return the dimensions of the game
     */
    IGridDimension getDimension();

    /**
     * Creates an iterable of the pixels in the game board.
     *
     * @return An iterable that contains all pixels on the board, their position and
     *         symbol.
     */
    Iterable<GridCell<Character>> getPixels();

    /**
     * Creates an iterable of the pixels in a SpaceBody
     *
     * @return An iterable that contains the pixels of a SpaceBody, their positions
     *         and symbols.
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
     * Returns the Player object of the MVP
     * TODO: Remove this once a proper model is in place.
     * TODO: There should be another "viewable" interface for model-objects, such
     * that the view doesn't get access to the model
     * 
     * @return the Player object
     */
    Player getPlayer();

    /**
     * Returns the Asteroid object of the MVP //TODO: Remove this once a proper
     * model is in place
     * 
     * @return the Asteroid object
     */
    Asteroid getAsteroid();

    /**
     * Returns the enemy ship of the MVP //TODO: Remove this once a proper model is
     * in place
     * 
     * @return the EnemyShip object
     */
    EnemyShip getEnemyShip();

    /**
     * Returns the Bullet object (laser) of the MVP //TODO: Remove this once a
     * proper model is in place
     * 
     * @return the Bullet object
     */
    Bullet getLaser();
}
