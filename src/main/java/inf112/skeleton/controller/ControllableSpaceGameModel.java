package inf112.skeleton.controller;

import inf112.skeleton.model.GameState;

public interface ControllableSpaceGameModel {

  /**
   * Gets the current game state. @TODO are we doing game states?
   *
   * @return The current game state
   */
  GameState getGameState();

  /**
   * Moves the spaceship by a change in x and a change in y on the game board
   *
   * @param deltaRow change in y.
   * @param deltaCol change in x
   * @return True if move occurred, else False
   */
  boolean moveSpaceShip(int deltaRow, int deltaCol);

  /**
   * Starts the game. Sets GameState to ACTIVE_GAME.
   */
  void gameStateActive();

  /**
   * Pauses the game. Sets GameState to PAUSE_GAME.
   */
  void gameStatePaused();

  /**
   * Stops the game. Sets GameState to STOPPED_GAME.
   */
  void stopGame();

  /**
   * Starts the game over. Clears the game board and resets every item in the game.
   */
  void startNewGame();

}
