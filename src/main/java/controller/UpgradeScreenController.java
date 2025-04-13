package controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import grid.CellPosition;
import grid.GridCell;
import model.GameStateModel;
import model.ShipComponents.Components.Fuselage;
import model.SpaceGameModel;
import model.UpgradeScreenModel;
import view.SpaceGame;
import view.screens.UpgradeScreen;

public class UpgradeScreenController extends GenericController {

    private final UpgradeScreen view;
    private final UpgradeScreenModel upgradeModel;
    private final SpaceGameModel spaceModel;

    public UpgradeScreenController(UpgradeScreen view, GameStateModel gameStateModel,
        SpaceGame game) {
        super(view, gameStateModel, game); // GenericController gives us touchpos
        this.view = view;
        this.upgradeModel = gameStateModel.getUpgradeScreenModel();
        this.spaceModel = gameStateModel.getSpaceGameModel();
    }

    public Iterable<GridCell<Fuselage>> getPlayerShipParts() {
        return spaceModel.getPlayer().getShipStructure();
    }

    @Override
    public void update(float delta) {
        upgradeModel.update(delta);
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
            case Input.Keys.ESCAPE, Keys.U -> {
                game.setSpaceScreen();
                yield true;
            }
            default -> false;
        };
    }

    private void toggleUpgradeDescriptionMode() {
        if (upgradeModel.upgradeInspectionModeIsActive()) {
            upgradeModel.setUpgradeInspectionModeIsActive(false);
            return;
        }

        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        view.unprojectTouchPos(touchPos);

        CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);
        upgradeModel.setUpgradeInspectionModeIsActive(cellPositionOnUpgradeBar(cpUpgrade));
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
        if (cellPositionOnUpgradeBar(cpUpgrade)) {
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

        CellPosition cpGrid = convertMouseToGrid(touchPos.x, touchPos.y);
        CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);

        if (cellPositionOnGrid(cpGrid)) {// TODO: Implement actions when clicking the grid.
            System.out.println("x = " + cpGrid.col() + ", y = " + cpGrid.row());
        }

        if (cellPositionOnUpgradeBar(cpUpgrade)) {
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

        touchPos.set(Gdx.input.getX(), Gdx.input.getY());
        view.unprojectTouchPos(touchPos);
        CellPosition cpGrid = convertMouseToGrid(touchPos.x, touchPos.y);

        upgradeModel.setReleasedCellPosition(cpGrid);

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

    /**
     * Converts a mouse click to a grid position.
     *
     * @param x the x-coordinate of the mouse click.
     * @param y the y-coordinate of the mouse click.
     * @return a CellPosition representing the click in the grid.
     */
    public CellPosition convertMouseToGrid(float x, float y) {
        return new CellPosition(
            (int) Math.floor(y - upgradeModel.getGridOffsetY()),
            (int) Math.floor(x - upgradeModel.getGridOffsetX()));
    }

    /**
     * Converts a mouse click to an upgrade bar position.
     *
     * @param x the x-coordinate of the mouse click.
     * @param y the y-coordinate of the mouse click.
     * @return a CellPosition representing the click on the upgrade bar.
     */
    public CellPosition convertMouseToUpgradeBar(float x, float y) {
        return new CellPosition(
            (int) Math.floor(y - upgradeModel.getUpgradeOffsetY()),
            (int) Math.floor(x - upgradeModel.getUpgradeOffsetX()));
    }

    /**
     * Checks if a cell position is on the grid.
     *
     * @param cp the <code>CellPosition</code> to check.
     * @return <code>true</code> if the cell position is on the grid,
     * <code>false</code> otherwise.
     */
    public boolean cellPositionOnGrid(CellPosition cp) {
        int gridX = cp.col();
        int gridY = cp.row();
        return !(gridX < 0 || gridX > upgradeModel.getGridWidth() - 1 ||
            gridY < 0 || gridY > upgradeModel.getGridHeight() - 1);
    }

    /**
     * Checks if a cell position is on the upgrade bar.
     *
     * @param cp the <code>CellPosition</code> to check.
     * @return <code>true</code> if the cell position is on the upgrade bar,
     * <code>false</code> otherwise.
     */
    public boolean cellPositionOnUpgradeBar(CellPosition cp) {
        int upgradeX = cp.col();
        int upgradeY = cp.row();
        return !((upgradeY != 0) || (upgradeX < 0) ||
            (upgradeX > upgradeModel.getNumUpgradeOptions() - 1));
    }
}