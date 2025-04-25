package view.screens;

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
import controller.UpgradeScreenController;
import grid.CellPosition;
import grid.GridCell;
import grid.IGrid;
import java.util.HashMap;
import java.util.List;
import model.ShipComponents.ShipStructure;
import model.ShipComponents.UpgradeStage;
import model.World.StoreItem;
import model.utils.FloatPair;
import view.Palette;
import view.SpaceGame;
import view.bars.UpgradeStageDisplay;
import model.GameStateModel;
import model.UpgradeScreenModel;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.stats.Stat;
import model.ShipComponents.Components.stats.StatModifier;

import java.util.Map;

/**
 * Screen for managing ship upgrades in game. Handles rendering of upgrade grid,
 * options and UI
 * elements.
 */
public class UpgradeScreen extends InputAdapter implements Screen {

    private final SpriteBatch batch;
    private final ShapeRenderer shape;
    private final ScreenViewport viewportGame;
    private final ScreenViewport viewportUI;
    private final AssetManager manager;
    private final UpgradeScreenModel model;
    private final UpgradeScreenController controller;
    private final Vector2 touchPos; // Simplifies converting touch / mouse position in window-coordinates (pixels)
    // to game-coordinates (meters x meters set in viewport)

    private BitmapFont fontRegular;
    private GlyphLayout glyphLayout;
    private Sprite squareRed;
    private Sprite squareGreen;
    private Sprite squareGray;
    private Sprite diamond;
    private final Rectangle descriptionRect;

    // ui sprites:
    private Sprite msLeft;
    private Sprite msMiddle;
    private Sprite msRight;
    private Sprite kbT;
    private Sprite kbEsc;

    private final float upgradeIconZoom = 0.8f;

    private final Map<UpgradeType, Map<UpgradeStage, Sprite>> upgradeSprites = new HashMap<>();
    private final List<StoreItem<UpgradeType>> storeShelf;

    private final int cursorDimentions = 64;

    private UpgradeStageDisplay upgradeStageDisplay;

    /**
     * Creates a new upgrade screen with necessary components for rendering and
     * input handling.
     * Initializes viewports, fonts, sprites and upgrade system.
     *
     * @param game The main game instance providing sprites, and other
     *             resources.
     */
    public UpgradeScreen(final SpaceGame game, final GameStateModel gameStateModel) {
        this.batch = game.getSpriteBatch();
        this.shape = game.getShapeRenderer();
        this.manager = game.getAssetManager();
        this.viewportGame = game.getScreenViewport();
        this.viewportUI = new ScreenViewport();
        this.model = gameStateModel.getUpgradeScreenModel();
        this.controller = new UpgradeScreenController(this, gameStateModel, game);
        this.touchPos = new Vector2();
        this.storeShelf = model.getStoreShelf();
        viewportUI.setUnitsPerPixel(viewportGame.getUnitsPerPixel());
        setupFonts();
        loadSprites();
        setupUISprites();
        setupUpgradeStageDisplay();
        this.model.setUpgradeStageDisplay(upgradeStageDisplay);
        descriptionRect = new Rectangle(0, 0, 0, 0);
    }

    private void loadSprites() {
        squareRed = createSprite("images/upgrade_grid_tile_red.png", 1, 1);
        squareGreen = createSprite("images/upgrade_grid_tile_green.png", 1, 1);
        squareGray = createSprite("images/upgrade_grid_tile_gray.png", 1, 1);

        for (UpgradeType type : UpgradeType.values()) {
            Map<UpgradeStage, Sprite> stageSprites = new HashMap<>();
            String basePath = "images/upgrades/" + type.name().toLowerCase() + "_stage_";
            for (UpgradeStage stage : UpgradeStage.values()) {

                String path = basePath + stage.ordinal() + ".png";

                Sprite sprite = createSprite(path, upgradeIconZoom, upgradeIconZoom);

                stageSprites.put(stage, sprite);
            }
            upgradeSprites.put(type, stageSprites);
        }

        diamond = createSprite("images/space/diamond.png", 1f, 1f);
        float uiIconZoom = fontRegular.getData().lineHeight;

        msLeft = createSprite("images/ui/Mouse_Left_Key_Light.png", uiIconZoom, uiIconZoom);
        msMiddle = createSprite("images/ui/Mouse_Middle_Key_Light.png", uiIconZoom, uiIconZoom);
        msRight = createSprite("images/ui/Mouse_Right_Key_Light.png", uiIconZoom, uiIconZoom);
        kbT = createSprite("images/ui/T_Key_Light.png", uiIconZoom, uiIconZoom);
        kbEsc = createSprite("images/ui/Esc_Key_Light.png", uiIconZoom, uiIconZoom);
    }

    private void setupUISprites() {
        // zoom (mouse wheel)
        msMiddle.setX(0f);
        msMiddle.setY(0.21f * fontRegular.getData().lineHeight);

        // move camera (right click)
        msRight.setX(0f);
        msRight.setY(1.21f * fontRegular.getData().lineHeight);

        // grab upgrade (left click)
        msLeft.setX(0f);
        msLeft.setY(2.21f * fontRegular.getData().lineHeight);

        // inspect upgrade (T key)
        kbT.setX(0f);
        kbT.setY(3.21f * fontRegular.getData().lineHeight);

        // swap to game screen (Esc key)
        kbEsc.setX(0f);
        kbEsc.setY(4.21f * fontRegular.getData().lineHeight);
    }

    private Sprite createSprite(String path, float width, float height) {
        Sprite sprite = new Sprite(manager.get(path, Texture.class));
        sprite.setSize(width, height);
        return sprite;
    }

    private void setupFonts() {
        BitmapFont fontBold = manager.get("fonts/PixelOperatorMono-Bold.ttf", BitmapFont.class);
        fontRegular = manager.get("fonts/PixelOperatorMonoHB.ttf", BitmapFont.class);

        // font are set as [integer]pt, need to scale them to our viewport by ratio of
        // viewport height to screen height in order to use world-unit sized font
        fontBold.setUseIntegerPositions(false);
        fontBold.getData().setScale(viewportGame.getUnitsPerPixel());

        fontRegular.setUseIntegerPositions(false);
        fontRegular.getData().setScale(viewportGame.getUnitsPerPixel());

        glyphLayout = new GlyphLayout();
    }

    private void setupUpgradeStageDisplay() {
        StatModifier max = new StatModifier();
        for (Stat stat : Stat.values()) {
            max.setModifier(stat, 42f);
        }

        upgradeStageDisplay = new UpgradeStageDisplay(max, upgradeSprites, fontRegular, upgradeIconZoom / 2f);
        upgradeStageDisplay.setPosition(new FloatPair(3f, 1f));
        upgradeStageDisplay.setCurrentStats(model.getPlayerStats());
        upgradeStageDisplay.setVisibility(false);
    }

    private void drawValidFuselagePlacements() {
        IGrid<Fuselage> grid = model.getExpandedGrid();
        for (GridCell<Fuselage> cell : grid) {
            Sprite coloredSquare = squareRed;

            if (!grid.isEmptyAt(cell.pos())) {
                coloredSquare = squareGray;
            } else if (ShipStructure.isValidFuselagePosition(grid, cell.pos())) {
                coloredSquare = squareGreen;
            }
            drawGridSquare(coloredSquare, cell);
        }
    }

    private void drawValidUpgradePlacements() {
        IGrid<Fuselage> grid = model.getExpandedGrid();

        for (GridCell<Fuselage> cell : grid) {
            Sprite coloredGrid = squareGray;

            if (grid.isEmptyAt(cell.pos())) {
                continue;
            }
            if (ShipStructure.isValidUpgradePosition(grid, cell.pos())) {
                coloredGrid = squareGreen;
            }
            drawGridSquare(coloredGrid, cell);
        }
    }

    private void drawDefaultGrid() {
        IGrid<Fuselage> grid = model.getExpandedGrid();

        for (GridCell<Fuselage> cell : grid) {
            if (!grid.isEmptyAt(cell.pos())) {
                drawGridSquare(squareGray, cell);
            }
        }
    }

    private void drawShipGrid() {
        if (!model.isUpgradeGrabbed()) {
            drawDefaultGrid();
        } else if (grabbedItemIsFuselage()) {
            drawValidFuselagePlacements();
        } else {
            drawValidUpgradePlacements();
        }
    }

    private void drawShip() {
        for (GridCell<Fuselage> cell : model.getExpandedGrid()) {
            if (cell.value() == null) {
                continue;
            }
            CellPosition pos = cell.pos();
            drawFuselage(pos, cell.value().getStage());

            if (cell.value().hasUpgrade()) {
                UpgradeType type = cell.value().getUpgrade().getType();
                drawUpgrade(pos, type, cell.value().getUpgrade().getStage());
            }
        }
    }

    @Override
    public void render(float delta) {
        model.update(delta);
        if (model.offsetsMustBeUpdated) {
            model.updateOffsets(viewportGame.getWorldWidth(), viewportGame.getWorldHeight());
            model.offsetsMustBeUpdated = false;
        }

        ScreenUtils.clear(Palette.MUTED_GREEN);
        viewportGame.apply(false);
        batch.setProjectionMatrix(viewportGame.getCamera().combined);
        batch.begin();

        // Draw upgrade options at top of screen
        for (int x = 0; x < model.getNumUpgradeOptions(); x++) {
            drawUpgradeSquare(x);
            drawUpgradePrice(x);
            drawUpgradeShade(x);
        }

        drawShipGrid();
        drawShip();

        if (model.isUpgradeGrabbed()) {
            // draw a ghost copy of upgrade if hovering a grid cell
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            unprojectTouchPos(touchPos);
            CellPosition cpGrid = controller.convertMouseToGrid(touchPos.x, touchPos.y);

            Sprite upgrade = getSpriteFromIndex(model.getGrabbedUpgradeIndex());

            if (controller.cellPositionOnGrid(cpGrid) && canPlaceItem(cpGrid)) {

                upgrade.setX(model.getGridOffsetX() + cpGrid.col() + 0.5f * (1f - upgradeIconZoom));
                upgrade.setY(model.getGridOffsetY() + cpGrid.row() + 0.5f * (1f - upgradeIconZoom));
                upgrade.draw(batch, 0.5f);
            }

            // draw held upgrade
            Vector2 pos = worldToGameCoordinates(model.getDragX(), model.getDragY());
            upgrade.setX(pos.x - 0.5f * upgradeIconZoom);
            upgrade.setY(pos.y - 0.5f * upgradeIconZoom);
            upgrade.draw(batch);
        }

        // draw cell highlight if a cell has been selected
        if (model.getShowCellHighlight()) {
            drawGridHighlight(model.getCellHighlightPosition());
        }

        batch.end();

        if (upgradeStageDisplay.getVisibility()) {
            shape.setProjectionMatrix(viewportGame.getCamera().combined);
            upgradeStageDisplay.render(batch, shape, true);
        }

        // draw UI elements
        viewportUI.apply(true);
        batch.setProjectionMatrix(viewportUI.getCamera().combined);
        batch.begin();

        fontRegular.setColor(Palette.WHITE);

        // zoom (mouse wheel)
        msMiddle.draw(batch);
        fontRegular.draw(batch, "Adjust zoom", fontRegular.getData().lineHeight,
                fontRegular.getData().lineHeight);

        // move camera (right click)
        msRight.draw(batch);
        fontRegular.draw(batch, "Move camera", fontRegular.getData().lineHeight,
                2f * fontRegular.getData().lineHeight);

        // grab upgrade (left click)
        msLeft.draw(batch);
        fontRegular.draw(batch, "Grab upgrade", fontRegular.getData().lineHeight,
                3f * fontRegular.getData().lineHeight);

        // inspect upgrade (T key)
        kbT.draw(batch);
        fontRegular.draw(batch, "Inspect upgrade", fontRegular.getData().lineHeight,
                4f * fontRegular.getData().lineHeight);

        // escape (Esc key)
        kbEsc.draw(batch);
        fontRegular.draw(batch, "Change screen", fontRegular.getData().lineHeight,
                5f * fontRegular.getData().lineHeight);

        // Resources
        diamond.setY(viewportUI.getWorldHeight() - 1.06f * diamond.getHeight());
        diamond.setX(-0.15f * diamond.getWidth());
        diamond.draw(batch);
        fontRegular.draw(batch, String.valueOf(model.getPlayerResources()), diamond.getX() + diamond.getWidth(),
                diamond.getY() + 0.45f * diamond.getHeight() + 0.5f * fontRegular.getLineHeight());

        if (model.isCameraZoomRecently()) {
            float alpha = model.getCameraZoomDeltaTime() < model.getCameraZoomTextFadeCutoffTime() ? 1f
                    : 1f - (float) Math.pow(
                            (model.getCameraZoomDeltaTime()
                                    - model.getCameraZoomTextFadeCutoffTime()),
                            2);
            if (alpha > 0) {
                Color fontColor = Palette.FONT_RED;
                fontColor.a = alpha;
                fontRegular.setColor(fontColor);
                fontRegular.draw(batch, "Zoom = x" + model.getCurrentZoom(),
                        0.1f, 6f * fontRegular.getData().lineHeight);
            }
        }
        batch.end();

        // draw upgrade description if inspection mode is on
        if (model.upgradeInspectionModeIsActive()) {
            int upgradeIndex = model.getInspectedUpgradeIndex();
            String upgradeDescription = storeShelf.get(upgradeIndex).description();

            float width = 5f;
            float rectanglePadding = 0.1f;
            glyphLayout.setText(fontRegular, upgradeDescription, Palette.WHITE, width, Align.left,
                    true);

            touchPos.set(Gdx.input.getX(), Gdx.input.getY() + cursorDimentions);
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
        float xPos = model.getUpgradeOffsetX() + x;
        float yPos = model.getUpgradeOffsetY();

        squareGray.setX(xPos);
        squareGray.setY(yPos);
        squareGray.draw(batch);

        Sprite upgrade = getSpriteFromIndex(x);
        upgrade.setX(xPos + (1f - upgradeIconZoom) / 2f);
        upgrade.setY(yPos + (1f - upgradeIconZoom) / 2f);
        upgrade.draw(batch);
    }

    private void drawUpgradePrice(int x) {

        float xPos = model.getUpgradeOffsetX() + x;
        float yPos = model.getUpgradeOffsetY();

        int price = storeShelf.get(x).price();

        fontRegular.setColor(Palette.WHITE);
        if (model.getPlayerResources() < price) {
            fontRegular.setColor(Palette.MUTED_RED_LIGHT);
        }

        glyphLayout.setText(fontRegular, String.valueOf(price));
        float fontOffsetX = (squareGray.getWidth() - glyphLayout.width) / 2f;
        float fontOffsetY = squareGray.getHeight() + fontRegular.getLineHeight();

        fontRegular.draw(batch, String.valueOf(price),
                xPos + fontOffsetX,
                yPos + fontOffsetY);

    }

    private void drawUpgradeShade(int x) {
        boolean canAfford = storeShelf.get(x).price() <= model.getPlayerResources();
        if (!canAfford) {
            squareRed.setX(model.getUpgradeOffsetX() + x);
            squareRed.setY(model.getUpgradeOffsetY());
            squareRed.draw(batch, 0.5f);
        }
    }

    private void drawGridHighlight(CellPosition cp) {
        squareGreen.setX(model.getGridOffsetX() + cp.col());
        squareGreen.setY(model.getGridOffsetY() + cp.row());
        squareGreen.draw(batch, 0.5f);
    }

    private void drawGridSquare(Sprite squareSprite, int x, int y) {
        squareSprite.setX(model.getGridOffsetX() + x);
        squareSprite.setY(model.getGridOffsetY() + y);
        squareSprite.draw(batch);
    }

    private void drawGridSquare(Sprite squareSprite, GridCell<Fuselage> cell) {
        drawGridSquare(squareSprite, cell.pos().col(), cell.pos().row());
    }

    private void drawFuselage(CellPosition cp, UpgradeStage stage) {
        drawUpgrade(cp, UpgradeType.FUSELAGE, stage);
    }

    private void drawUpgrade(CellPosition cp, UpgradeType type, UpgradeStage stage) {
        Sprite upgrade = upgradeSprites.get(type).get(stage);
        upgrade.setX(model.getGridOffsetX() + cp.col() + 0.5f * (1f - upgradeIconZoom));
        upgrade.setY(model.getGridOffsetY() + cp.row() + 0.5f * (1f - upgradeIconZoom));
        upgrade.draw(batch);
    }

    /**
     * Checks if the currently grabbed item is a fuselage.
     *
     * @return {@code true} if the grabbed upgrade index is {@code 0}, otherwise
     *         {@code false}.
     */
    public boolean grabbedItemIsFuselage() {
        return storeShelf.get(model.getGrabbedUpgradeIndex()).item() == UpgradeType.FUSELAGE;
    }

    /**
     * Retrieves the {@code UpgradeType} associated with a given index.
     *
     * @param index the index to look up
     * @return the corresponding {@code UpgradeType}, or {@code null} if no match is
     *         found
     */
    public UpgradeType getUpgradeTypeFromIndex(int index) {
        return storeShelf.get(index).item();
    }

    public Sprite getSpriteFromIndex(int index) {
        return upgradeSprites.get(getUpgradeTypeFromIndex(index)).get(UpgradeStage.ZERO);
    }

    private boolean canPlaceItem(CellPosition cp) {
        IGrid<Fuselage> grid = model.getExpandedGrid();
        boolean canPlaceFuselage = grabbedItemIsFuselage() && ShipStructure.isValidFuselagePosition(grid, cp);
        boolean canPlaceUpgrade = !grabbedItemIsFuselage() && ShipStructure.isValidUpgradePosition(grid, cp);
        return canPlaceFuselage || canPlaceUpgrade;
    }

    private Vector2 worldToGameCoordinates(float worldX, float worldY) {
        touchPos.set(worldX, worldY);
        unprojectTouchPos(touchPos);
        return touchPos;
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
        unprojectTouchPos(touchPos);
        clampVector(touchPos, model.getGridOffsetX(),
                viewportGame.getWorldWidth() - model.getGridOffsetX(),
                model.getGridOffsetY(),
                viewportGame.getWorldHeight() - model.getGridOffsetY());

        viewportGame.getCamera().position.set(touchPos, 0);
    }

    private void resetCameraPositionAfterMinimize() {
        touchPos.set(viewportGame.getWorldWidth() / 2f, viewportGame.getWorldHeight() / 2f);
        clampVector(touchPos, 0f, viewportGame.getWorldWidth(), 0f, viewportGame.getWorldHeight());
        viewportGame.getCamera().position.set(touchPos, 0);
    }

    private void clampVector(Vector2 v, float minX, float maxX, float minY, float maxY) {
        v.x = Math.max(Math.min(v.x, maxX), minX);
        v.y = Math.max(Math.min(v.y, maxY), minY);
    }

    /**
     * Convert screen coordinate to world coordinates.
     * Window has origin at top-left, game world at bottom-left.
     *
     * @param pos The position vector to be converted.
     */
    public void unprojectTouchPos(Vector2 pos) {
        viewportGame.unproject(pos);
    }

    public UpgradeStageDisplay getUpgradeStageDisplay() {
        return upgradeStageDisplay;
    }

    @Override
    public void resize(int width, int height) {
        int oldWidth = viewportGame.getScreenWidth();
        int oldHeight = viewportGame.getScreenHeight();
        viewportGame.update(width, height, false);

        if (viewportGame.getScreenWidth() == 0 || viewportGame.getScreenHeight() == 0) {
            // if here, then the window was minimized -> must reset camera
            resetCameraPositionAfterMinimize();
        } else { // if here, then the window size was just adjusted by the user
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
        model.setOldCameraZoom(((OrthographicCamera) viewportGame.getCamera()).zoom);
        Gdx.input.setInputProcessor(controller);
        controller.reset();
        updateCameraZoom(model.getCurrentZoom());
        viewportGame.apply(true);
        viewportUI.apply(true);
        model.setUpgradeStageDisplay(upgradeStageDisplay);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        updateCameraZoom(model.getOldCameraZoom());
        model.setCellHighlight(false, null);
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