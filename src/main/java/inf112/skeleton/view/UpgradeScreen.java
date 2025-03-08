package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.controller.UpgradeScreenController;
import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.model.UpgradeScreenModel;

/**
 * Screen for managing ship upgrades in game. Handles rendering of upgrade grid,
 * options and UI
 * elements.
 */
public class UpgradeScreen extends InputAdapter implements Screen {

    private final SpaceGame game;
    private final SpriteBatch batch;
    private final ScreenViewport viewportGame;
    private final ScreenViewport viewportUI;
    private final AssetManager manager; // An assetmanager helps with loading assets and disposing them once they are no
                                        // longer needed
    private final UpgradeScreenModel model;
    private final UpgradeScreenController controller;
    private final Vector2 touchPos; // Simplifies converting touch / mouse position in window-coordinates (pixels)
                                    // to game-coordinates (meters x meters set in viewport)

    private BitmapFont fontBold; // Agency FB Bold
    private BitmapFont fontRegular; // Agency FB Regular

    private Sprite[] upgradeIcons;
    private Sprite squareRed;
    private Sprite squareGreen;
    private Sprite squareGray;
    // ui sprites:
    private Sprite msLeft;
    private Sprite msMiddle;
    private Sprite msRight;
    private Sprite kbT;

    private final float upgradeIconZoom = 0.8f;
    private float uiIconZoom;
    private final String[] upgradeStrings;

    /**
     * Creates a new upgrade screen with necessary components for rendering and
     * input handling.
     * Initializes viewports, fonts, sprites and upgrade system.
     *
     * @param spaceGame The main game instance providing sprites, and other
     *                  resources.
     */
    public UpgradeScreen(final SpaceGame spaceGame) {
        this.game = spaceGame;
        this.batch = game.getSpriteBatch();
        this.manager = game.getAssetManager();
        this.viewportGame = game.getScreenViewport();
        this.viewportUI = new ScreenViewport();
        this.model = new UpgradeScreenModel();
        this.controller = new UpgradeScreenController(this, model);
        this.touchPos = new Vector2();

        viewportUI.setUnitsPerPixel(viewportGame.getUnitsPerPixel());
        setupFonts();
        loadSprites();
        upgradeStrings = setupUpgradeStrings();
    }

    private void loadSprites() {
        squareRed = createSprite("images/upgrade_grid_tile_red.png", 1, 1);
        squareGreen = createSprite("images/upgrade_grid_tile_green.png", 1, 1);
        squareGray = createSprite("images/upgrade_grid_tile_gray.png", 1, 1);

        upgradeIcons = new Sprite[] { // [fuselage, rocket, turret, shield]
                createSprite("images/upgrades/fuselage_alt_stage_0.png", upgradeIconZoom, upgradeIconZoom),
                createSprite("images/upgrades/turret_laser_stage_0.png", upgradeIconZoom, upgradeIconZoom),
                createSprite("images/upgrades/rocket_stage_0.png", upgradeIconZoom, upgradeIconZoom),
                createSprite("images/upgrades/shield_stage_0.png", upgradeIconZoom, upgradeIconZoom)
        };

        uiIconZoom = fontRegular.getData().lineHeight;

        msLeft = createSprite("images/ui/Mouse_Left_Key_Light.png", uiIconZoom, uiIconZoom);
        msMiddle = createSprite("images/ui/Mouse_Middle_Key_Light.png", uiIconZoom, uiIconZoom);
        msRight = createSprite("images/ui/Mouse_Right_Key_Light.png", uiIconZoom, uiIconZoom);
        kbT = createSprite("images/ui/T_Key_Light.png", uiIconZoom, uiIconZoom);
    }

    private Sprite createSprite(String path, float width, float height) {
        Sprite sprite = new Sprite(manager.get(path, Texture.class));
        sprite.setSize(width, height);
        return sprite;
    }

    private String[] setupUpgradeStrings() {
        return new String[] {
                "Fuselage:\nUsed to expand the ship. Any new\npart must be attached to a piece\nof Fuselage.",
                "Turret:\nFires lasers at enemies and\nasteroids.",
                "Rocket:\nImproves acceleration and\ntop speed of the ship.",
                "Shield:\nIncrease the ship's health."
        };
    }

    private void setupFonts() {
        fontBold = manager.get("fonts/AGENCYB.ttf", BitmapFont.class);
        fontRegular = manager.get("fonts/AGENCYR.ttf", BitmapFont.class);

        // font are set as [integer]pt, need to scale them to our viewport by ratio of
        // viewport height to screen height in order to use world-unit sized font
        fontBold.setUseIntegerPositions(false);
        fontBold.getData().setScale(viewportGame.getUnitsPerPixel());

        fontRegular.setUseIntegerPositions(false);
        fontRegular.getData().setScale(viewportGame.getUnitsPerPixel());
    }

    @Override
    public void render(float delta) {
        model.updateCameraZoomDeltaTime(delta);
        ScreenUtils.clear(new Color(0f, 64f / 255f, 64f / 255f, 1f));

        viewportGame.apply(false);
        batch.setProjectionMatrix(viewportGame.getCamera().combined);
        batch.begin();

        // Draw upgrade options at top of screen
        for (int x = 0; x < model.getNumUpgradeOptions(); x++) {
            drawUpgradeSquare(x);
        }

        // Draw ship grid
        for (int x = 0; x < model.getGridWidth(); x++) {
            for (int y = 0; y < model.getGridHeight(); y++) {
                if ((x == 1 || x == 3) || (y < 2 || y == 4)) {
                    drawGridSquare(squareGreen, x, y);
                } else {
                    drawGridSquare(squareRed, x, y);
                }
            }
        }

        if (model.isUpgradeGrabbed()) {
            // draw a ghost copy of upgrade if hovering a grid cell
            CellPosition cpGrid = convertMouseToGrid(touchPos.x, touchPos.y);
            if (cellPositionOnGrid(cpGrid)) {
                upgradeIcons[model.getGrabbedUpgradeIndex()].setX(
                        model.getGridOffsetX() + cpGrid.col() + 0.5f * (1f - upgradeIconZoom));
                upgradeIcons[model.getGrabbedUpgradeIndex()].setY(
                        model.getGridOffsetY() + cpGrid.row() + 0.5f * (1f - upgradeIconZoom));
                upgradeIcons[model.getGrabbedUpgradeIndex()].draw(batch, 0.5f);
            }

            // draw held upgrade
            Vector2 pos = worldToGameCoordinates(model.getDragX(), model.getDragY());
            upgradeIcons[model.getGrabbedUpgradeIndex()].setX(pos.x - 0.5f * upgradeIconZoom);
            upgradeIcons[model.getGrabbedUpgradeIndex()].setY(pos.y - 0.5f * upgradeIconZoom);
            upgradeIcons[model.getGrabbedUpgradeIndex()].draw(batch);
        }

        if (model.isReleaseGrabbedUpgrade() && model.isUpgradeGrabbed()) {
            model.setReleaseGrabbedUpgrade(false);
            model.setUpgradeGrabbed(false);
        }

        batch.end();

        // draw UI elements
        viewportUI.apply(true);
        batch.setProjectionMatrix(viewportUI.getCamera().combined);
        batch.begin();

        fontRegular.setColor(Color.WHITE);

        // zoom (mouse wheel)
        msMiddle.setX(0f);
        msMiddle.setY(0.15f * fontRegular.getData().lineHeight);
        msMiddle.draw(batch);
        fontRegular.draw(batch, "Adjust zoom", fontRegular.getData().lineHeight,
                fontRegular.getData().lineHeight);

        // move camera (right click)
        msRight.setX(0f);
        msRight.setY(1.15f * fontRegular.getData().lineHeight);
        msRight.draw(batch);
        fontRegular.draw(batch, "Move camera", fontRegular.getData().lineHeight,
                2 * fontRegular.getData().lineHeight);

        // grab upgrade (left click)
        msLeft.setX(0f);
        msLeft.setY(2.15f * fontRegular.getData().lineHeight);
        msLeft.draw(batch);
        fontRegular.draw(batch, "Grab upgrade", fontRegular.getData().lineHeight,
                3 * fontRegular.getData().lineHeight);

        // inspect upgrade (T key)
        kbT.setX(0f);
        kbT.setY(3.15f * fontRegular.getData().lineHeight);
        kbT.draw(batch);
        fontRegular.draw(batch, "Inspect upgrade", fontRegular.getData().lineHeight,
                4 * fontRegular.getData().lineHeight);

        if (model.isCameraZoomRecently()) {
            float alpha = model.getCameraZoomDeltaTime() < model.getCameraZoomTextFadeCutoffTime() ? 1f
                    : 1f - (float) Math.pow(
                            (model.getCameraZoomDeltaTime() - model.getCameraZoomTextFadeCutoffTime()), 2);
            if (alpha > 0) {
                Color fontColor = new Color(1f, 0.47f, 0.55f, alpha);
                fontRegular.setColor(fontColor);
                fontRegular.draw(batch, "Zoom = x" + model.getCurrentZoom(),
                        0.1f, 5 * fontRegular.getData().lineHeight);
            }
        }

        batch.end();
    }

    private void drawUpgradeSquare(int x) {
        squareGray.setX(model.getUpgradeOffsetX() + x);
        squareGray.setY(model.getUpgradeOffsetY());
        squareGray.draw(batch);

        upgradeIcons[x].setX(model.getUpgradeOffsetX() + x + (1f - upgradeIconZoom) / 2f);
        upgradeIcons[x].setY(model.getUpgradeOffsetY() + (1f - upgradeIconZoom) / 2f);
        upgradeIcons[x].draw(batch);
    }

    private void drawGridSquare(Sprite squareSprite, int x, int y) {
        squareSprite.setX(model.getGridOffsetX() + x);
        squareSprite.setY(model.getGridOffsetY() + y);
        squareSprite.draw(batch);
    }

    private Vector2 worldToGameCoordinates(float worldX, float worldY) {
        touchPos.set(worldX, worldY);
        viewportGame.unproject(touchPos);
        return touchPos;
    }

    /**
     * Convert a mouse click to a grid position
     *
     * @param x the x-coordinate of the mouse click.
     * @param y the y-coordinate of the mouse click.
     * @return a new CellPosition to represent the place in the grid.
     */
    public CellPosition convertMouseToGrid(float x, float y) {
        return new CellPosition(
                (int) Math.floor(y - model.getGridOffsetY()),
                (int) Math.floor(x - model.getGridOffsetX()));
    }

    private boolean cellPositionOnGrid(CellPosition cp) {
        int gridX = cp.col();
        int gridY = cp.row();
        return !(gridX < 0 || gridX > model.getGridWidth() - 1 ||
                gridY < 0 || gridY > model.getGridHeight() - 1);
    }

    /**
     * Updates camera zoom level. Used by controller to adjust view magnification
     * based on user
     * input.
     *
     * @param zoom The new zoom level to apply to camera.
     */
    public void updateCameraZoom(float zoom) {
        ((OrthographicCamera) viewportGame.getCamera()).zoom = zoom;
    }

    /**
     * Updates camera position based on screen offset. Handles camera movement and
     * staying within
     * world bounds.
     *
     * @param offsetX The x offset in screen coordinates.
     * @param offsetY the y offset in screen coordinates.
     */
    public void updateCameraPosition(int offsetX, int offsetY) {
        float cameraX = viewportGame.getScreenWidth() / 2f + offsetX;
        float cameraY = viewportGame.getScreenHeight() / 2f + offsetY;

        touchPos.set(cameraX, cameraY);
        viewportGame.unproject(touchPos);
        clampVector(touchPos, 0f, viewportGame.getWorldWidth(), 0f, viewportGame.getWorldHeight());

        viewportGame.getCamera().position.set(touchPos, 0);
    }

    private void clampVector(Vector2 v, float minX, float maxX, float minY, float maxY) {
        v.x = Math.max(Math.min(v.x, maxX), minX);
        v.y = Math.max(Math.min(v.y, maxY), minY);
    }

    /**
     * Convert screen coordinate to world coordinates. Windows have origin at
     * top-left, game world at
     * bottom-left.
     *
     * @param pos The position vector to be converted.
     */
    public void unprojectTouchPos(Vector2 pos) {
        viewportGame.unproject(pos);
    }

    @Override
    public void resize(int width, int height) {
        int oldWidth = viewportGame.getScreenWidth();
        int oldHeight = viewportGame.getScreenHeight();
        viewportGame.update(width, height, false);

        if (viewportGame.getScreenWidth() == 0 || viewportGame.getScreenHeight() == 0) {
            updateCameraPosition(0, 0);
        } else {
            float screenDeltaX = (width - oldWidth) / 2f;
            float screenDeltaY = (height - oldHeight) / 2f;
            float deltaX = viewportGame.getUnitsPerPixel() * screenDeltaX;
            float deltaY = viewportGame.getUnitsPerPixel() * screenDeltaY;
            viewportGame.getCamera().translate(deltaX, deltaY, 0f);
        }

        model.updateOffsets(viewportGame.getWorldWidth(), viewportGame.getWorldHeight());
        viewportUI.update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
        viewportGame.apply(true);
        viewportUI.apply(true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}