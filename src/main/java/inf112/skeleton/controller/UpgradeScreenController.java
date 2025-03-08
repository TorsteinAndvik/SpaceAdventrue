package inf112.skeleton.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.model.UpgradeScreenModel;
import inf112.skeleton.view.UpgradeScreen;

public class UpgradeScreenController extends GenericController {

    private final UpgradeScreenModel model;
    private final UpgradeScreen view;

    public UpgradeScreenController(UpgradeScreen view, UpgradeScreenModel model) {
        super(); // GenericController gives us touchpos
        this.view = view;
        this.model = model;
    }

    @Override
    protected void handleScroll(float amount) {
        model.updateCameraZoom(amount);
        view.updateCameraZoom(model.getCurrentZoom());
    }

    @Override
    protected boolean handleKeyDown(int keycode) {
        // TODO: add this feature?
        return keycode == Keys.T;
    }

    @Override
    protected boolean leftClick(int screenX, int screenY) {
        if (isLeftClickLocked()) {
            return true;
        }

        setRightClickLocked(true);
        model.setReleaseGrabbedUpgrade(false);
        model.updateDragPosition(screenX, screenY);

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

    @Override
    protected boolean rightClick(int screenX, int screenY) {
        if (isRightClickLocked()) {
            return true;
        }

        setLeftClickLocked(true);
        model.updateDragPosition(screenX, screenY);
        return true;
    }

    @Override
    protected boolean middleClick() {
        model.setCameraZoomRecently(true);
        model.updateCameraZoomDeltaTime(0f);
        return true;
    }

    @Override
    protected boolean leftClickDragged(int screenX, int screenY) {
        model.updateDragPosition(screenX, screenY);
        return true;
    }

    @Override
    protected boolean rightClickDragged(int screenX, int screenY) {
        model.updateDragPosition(screenX, screenY);
        Vector2 dragDelta = model.getDragDelta();
        view.updateCameraPosition((int) dragDelta.x, (int) dragDelta.y);
        return true;
    }

    @Override
    protected boolean leftClickRelease() {
        if (isLeftClickLocked()) {
            return true;
        }
        setRightClickLocked(false);
        model.setReleaseGrabbedUpgrade(true);
        return true;
    }

    @Override
    protected boolean rightClickRelease() {
        if (isRightClickLocked()) {
            return true;
        }
        setLeftClickLocked(false);
        return true;
    }

    private CellPosition convertMouseToGrid(float x, float y) {
        return new CellPosition(
                (int) Math.floor(y - model.getGridOffsetY()),
                (int) Math.floor(x - model.getGridOffsetX()));
    }

    private CellPosition convertMouseToUpgradeBar(float x, float y) {
        return new CellPosition(
                (int) Math.floor(y - model.getUpgradeOffsetY()),
                (int) Math.floor(x - model.getUpgradeOffsetX()));
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