package model;

import com.badlogic.gdx.math.Vector2;
import grid.CellPosition;
import grid.IGrid;
import java.util.List;
import java.util.Set;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.ShipUpgrade;
import model.ShipComponents.Components.stats.MaxStatCalculator;
import model.ShipComponents.Components.stats.StatModifier;
import model.ShipComponents.UpgradeHandler;
import model.SpaceCharacters.Ships.Player;
import model.World.StoreItem;
import model.World.UpgradeStore;
import view.bars.UpgradeStageDisplay;
import model.ShipComponents.UpgradeType;

/**
 * Model for the Upgrade Screen. Handles game state for upgrades, camera
 * controls and UI
 * positioning.
 */
public class UpgradeScreenModel {

    private float gridOffsetX;
    private float gridOffsetY;
    private float upgradeOffsetX;
    private float upgradeOffsetY;

    private final float[] cameraZoomLevels = { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f };
    private int cameraCurrentZoomLevel;
    private float cameraZoomDeltaTime;
    private float oldCameraZoom;
    private final float cameraZoomTextFadeCutoffTime = 0.5f;
    private boolean cameraZoomRecently;
    private final Vector2 cameraPosition;

    private boolean upgradeGrabbed;
    private boolean releaseGrabbedUpgrade;
    private int grabbedUpgradeIndex;
    private int inspectedUpgradeIndex;

    private boolean upgradeInspectionModeIsActive;

    private final Vector2 mousePosition;
    private final Vector2 dragPosition;
    private final Vector2 lastDragPosition;
    private CellPosition releasedCellPosition;

    private UpgradeStore store;
    private final Player player;
    private final UpgradeHandler upgradeHandler;
    public boolean offsetsMustBeUpdated;

    private CellPosition highlightedCellPosition;
    private boolean showCellHighlight;

    private UpgradeStageDisplay upgradeStageDisplay;
    private final MaxStatCalculator maxStatCalculator;

    /**
     * Initializes an upgrade screen model with vectors for tracking positions. Also
     * initializes
     * camera zoom, and sets to middle zoom level.
     */
    public UpgradeScreenModel(Player player) {
        this.player = player;
        upgradeHandler = new UpgradeHandler(player.getShipStructure());
        store = new UpgradeStore();
        this.maxStatCalculator = new MaxStatCalculator();

        cameraPosition = new Vector2();
        mousePosition = new Vector2();
        dragPosition = new Vector2();
        lastDragPosition = new Vector2();
        cameraCurrentZoomLevel = cameraZoomLevels.length / 2;
        resetState();
    }

    /**
     * Reset state of the model.
     */
    public void resetState() {
        upgradeInspectionModeIsActive = false;
        upgradeGrabbed = false;
        releaseGrabbedUpgrade = false;
        grabbedUpgradeIndex = -1;
        inspectedUpgradeIndex = -1;
        cameraZoomRecently = false;
        cameraZoomDeltaTime = 0f;
    }

    /**
     * Updates the game state based on the elapsed time (delta).
     *
     * <p>
     * This method handles camera zoom updates and processes the release of a
     * grabbed upgrade.
     * If an upgrade is grabbed and released, it is either placed as a fuselage or
     * as a ship upgrade.
     * After placing the upgrade, relevant state variables are reset.
     *
     * @param delta The time elapsed since the last update, used for time-based
     *              calculations.
     */
    public void update(float delta) {
        updateCameraZoomDeltaTime(delta);

        if (isReleaseGrabbedUpgrade() && isUpgradeGrabbed()) {

            StoreItem<UpgradeType> item = store.getStoreItem(getGrabbedUpgradeIndex());

            int upgradePrice = item.price();

            if (item.item() == UpgradeType.FUSELAGE) {
                offsetsMustBeUpdated = true;
            }
            boolean canAfford = player.getInventory().canAfford(upgradePrice);

            if (canAfford) {
                boolean upgradeSuccess = upgradeHandler.placeItem(releasedCellPosition,
                        getGrabbedShipUpgrade().getType());
                if (upgradeSuccess) {
                    player.getInventory().spendResources(upgradePrice);
                    if (upgradeStageDisplay != null) {
                        upgradeStageDisplay.setMaxStats(maxStatCalculator.getFuselageMax(player.getNumFuselage()));
                        upgradeStageDisplay.setCurrentStats(getPlayerStats());
                    }
                }
            }

            releasedCellPosition = null;
            setReleaseGrabbedUpgrade(false);
            setUpgradeGrabbed(false);
        }
    }

    private ShipUpgrade getGrabbedShipUpgrade() {
        UpgradeType upgradeType = store.getStoreItem(getGrabbedUpgradeIndex()).item();
        return ShipUpgrade.getShipUpgrade(upgradeType);
    }

    public StatModifier getPlayerStats() {
        return player.getShipStructure().getCombinedStatModifier().copy();
    }

    /**
     * Upgrade camera zoom level based on scroll input.
     * Clamps zoom level between minimum and maximum zoom.
     * Triggers zoom level display UI.
     *
     * @param amount The scroll amount to adjust zoom by (+ = zoom out, - = zoom in)
     */
    public void updateCameraZoom(float amount) {
        cameraCurrentZoomLevel = Math.min(Math.max(cameraCurrentZoomLevel + (int) amount, 0),
                cameraZoomLevels.length - 1);
        cameraZoomRecently = true;
        cameraZoomDeltaTime = 0f;
    }

    /**
     * Update timer for fading out zoom level display.
     *
     * @param delta Time elapsed since last update in seconds.
     */
    public void updateCameraZoomDeltaTime(float delta) {
        if (!cameraZoomRecently) {
            return;
        }
        cameraZoomDeltaTime += delta;
        if (cameraZoomDeltaTime > cameraZoomTextFadeCutoffTime) {
            float fadeAmount = 1f - (float) Math.pow((cameraZoomDeltaTime - cameraZoomTextFadeCutoffTime), 2);
            if (fadeAmount < 0) {
                cameraZoomRecently = false;
            }
        }
    }

    /**
     * Updates the camera zoom level used for the previous screen.
     * This is used for camera zoom transitions between screens.
     * It is recommended to call this in <code>UpgradeScreen.show()</code>.
     * Value is retrieved with the <code>getOldCameraZoom()</code> method.
     * 
     * @param oldCameraZoom
     */
    public void setOldCameraZoom(float oldCameraZoom) {
        this.oldCameraZoom = oldCameraZoom;
    }

    /**
     * Updates the drag position for mouse input tracking. Stores current position
     * as last position
     * before updating
     *
     * @param x The new x-coordinate in screen coordinates.
     * @param y The new y-coordinate in screen coordinates.
     */
    public void updateDragPosition(int x, int y) {
        lastDragPosition.set(dragPosition);
        dragPosition.set(x, y);
    }

    /**
     * Calculated the delta between the current and last drag positions. Used for
     * camera movement
     * and drag-and-drop operations
     *
     * @return A Vector2 containing the x and y differences between drag positions.
     */
    public Vector2 getDragDelta() {
        return new Vector2(
                lastDragPosition.x - dragPosition.x,
                lastDragPosition.y - dragPosition.y);
    }

    /**
     * Updates offset for upgrade grid and upgrade bar elements. Centers these
     * elements in available screen space.
     *
     * @param worldWidth  The width of the game world in world units.
     * @param worldHeight The height of the game world in world units.
     */
    public void updateOffsets(float worldWidth, float worldHeight) {
        float upgradeToGridDelta = 2f;
        gridOffsetX = (worldWidth - getGridWidth()) / 2f;
        gridOffsetY = (worldHeight - getGridHeight() - upgradeToGridDelta) / 2f;
        upgradeOffsetX = (worldWidth - getNumUpgradeOptions()) / 2f;
        upgradeOffsetY = gridOffsetY + getGridHeight() + upgradeToGridDelta / 2f;
    }

    public float getCurrentZoom() {
        return cameraZoomLevels[cameraCurrentZoomLevel];
    }

    public float getOldCameraZoom() {
        return oldCameraZoom;
    }

    public boolean isUpgradeGrabbed() {
        return upgradeGrabbed;
    }

    public int getGrabbedUpgradeIndex() {
        return grabbedUpgradeIndex;
    }

    public int getInspectedUpgradeIndex() {
        return inspectedUpgradeIndex;
    }

    public int getGridWidth() {
        return upgradeHandler.getGrid().cols();
    }

    public int getGridHeight() {
        return upgradeHandler.getGrid().rows();
    }

    public int getNumUpgradeOptions() {
        return getStoreShelf().size();
    }

    public float getGridOffsetX() {
        return gridOffsetX;
    }

    public float getGridOffsetY() {
        return gridOffsetY;
    }

    public float getUpgradeOffsetX() {
        return upgradeOffsetX;
    }

    public float getUpgradeOffsetY() {
        return upgradeOffsetY;
    }

    public Vector2 getCameraPosition() {
        return cameraPosition;
    }

    public boolean isCameraZoomRecently() {
        return cameraZoomRecently;
    }

    public float getCameraZoomDeltaTime() {
        return cameraZoomDeltaTime;
    }

    public float getCameraZoomTextFadeCutoffTime() {
        return cameraZoomTextFadeCutoffTime;
    }

    public boolean isReleaseGrabbedUpgrade() {
        return releaseGrabbedUpgrade;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public float getDragX() {
        return dragPosition.x;
    }

    public float getDragY() {
        return dragPosition.y;
    }

    public void setUpgradeGrabbed(boolean grabbed) {
        this.upgradeGrabbed = grabbed;
    }

    public void setGrabbedUpgradeIndex(int index) {
        this.grabbedUpgradeIndex = index;
    }

    public void setReleaseGrabbedUpgrade(boolean value) {
        this.releaseGrabbedUpgrade = value;
    }

    public void setInspectedUpgradeIndex(int index) {
        this.inspectedUpgradeIndex = index;
    }

    public void setCameraZoomRecently(boolean value) {
        this.cameraZoomRecently = value;
    }

    public boolean upgradeInspectionModeIsActive() {
        return this.upgradeInspectionModeIsActive;
    }

    public void setUpgradeInspectionModeIsActive(boolean value) {
        this.upgradeInspectionModeIsActive = value;
    }

    public void setReleasedCellPosition(CellPosition cellPosition) {
        releasedCellPosition = cellPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerResources() {
        return player.getInventory().getResourceCount();
    }

    public List<StoreItem<UpgradeType>> getStoreShelf() {
        return store.getStock();
    }

    public void addNewStoreStock(Set<StoreItem<UpgradeType>> stock) {
        store = new UpgradeStore(stock);
    }

    public IGrid<Fuselage> getExpandedGrid() {
        return upgradeHandler.getGrid();
    }

    public UpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    public void exitUpgradeHandler() {
        upgradeHandler.exit();
    }

    public void setCellHighlight(boolean showCellHighlight, CellPosition cpGrid) {
        if (upgradeHandler.hasFuselage(cpGrid)) {
            this.showCellHighlight = showCellHighlight;
            this.highlightedCellPosition = cpGrid;
        } else {
            this.showCellHighlight = false;
        }
    }

    public void disableCellHighlight() {
        showCellHighlight = false;
    }

    public boolean getShowCellHighlight() {
        return showCellHighlight;
    }

    public CellPosition getCellHighlightPosition() {
        return highlightedCellPosition;
    }

    public boolean canAfford(ShipUpgrade upgrade) {
        int price = store.getUpgradeStageShelf().get(upgrade.getType());
        return player.getInventory().canAfford(price);
    }

    public boolean attemptUpgradeStagePurchase(CellPosition cpGrid, ShipUpgrade upgrade) {
        if (canAfford(upgrade)) {
            boolean upgradeSuccess = upgradeHandler.upgradeStage(cpGrid, upgrade.getType() == UpgradeType.FUSELAGE);
            if (upgradeSuccess) {
                player.getInventory().spendResources(store.getUpgradeStageShelf().get(upgrade.getType()));
                if (upgradeStageDisplay != null) {
                    upgradeStageDisplay.setCurrentStats(getPlayerStats());
                }
                return true;
            }
        }
        return false;
    }

    public void setUpgradeStageDisplay(UpgradeStageDisplay display) {
        this.upgradeStageDisplay = display;
        upgradeStageDisplay.setMaxStats(maxStatCalculator.getFuselageMax(player.getNumFuselage()));
        upgradeStageDisplay.setCurrentStats(getPlayerStats());
        setUpgradeStageDisplayPrices(upgradeStageDisplay.getFuselage());
    }

    public void setUpgradeStageDisplayPrices(Fuselage usdFuselage) {
        if (upgradeStageDisplay != null) {
            if (usdFuselage == null) {
                upgradeStageDisplay.setPrices(0, 0);
            } else {
                int upgradePrice = usdFuselage.hasUpgrade()
                        ? store.getUpgradeStageShelf().get(usdFuselage.getUpgrade().getType())
                        : 0;
                int fuselagePrice = store.getUpgradeStageShelf().get(usdFuselage.getType());
                upgradeStageDisplay.setPrices(fuselagePrice, upgradePrice);
            }
        }
    }
}