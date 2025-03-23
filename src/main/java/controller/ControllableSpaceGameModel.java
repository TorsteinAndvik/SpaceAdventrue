package controller;


public interface ControllableSpaceGameModel {

    /**
     * Updates the game model by the given delta time in seconds.
     *
     * @param delta delta time in seconds
     */
    void update(float delta);


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

    /**
     * Stop a spaceship from moving. Related to releasing a key.
     */
    void stopMoving();

    /**
     * Move the spaceship up.
     */
    void moveUp();

    /**
     * Move the spaceship down.
     */
    void moveDown();

    /**
     * Move the spaceship left.
     */
    void moveLeft();

    /**
     * Move the spaceship right.
     */
    void moveRight();
}
