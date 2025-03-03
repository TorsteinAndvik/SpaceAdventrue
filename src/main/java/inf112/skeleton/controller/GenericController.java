package inf112.skeleton.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

abstract class GenericController extends InputAdapter {
  protected final Vector2 touchPos;
  protected boolean leftClickLocked;
  protected boolean rightClickLocked;

  public GenericController() {
    this.touchPos = new Vector2();
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    handleScroll(amountY);
    return true;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return switch (button) {
      case 0 -> leftClick(screenX, screenY);
      case 1 -> rightClick(screenX, screenY);
      case 2 -> middleClick();
      default -> false;
    };
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (button == 0) {
      return leftClickRelease();
    } else if (button == 1) {
      return rightClickRelease();
    }
    return false;
  }


  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (isLeftClickLocked()) {
      return rightClickDragged(screenX, screenY);
    } else if (isRightClickLocked()) {
      return leftClickDragged(screenX, screenY);
    }
    return true;
  }

  protected boolean isLeftClickLocked() {
    return leftClickLocked;
  }

  protected boolean isRightClickLocked() {
    return rightClickLocked;
  }

  protected void setLeftClickLocked(boolean locked) {
    this.leftClickLocked = locked;
  }

  protected void setRightClickLocked(boolean locked) {
    this.rightClickLocked = locked;
  }


  protected abstract boolean leftClick(int screenX, int screenY);
  protected abstract boolean rightClick(int screenX, int screenY);
  protected abstract boolean middleClick();
  protected abstract boolean leftClickDragged(int screenX, int screenY);
  protected abstract boolean rightClickDragged(int screenX, int screenY);
  protected abstract boolean leftClickRelease();
  protected abstract boolean rightClickRelease();
  protected abstract void handleScroll(float amountY);

}
