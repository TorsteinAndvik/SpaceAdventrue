package inf112.skeleton.view;

import inf112.skeleton.grid.GridCell;
import inf112.skeleton.grid.IGridDimension;
import inf112.skeleton.model.GameState;

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
   * @return An iterable that contains all pixels on the board, their position and symbol.
   */
  Iterable<GridCell<Character>> getPixels();

  /**
   * Creates an iterable of the pixels in a SpaceBody
   *
   * @return An iterable that contains the pixels of a SpaceBody, their positions and symbols.
   */
  Iterable<GridCell<Character>> getPixelsInSpaceBody();

  /**
   * Returns the current game score.
   *
   * @return the game score.
   */
  int getScore();

  /**
   * Returns an integer representation of the game progression @TODO are we changing this?
   *
   * @return an integer representation of the game score.
   */
  int getProgression();
}
