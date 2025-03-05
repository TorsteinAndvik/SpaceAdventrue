package inf112.skeleton.controller;

import com.badlogic.gdx.Input;
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
  protected boolean handleKeyDown(int keycode) {
    return switch (keycode) {
      case Input.Keys.W -> {
        model.moveUp();
        yield true;
      }
      case Input.Keys.S -> {
        model.moveDown();
        yield true;
      }
      case Input.Keys.A -> {
        model.moveLeft();
        yield true;
      }
      case Input.Keys.D -> {
        model.moveRight();
        yield true;
      }
      default -> false;
    };
  }

  @Override
  protected boolean handleKeyUp(int keycode) {
    switch (keycode) {
      case Input.Keys.W:
      case Input.Keys.S:
      case Input.Keys.A:
      case Input.Keys.D:
        model.stopMoving();
        return true;
      default:
        return false;
    }
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
