package inf112.skeleton.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Model for the Upgrade Screen. Handles game state for upgrades, camera
 * controls and UI positioning.
 */
public class UpgradeScreenModel {
    private final int gridWidth = 5;
    private final int gridHeight = 5;
    private final int numUpgradeOptions = 4;
    private float gridOffsetX;
    private float gridOffsetY;
    private float upgradeOffsetX;
    private float upgradeOffsetY;

    private final float[] cameraZoomLevels = { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f };
    private int cameraCurrentZoomLevel;
    private float cameraZoomDeltaTime;
    private final float cameraZoomTextFadeCutoffTime = 0.5f;
    private boolean cameraZoomRecently;
    private final Vector2 cameraPosition;

    private boolean upgradeGrabbed;
    private boolean releaseGrabbedUpgrade;
    private int grabbedUpgradeIndex;

    private final Vector2 mousePosition;
    private final Vector2 dragPosition;
    private final Vector2 lastDragPosition;

    /**
     * Initializes an upgrade screen model with vectors for tracking positions.
     * Also initializes camera zoom, and sets to middle zoom level.
     */
    public UpgradeScreenModel() {
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
        upgradeGrabbed = false;
        releaseGrabbedUpgrade = false;
        grabbedUpgradeIndex = -1;
        cameraZoomRecently = false;
        cameraZoomDeltaTime = 0f;
    }

    /**
     * Upgrade camera zoom level based on scroll input.
     * Clamps zoom level between minimum and maximum zoom.
     * Triggers zoom level display UI.
     * 
     * @param amount The scroll amount to adjust zoom by (positive values = zoom out
     *               etc.)
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
        if (cameraZoomRecently) {
            cameraZoomDeltaTime += delta;
            if (cameraZoomDeltaTime > cameraZoomTextFadeCutoffTime) {
                float fadeAmount = 1f - (float) Math.pow((cameraZoomDeltaTime - cameraZoomTextFadeCutoffTime), 2);
                if (fadeAmount < 0) {
                    cameraZoomRecently = false;
                }
            }
        }
    }

    /**
     * Updates the drag position for mouse input tracking.
     * Stores current position as last position before updating
     * 
     * @param x The new x-coordinate in screen coordinates.
     * @param y The new y-coordinate in screen coordinates.
     */
    public void updateDragPosition(int x, int y) {
        lastDragPosition.set(dragPosition);
        dragPosition.set(x, y);
    }

    /**
     * Calculated the delta between the current and last drag positions.
     * Used for camera movement and drag-and-drop operations
     * 
     * @return A Vector2 containing the x and y differences between drag positions.
     */
    public Vector2 getDragDelta() {
        return new Vector2(
                lastDragPosition.x - dragPosition.x,
                lastDragPosition.y - dragPosition.y);
    }

    /**
     * Updates offset for upgrade grid adnd upgrade bar elements.
     * Centers these elements in available screen space.
     * 
     * @param worldWidth  The width of the game world in world units.
     * @param worldHeight The height of the game world in world units.
     */
    public void updateOffsets(float worldWidth, float worldHeight) {
        float upgradeToGridDelta = 2f;
        gridOffsetX = (worldWidth - gridWidth) / 2f;
        gridOffsetY = (worldHeight - gridHeight - upgradeToGridDelta) / 2f;
        upgradeOffsetX = (worldWidth - numUpgradeOptions) / 2f;
        upgradeOffsetY = gridOffsetY + gridHeight + upgradeToGridDelta / 2f;
    }

    public float getCurrentZoom() {
        return cameraZoomLevels[cameraCurrentZoomLevel];
    }

    public boolean isUpgradeGrabbed() {
        return upgradeGrabbed;
    }

    public int getGrabbedUpgradeIndex() {
        return grabbedUpgradeIndex;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getNumUpgradeOptions() {
        return numUpgradeOptions;
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

    public void setCameraZoomRecently(boolean value) {
        this.cameraZoomRecently = value;
    }
}