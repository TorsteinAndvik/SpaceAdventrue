package controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import grid.CellPosition;
import model.GameStateModel;
import model.UpgradeScreenModel;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.ShipUpgrade;
import model.utils.FloatPair;
import view.SpaceGame;
import view.screens.UpgradeScreen;

public class UpgradeScreenController extends GenericController {

    private final UpgradeScreen view;
    private final UpgradeScreenModel upgradeModel;

    public UpgradeScreenController(UpgradeScreen view, GameStateModel gameStateModel,
            SpaceGame game) {
        super(view, gameStateModel, game);
        this.view = view;
        this.upgradeModel = gameStateModel.getUpgradeScreenModel();
    }

    @Override
    public void update(float delta) {
        upgradeModel.update(delta);
    }

    public void reset() {
        upgradeModel.getUpgradeHandler().expand();
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
                upgradeModel.exitUpgradeHandler();
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

        if (clickedOnUpgradeStageDisplay(touchPos.x, touchPos.y)) {
            handleUpgradeDisplayClick(upgradeModel.getCellHighlightPosition(), touchPos.x, touchPos.y);
            return true;

        } else if (cellPositionOnGrid(cpGrid) && upgradeModel.getUpgradeHandler().hasFuselage(cpGrid)) {
            upgradeModel.setCellHighlight(true, cpGrid);
            updateAndShowUpgradeStageDisplay(cpGrid);
            return true;

        } else {
            upgradeModel.disableCellHighlight();
            view.getUpgradeStageDisplay().setVisibility(false);
        }

        if (cellPositionOnUpgradeBar(cpUpgrade)) {
            if (upgradeModel.getStoreShelf().get(cpUpgrade.col()).price() > upgradeModel.getPlayerResources()) {
                return true;
            }

            upgradeModel.setGrabbedUpgradeIndex(cpUpgrade.col());
            upgradeModel.setUpgradeGrabbed(true);
            return true;
        }

        return false;
    }

    private void handleUpgradeDisplayClick(CellPosition cpGrid, float x, float y) {
        ShipUpgrade upgrade = view.getUpgradeStageDisplay().getClickedUpgrade(x, y, true);
        if (upgrade != null) { // didn't click on empty space, or clicked on already selected upgrade
            if (upgradeModel.attemptUpgradeStagePurchase(cpGrid, upgrade)) {// stage upgrade successful
                // view.getUpgradeStageDisplay().setCurrentStats(upgradeModel.getPlayerStats());
                Fuselage usdFuselage = view.getUpgradeStageDisplay().getFuselage();
                view.getUpgradeStageDisplay().setUpgradeEligibility(
                        fuselageUpgradeableAndAffordable(usdFuselage),
                        upgradeUpgradeableAndAffordable(usdFuselage));
                upgradeModel.setUpgradeStageDisplayPrices(usdFuselage);
            }
        }
    }

    private void updateAndShowUpgradeStageDisplay(CellPosition cpGrid) {
        Fuselage fuselage = upgradeModel.getUpgradeHandler().getFuselage(cpGrid);
        if (fuselage != null) { // ==null would mean cpGrid is on the grid but not a fuselage
            view.getUpgradeStageDisplay().setPosition(
                    new FloatPair(
                            upgradeModel.getGridOffsetX() + (float) cpGrid.col() + 1.5f,
                            upgradeModel.getGridOffsetY() + (float) cpGrid.row() - 1.5f));

            view.getUpgradeStageDisplay().setUpgradeEligibility(
                    fuselageUpgradeableAndAffordable(fuselage),
                    upgradeUpgradeableAndAffordable(fuselage));

            upgradeModel.setUpgradeStageDisplayPrices(fuselage);

            view.getUpgradeStageDisplay().setComponents(upgradeModel.getUpgradeHandler().getFuselage(cpGrid));
            view.getUpgradeStageDisplay().setVisibility(true);
        }
    }

    private boolean fuselageUpgradeableAndAffordable(Fuselage fuselage) {
        return fuselage.getStage().isUpgradeable() && upgradeModel.canAfford(fuselage);
    }

    private boolean upgradeUpgradeableAndAffordable(Fuselage fuselage) {
        return fuselage.hasUpgrade()
                && fuselage.getUpgrade().getStage().isUpgradeable()
                && upgradeModel.canAfford(fuselage.getUpgrade())
                && upgradeModel.getUpgradeHandler().upgradeStageIncreaseIsAllowed(fuselage);
    }

    public boolean clickedOnUpgradeStageDisplay(float x, float y) {
        return view.getUpgradeStageDisplay().clickedOnUpgradeStageDisplay(x, y);
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
        upgradeModel.updateCameraZoom(0f);
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
     *         <code>false</code> otherwise.
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
     *         <code>false</code> otherwise.
     */
    public boolean cellPositionOnUpgradeBar(CellPosition cp) {
        int upgradeX = cp.col();
        int upgradeY = cp.row();
        return !((upgradeY != 0) || (upgradeX < 0) ||
                (upgradeX > upgradeModel.getNumUpgradeOptions() - 1));
    }
}