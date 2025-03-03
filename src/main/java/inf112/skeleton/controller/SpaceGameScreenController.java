package inf112.skeleton.controller;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.SpaceGameModel;
import inf112.skeleton.view.SpaceScreen;

public class SpaceGameScreenController extends GenericController {

  private final SpaceGameModel model;
  private final SpaceScreen view;
  private final Vector2 touchPos;

  public SpaceGameScreenController(SpaceScreen view, SpaceGameModel model) {
    this.model = model;
    this.view = view;
    this.touchPos = new Vector2();
  }

  @Override
  protected boolean leftClick(int screenX, int screenY) {
    return false;
  }

  @Override
  protected boolean rightClick(int screenX, int screenY) {
    return false;
  }

  @Override
  protected boolean middleClick() {
    return false;
  }

  @Override
  protected boolean leftClickDragged(int screenX, int screenY) {
    return false;
  }

  @Override
  protected boolean rightClickDragged(int screenX, int screenY) {
    return false;
  }

  @Override
  protected boolean leftClickRelease() {
    return false;
  }

  @Override
  protected boolean rightClickRelease() {
    return false;
  }

  @Override
  protected void handleScroll(float amountY) {

  }
}
