package inf112.skeleton.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.model.SpaceGameModel;
import inf112.skeleton.model.UpgradeScreenModel;
import inf112.skeleton.model.ShipComponents.Components.Fuselage;
import inf112.skeleton.view.SpaceGame;
import inf112.skeleton.view.UpgradeScreen;

public class UpgradeScreenController extends GenericController {

    private final UpgradeScreen view;
    private final UpgradeScreenModel upgradeModel;
    private final SpaceGameModel spaceModel;
    private final SpaceGame game;

    public UpgradeScreenController(UpgradeScreen view, UpgradeScreenModel upgradeModel, SpaceGameModel spaceModel,
            SpaceGame game) {
        super(); // GenericController gives us touchpos
        this.view = view;
        this.upgradeModel = upgradeModel;
        this.spaceModel = spaceModel;
        this.game = game;
    }

    public Iterable<GridCell<Fuselage>> getPlayerShipParts() {
        return spaceModel.getSpaceShips()[0].getShipStructure().iterable();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    protected void handleScroll(float amount) {
        upgradeModel.updateCameraZoom(amount);
        view.updateCameraZoom(upgradeModel.getCurrentZoom());
    }

    @Override
    protected boolean handleKeyDown(int keycode) {
        return switch (keycode) {
            case Input.Keys.T -> { // switch inspection mode
                toggleUpgradeDescriptionMode();
                updateInspectionMode();
                yield true;
            }
            case Input.Keys.ESCAPE -> {
                game.setSpaceScreen();
                yield true;
            }
            default -> false;
        };
    }

    private void toggleUpgradeDescriptionMode() {
        if (upgradeModel.upgradeInspectionModeIsActive()) {
            upgradeModel.setUpgradeInspectionModeIsActive(false);
        }

        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        view.unprojectTouchPos(touchPos);

        CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);
        upgradeModel.setUpgradeInspectionModeIsActive(cellPositionOnUpgradeOptions(cpUpgrade));
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updateInspectionMode();
        return true;
    }

    private void updateInspectionMode() {
        if (!upgradeModel.upgradeInspectionModeIsActive()) {
            return;
        }

        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        view.unprojectTouchPos(touchPos);
        CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);
        if (cellPositionOnUpgradeOptions(cpUpgrade)) {
            upgradeModel.setInspectedUpgradeIndex(cpUpgrade.col());
            upgradeModel.setUpgradeInspectionModeIsActive(true);
        } else {
            upgradeModel.setUpgradeInspectionModeIsActive(false);
        }
    }

    @Override
    protected boolean leftClick(int screenX, int screenY) {
        if (isLeftClickLocked()) {
            return true;
        }

        setRightClickLocked(true);
        upgradeModel.setReleaseGrabbedUpgrade(false);
        upgradeModel.updateDragPosition(screenX, screenY);

        touchPos.set(screenX, screenY);
        view.unprojectTouchPos(touchPos);

        CellPosition cpGrid = view.convertMouseToGrid(touchPos.x, touchPos.y);
        CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);

        if (cellPositionOnGrid(cpGrid)) {// TODO: Implement actions when clicking the grid.
            System.out.println("x = " + cpGrid.col() + ", y = " + cpGrid.row());
        }

        if (cellPositionOnUpgradeOptions(cpUpgrade)) {
            upgradeModel.setGrabbedUpgradeIndex(cpUpgrade.col());
            upgradeModel.setUpgradeGrabbed(true);
        }

        return true;
    }

    @Override
    protected boolean rightClick(int screenX, int screenY) {
        if (isRightClickLocked()) {
            return true;
        }

        setLeftClickLocked(true);
        upgradeModel.updateDragPosition(screenX, screenY);
        return true;
    }

    @Override
    protected boolean middleClick() {
        upgradeModel.setCameraZoomRecently(true);
        upgradeModel.updateCameraZoomDeltaTime(0f);
        return true;
    }

    @Override
    protected boolean leftClickDragged(int screenX, int screenY) {
        upgradeModel.updateDragPosition(screenX, screenY);
        return true;
    }

    @Override
    protected boolean rightClickDragged(int screenX, int screenY) {
        upgradeModel.updateDragPosition(screenX, screenY);
        Vector2 dragDelta = upgradeModel.getDragDelta();
        view.updateCameraPosition((int) dragDelta.x, (int) dragDelta.y);
        return true;
    }

    @Override
    protected boolean leftClickRelease() {
        if (isLeftClickLocked()) {
            return true;
        }
        setRightClickLocked(false);
        upgradeModel.setReleaseGrabbedUpgrade(true);
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
                (int) Math.floor(y - upgradeModel.getGridOffsetY()),
                (int) Math.floor(x - upgradeModel.getGridOffsetX()));
    }

    private CellPosition convertMouseToUpgradeBar(float x, float y) {
        return new CellPosition(
                (int) Math.floor(y - upgradeModel.getUpgradeOffsetY()),
                (int) Math.floor(x - upgradeModel.getUpgradeOffsetX()));
    }

    private boolean cellPositionOnGrid(CellPosition cp) {
        int gridX = cp.col();
        int gridY = cp.row();
        return !(gridX < 0 || gridX > upgradeModel.getGridWidth() - 1 ||
                gridY < 0 || gridY > upgradeModel.getGridHeight() - 1);
    }

    private boolean cellPositionOnUpgradeOptions(CellPosition cp) {
        int upgradeX = cp.col();
        int upgradeY = cp.row();
        return !((upgradeY != 0) || (upgradeX < 0) ||
                (upgradeX > upgradeModel.getNumUpgradeOptions() - 1));
    }
}