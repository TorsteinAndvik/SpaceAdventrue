package inf112.skeleton.model;

import inf112.skeleton.controller.ControllableSpaceGameModel;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.grid.IGridDimension;
import inf112.skeleton.view.ViewableSpaceGameModel;

public class SpaceGameModel implements ViewableSpaceGameModel, ControllableSpaceGameModel {

  @Override
  public boolean moveSpaceShip(int deltaRow, int deltaCol) {
    return false;
  }

  @Override
  public void gameStateActive() {

  }

  @Override
  public void gameStatePaused() {

  }

  @Override
  public void stopGame() {

  }

  @Override
  public void startNewGame() {

  }

  @Override
  public void stopMoving() {

  }

  @Override
  public void moveUp() {

  }

  @Override
  public void moveDown() {

  }

  @Override
  public void moveLeft() {

  }

  @Override
  public void moveRight() {

  }

  @Override
  public GameState getGameState() {
    return null;
  }

  @Override
  public IGridDimension getDimension() {
    return null;
  }

  @Override
  public Iterable<GridCell<Character>> getPixels() {
    return null;
  }

  @Override
  public Iterable<GridCell<Character>> getPixelsInSpaceBody() {
    return null;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public int getProgression() {
    return 0;
  }

}
