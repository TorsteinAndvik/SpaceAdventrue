package inf112.skeleton.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.UpgradeScreenModel;
import inf112.skeleton.view.UpgradeScreen;
import inf112.skeleton.grid.CellPosition;

public class UpgradeScreenController extends InputAdapter {
  private final UpgradeScreenModel model;
  private final UpgradeScreen view;
  private final Vector2 touchPos;

  public UpgradeScreenController(UpgradeScreen view, UpgradeScreenModel model) {
    this.view = view;
    this.model = model;
    this.touchPos = new Vector2();
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    model.updateCameraZoom(amountY);
    view.updateCameraZoom(model.getCurrentZoom());
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

  private boolean leftClick(int screenX, int screenY) {
    if (model.isLeftClickLocked()) return true;

    model.setRightClickLocked(true);
    model.setReleaseGrabbedUpgrade(false);
    model.updateMouseDrag(screenX, screenY, true);

    touchPos.set(screenX, screenY);
    view.unprojectTouchPos(touchPos);

    CellPosition cpGrid = view.convertMouseToGrid(touchPos.x, touchPos.y);
    CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);


    if (cellPositionOnGrid(cpGrid)) {
      System.out.println("x = " + cpGrid.col() + ", y = " + cpGrid.row());
    }

    if (cellPositionOnUpgradeOptions(cpUpgrade)) {
      model.setGrabbedUpgradeIndex(cpUpgrade.col());
      model.setUpgradeGrabbed(true);
    }

    return true;
  }

  private boolean rightClick(int screenX, int screenY) {
    if (model.isRightClickLocked()) return true;

    model.setLeftClickLocked(true);
    model.updateMouseDrag(screenX, screenY, false);
    return true;
  }

  private boolean middleClick() {
    model.setCameraZoomRecently(true);
    model.updateCameraZoomDeltaTime(0f);
    return true;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (model.isLeftClickLocked()) {
      return rightClickDragged(screenX, screenY);
    } else if (model.isRightClickLocked()) {
      return leftClickDragged(screenX, screenY);
    }
    return true;
  }

  private boolean leftClickDragged(int screenX, int screenY) {
    model.updateMouseDrag(screenX, screenY, true);
    return true;
  }

  private boolean rightClickDragged(int screenX, int screenY) {
    Vector2 dragDelta = model.getDragDelta(screenX, screenY, true);
    view.updateCameraPosition((int)dragDelta.x, (int)dragDelta.y);
    return true;
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

  private boolean leftClickRelease() {
    if (model.isLeftClickLocked()) return true;
    model.setRightClickLocked(false);
    model.setReleaseGrabbedUpgrade(true);
    return true;
  }

  private boolean rightClickRelease() {
    if (model.isRightClickLocked()) return true;
    model.setLeftClickLocked(false);
    return true;
  }

  private CellPosition convertMouseToGrid(float x, float y) {
    return new CellPosition(
        (int) Math.floor(y - model.getGridOffsetY()),
        (int) Math.floor(x - model.getGridOffsetX())
    );
  }

  private CellPosition convertMouseToUpgradeBar(float x, float y) {
    return new CellPosition(
        (int) Math.floor(y - model.getUpgradeOffsetY()),
        (int) Math.floor(x - model.getUpgradeOffsetX())
    );
  }

  private boolean cellPositionOnGrid(CellPosition cp) {
    int gridX = cp.col();
    int gridY = cp.row();
    return !(gridX < 0 || gridX > model.getGridWidth() - 1 ||
        gridY < 0 || gridY > model.getGridHeight() - 1);
  }

  private boolean cellPositionOnUpgradeOptions(CellPosition cp) {
    int upgradeX = cp.col();
    int upgradeY = cp.row();
    return !((upgradeY != 0) || (upgradeX < 0) ||
        (upgradeX > model.getNumUpgradeOptions() - 1));
  }
}