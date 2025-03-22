package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.controller.UpgradeScreenController;
import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.model.ShipComponents.Components.ViewableShipStructure;
import inf112.skeleton.model.SpaceGameModel;
import inf112.skeleton.model.UpgradeScreenModel;
import inf112.skeleton.model.ShipComponents.UpgradeType;
import inf112.skeleton.model.ShipComponents.Components.Fuselage;
import java.util.HashMap;
import java.util.Map;

/**
 * Screen for managing ship upgrades in game. Handles rendering of upgrade grid, options and UI
 * elements.
 */
public class UpgradeScreen extends InputAdapter implements Screen {

    private final SpaceGame game;
    private final SpriteBatch batch;
    private final ShapeRenderer shape;
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
    private GlyphLayout glyphLayout;

    private Sprite[] upgradeIcons;
    private Sprite squareRed;
    private Sprite squareGreen;
    private Sprite squareGray;
    private Rectangle descriptionRect;

    // ui sprites:
    private Sprite msLeft;
    private Sprite msMiddle;
    private Sprite msRight;
    private Sprite kbT;
    private Sprite kbEsc;

    private final float upgradeIconZoom = 0.8f;
    private float uiIconZoom;
    private final String[] upgradeStrings;
    private Map<UpgradeType, Sprite> upgradeIconsMap;
    private ViewableShipStructure shipStructure;
    int cursorWidth = 64;
    int cursorHeight = 64;

    /**
     * Creates a new upgrade screen with necessary components for rendering and input handling.
     * Initializes viewports, fonts, sprites and upgrade system.
     *
     * @param spaceGame The main game instance providing sprites, and other resources.
     */
    public UpgradeScreen(final SpaceGame spaceGame, final SpaceGameModel spaceModel) {
        this.game = spaceGame;
        this.batch = game.getSpriteBatch();
        this.shape = new ShapeRenderer();
        this.manager = game.getAssetManager();
        this.viewportGame = game.getScreenViewport();
        this.viewportUI = new ScreenViewport();
        shipStructure = spaceModel.getPlayerSpaceShip().getShipStructure();
        this.model = new UpgradeScreenModel(shipStructure);
        this.controller = new UpgradeScreenController(this, model, spaceModel, game);
        this.touchPos = new Vector2();

        viewportUI.setUnitsPerPixel(viewportGame.getUnitsPerPixel());
        setupFonts();
        loadSprites();
        setupUpgradeHashMap(); //TODO: Fix duplicate sprite import.
        setupUISprites();

        descriptionRect = new Rectangle(0, 0, 0, 0);

        upgradeStrings = setupUpgradeStrings();
    }

    private void loadSprites() {
        squareRed = createSprite("images/upgrade_grid_tile_red.png", 1, 1);
        squareGreen = createSprite("images/upgrade_grid_tile_green.png", 1, 1);
        squareGray = createSprite("images/upgrade_grid_tile_gray.png", 1, 1);

        upgradeIcons = new Sprite[]{ // [fuselage, rocket, turret, shield]
                createSprite("images/upgrades/fuselage_alt_stage_0.png", upgradeIconZoom,
                        upgradeIconZoom),
                createSprite("images/upgrades/turret_laser_stage_0.png", upgradeIconZoom,
                        upgradeIconZoom),
                createSprite("images/upgrades/rocket_stage_0.png", upgradeIconZoom,
                        upgradeIconZoom),
                createSprite("images/upgrades/shield_stage_0.png", upgradeIconZoom, upgradeIconZoom)
        };

        uiIconZoom = fontRegular.getData().lineHeight;

        msLeft = createSprite("images/ui/Mouse_Left_Key_Light.png", uiIconZoom, uiIconZoom);
        msMiddle = createSprite("images/ui/Mouse_Middle_Key_Light.png", uiIconZoom, uiIconZoom);
        msRight = createSprite("images/ui/Mouse_Right_Key_Light.png", uiIconZoom, uiIconZoom);
        kbT = createSprite("images/ui/T_Key_Light.png", uiIconZoom, uiIconZoom);
        kbEsc = createSprite("images/ui/Esc_Key_Light.png", uiIconZoom, uiIconZoom);
    }

    private void setupUpgradeHashMap() { // TODO: Fix double sprite import. Needs this to draw space ship.
        upgradeIconsMap = new HashMap<UpgradeType, Sprite>();
        upgradeIconsMap.put(UpgradeType.TURRET,
                createSprite("images/upgrades/turret_laser_stage_0.png", 1, 1));
        upgradeIconsMap.put(UpgradeType.THRUSTER,
                createSprite("images/upgrades/rocket_stage_0.png", 1, 1));
        upgradeIconsMap.put(UpgradeType.SHIELD,
                createSprite("images/upgrades/shield_stage_0.png", 1, 1));
    }

    private void setupUISprites() {
        // zoom (mouse wheel)
        msMiddle.setX(0f);
        msMiddle.setY(0.18f * fontRegular.getData().lineHeight);

        // move camera (right click)
        msRight.setX(0f);
        msRight.setY(1.18f * fontRegular.getData().lineHeight);

        // grab upgrade (left click)
        msLeft.setX(0f);
        msLeft.setY(2.18f * fontRegular.getData().lineHeight);

        // inspect upgrade (T key)
        kbT.setX(0f);
        kbT.setY(3.18f * fontRegular.getData().lineHeight);

        // swap to game screen (Esc key)
        kbEsc.setX(0f);
        // kbEsc.y must be set in render() since it changes dynamically with window size
    }

    private Sprite createSprite(String path, float width, float height) {
        Sprite sprite = new Sprite(manager.get(path, Texture.class));
        sprite.setSize(width, height);
        return sprite;
    }

    private String[] setupUpgradeStrings() {
        return new String[]{
                "Fuselage:\nUsed to expand the ship. New upgrades are attached to Fuselage.",
                "Turret:\nFires lasers at enemies and asteroids.",
                "Rocket:\nImproves acceleration and top speed of the ship.",
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

        glyphLayout = new GlyphLayout();
    }


    private void drawValidFuselagePlacements() {
        for (int x = 0; x < model.getGridWidth(); x++) {
            for (int y = 0; y < model.getGridHeight(); y++) {
                CellPosition cp = new CellPosition(y, x);

                if (model.isValidFuselagePosition(cp)) {
                    drawGridSquare(squareGreen, x, y);

                } else if (model.getPlayerShipStructure().hasFuselage(cp)) {
                    drawGridSquare(squareGray, x, y);
                } else {
                    drawGridSquare(squareRed, x, y);
                }
            }
        }
    }

    private void drawValidUpgradePlacements() {

        for (int x = 0; x < model.getGridWidth(); x++) {
            for (int y = 0; y < model.getGridHeight(); y++) {
                CellPosition cp = new CellPosition(y, x);

                if (model.isValidUpgradePosition(cp)) {
                    drawGridSquare(squareGreen, x, y);
                } else if (model.getPlayerShipStructure().hasFuselage(cp)) {
                    drawGridSquare(squareGray, x, y);
                }
            }
        }
    }

    private void drawDefaultGrid() {
        for (int x = 0; x < model.getGridWidth(); x++) {
            for (int y = 0; y < model.getGridHeight(); y++) {

                if (model.getPlayerShipStructure().hasFuselage(new CellPosition(y, x))) {
                    drawGridSquare(squareGray, x, y);
                }
            }
        }
    }

    private void drawShipGrid() {

        if (model.isUpgradeGrabbed() && grabbedItemIsFuselage()) {
            drawValidFuselagePlacements();
            return;
        }

        if (model.isUpgradeGrabbed()) {
            drawValidUpgradePlacements();
            return;
        }

        drawDefaultGrid();

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

        drawShipGrid();

        // draw player's ship
        for (GridCell<Fuselage> cell : model.getPlayerShipStructure()) {
            if (cell.value() == null) {
                continue;
            }
            drawUpgrade(cell.pos());

            if (cell.value().hasUpgrade()) {
                drawUpgradeAtPosition(cell.pos(), cell.value().getUpgrade().getType());
            }
        }

        if (model.isUpgradeGrabbed()) {
            // draw a ghost copy of upgrade if hovering a grid cell
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            unprojectTouchPos(touchPos);
            CellPosition cpGrid = convertMouseToGrid(touchPos.x, touchPos.y);

            if (cellPositionOnGrid(cpGrid) && canPlaceItem(cpGrid)) {
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
        msMiddle.draw(batch);
        fontRegular.draw(batch, "Adjust zoom", fontRegular.getData().lineHeight,
                fontRegular.getData().lineHeight);

        // move camera (right click)
        msRight.draw(batch);
        fontRegular.draw(batch, "Move camera", fontRegular.getData().lineHeight,
                2 * fontRegular.getData().lineHeight);

        // grab upgrade (left click)
        msLeft.draw(batch);
        fontRegular.draw(batch, "Grab upgrade", fontRegular.getData().lineHeight,
                3 * fontRegular.getData().lineHeight);

        // inspect upgrade (T key)
        kbT.draw(batch);
        fontRegular.draw(batch, "Inspect upgrade", fontRegular.getData().lineHeight,
                4 * fontRegular.getData().lineHeight);

        // escape (Esc key)
        kbEsc.setY(viewportUI.getWorldHeight() - 1.15f * fontRegular.getData().lineHeight);
        kbEsc.draw(batch);
        fontRegular.draw(batch, "Change screen", fontRegular.getData().lineHeight,
                viewportUI.getWorldHeight() - 0.33f * fontRegular.getData().lineHeight);

        if (model.isCameraZoomRecently()) {
            float alpha =
                    model.getCameraZoomDeltaTime() < model.getCameraZoomTextFadeCutoffTime() ? 1f
                            : 1f - (float) Math.pow(
                                    (model.getCameraZoomDeltaTime()
                                            - model.getCameraZoomTextFadeCutoffTime()), 2);
            if (alpha > 0) {
                Color fontColor = new Color(1f, 0.47f, 0.55f, alpha);
                fontRegular.setColor(fontColor);
                fontRegular.draw(batch, "Zoom = x" + model.getCurrentZoom(),
                        0.1f, 5 * fontRegular.getData().lineHeight);
            }
        }

        batch.end();

        // draw upgrade description if inspection mode is on
        if (model.upgradeInspectionModeIsActive()) {
            String upgradeDescription = upgradeStrings[model.getInspectedUpgradeIndex()];

            float width = 3f;
            float rectanglePadding = 0.1f;
            glyphLayout.setText(fontRegular, upgradeDescription, Color.WHITE, width, Align.left,
                    true);

            touchPos.set(Gdx.input.getX(), Gdx.input.getY() + cursorHeight);
            viewportUI.unproject(touchPos);

            descriptionRect.setWidth(glyphLayout.width);
            descriptionRect.setHeight(glyphLayout.height);

            shape.setProjectionMatrix(viewportUI.getCamera().combined);
            shape.begin(ShapeType.Filled);
            shape.setColor(Color.DARK_GRAY);
            shape.rect(touchPos.x - rectanglePadding,
                    touchPos.y - rectanglePadding - descriptionRect.height,
                    descriptionRect.width + 2f * rectanglePadding,
                    descriptionRect.height + 2f * rectanglePadding);
            shape.end();

            batch.begin();
            fontRegular.draw(batch, glyphLayout,
                    touchPos.x, touchPos.y);
            batch.end();
        }
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

    private void drawUpgrade(CellPosition cp) {
        upgradeIcons[0].setX(model.getGridOffsetX() + cp.col() + 0.5f * (1f - upgradeIconZoom));
        upgradeIcons[0].setY(model.getGridOffsetY() + cp.row() + 0.5f * (1f - upgradeIconZoom));
        upgradeIcons[0].draw(batch);
    }

    private void drawUpgradeAtPosition(CellPosition cp, UpgradeType type) {

        upgradeIconsMap.get(type).setX(model.getGridOffsetX() + cp.col());
        upgradeIconsMap.get(type).setY(model.getGridOffsetY() + cp.row());
        upgradeIconsMap.get(type).draw(batch);
    }

    private boolean grabbedItemIsFuselage() {
        return model.getGrabbedUpgradeIndex() == 0; // TODO: Make this more robust.
    }

    private boolean canPlaceItem(CellPosition cp) {
        boolean canPlaceFuselage = grabbedItemIsFuselage()
                && model.isValidFuselagePosition(cp);
        boolean canPlaceUpgrade =
                !grabbedItemIsFuselage() && model.isValidUpgradePosition(cp);
        return canPlaceFuselage || canPlaceUpgrade;
    }


    private Vector2 worldToGameCoordinates(float worldX, float worldY) {
        touchPos.set(worldX, worldY);
        unprojectTouchPos(touchPos);
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
     * Updates camera zoom level. Used by controller to adjust view magnification based on user
     * input.
     *
     * @param zoom The new zoom level to apply to camera.
     */
    public void updateCameraZoom(float zoom) {
        ((OrthographicCamera) viewportGame.getCamera()).zoom = zoom;
    }

    /**
     * Updates camera position based on screen offset. Handles camera movement and staying within
     * world bounds.
     *
     * @param offsetX The x offset in screen coordinates.
     * @param offsetY the y offset in screen coordinates.
     */
    public void updateCameraPosition(int offsetX, int offsetY) {
        float cameraX = viewportGame.getScreenWidth() / 2f + offsetX;
        float cameraY = viewportGame.getScreenHeight() / 2f + offsetY;

        touchPos.set(cameraX, cameraY);
        unprojectTouchPos(touchPos);
        clampVector(touchPos, 0f, viewportGame.getWorldWidth(), 0f, viewportGame.getWorldHeight());

        viewportGame.getCamera().position.set(touchPos, 0);
    }

    private void clampVector(Vector2 v, float minX, float maxX, float minY, float maxY) {
        v.x = Math.max(Math.min(v.x, maxX), minX);
        v.y = Math.max(Math.min(v.y, maxY), minY);
    }

    /**
     * Convert screen coordinate to world coordinates. Windows have origin at top-left, game world
     * at bottom-left.
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