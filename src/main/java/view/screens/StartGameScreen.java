package view.screens;

import app.TestSpaceGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import controller.StartScreenController;
import java.util.ArrayList;
import java.util.List;
import model.GameStateModel;
import model.constants.GameState;
import model.utils.MenuButton;
import view.Palette;

public class StartGameScreen implements Screen {

    private final TestSpaceGame game;
    private final GameStateModel gameStateModel;
    private final SpriteBatch spriteBatch;
    private final ScreenViewport viewport;
    private final ScreenViewport backgroundViewport;
    private final OrthographicCamera camera;
    private final AssetManager assetManager;
    private final Sound blipSound;
    private final StartScreenController controller;

    private final BitmapFont titleFont;
    private final BitmapFont regularFont;

    private TextureRegion[] background;
    private float[] backgroundParallax;
    private float[] backgroundDrift;

    private final Vector2 mousePosition = new Vector2();
    private final Vector2 lastMousePosition = new Vector2();
    private final float mouseInfluence = 2.0f;

    private final String GAME_TITLE = "SPACE ADVENTURE";
    private final Color TITLE_COLOR = Palette.TITLE_FONT_COLOR;
    private final Color BUTTON_COLOR = Color.WHITE;
    private final Color BUTTON_HOVER_COLOR = Palette.TITLE_FONT_HIGHLIGHT_COLOR;

    private final List<MenuButton> menuButtons = new ArrayList<>();
    private final float BUTTON_WIDTH = 4f;
    private final float BUTTON_HEIGHT = 1f;
    private final float BUTTON_PADDING = 0.5f;

    private boolean menuInitialized = false;

    private float stateTime = 0f;
    private float pulseTime = 0f;

    private final GlyphLayout glyphLayout;

    public StartGameScreen(TestSpaceGame testSpaceGame, GameStateModel gameStateModel) {
        this.game = testSpaceGame;
        this.gameStateModel = gameStateModel;
        this.spriteBatch = game.getSpriteBatch();
        this.assetManager = game.getAssetManager();
        this.viewport = game.getScreenViewport();
        this.backgroundViewport = new ScreenViewport();
        this.backgroundViewport.setUnitsPerPixel(viewport.getUnitsPerPixel());
        this.camera = (OrthographicCamera) viewport.getCamera();

        this.titleFont = assetManager.get("fonts/AGENCYB.ttf", BitmapFont.class);
        this.regularFont = assetManager.get("fonts/AGENCYR.ttf", BitmapFont.class);
        this.blipSound = assetManager.get("audio/blipp.ogg", Sound.class);
        this.glyphLayout = new GlyphLayout();

        this.controller = new StartScreenController(this, gameStateModel, game);

        setupBackground();
        setupFonts();

        mousePosition.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        lastMousePosition.set(mousePosition);
    }

    private void setupFonts() {
        titleFont.setUseIntegerPositions(false);
        titleFont.getData().setScale(viewport.getUnitsPerPixel());

        regularFont.setUseIntegerPositions(false);
        regularFont.getData().setScale(viewport.getUnitsPerPixel());

    }

    /**
     * Borrowed in its entirety from the SpaceScreen. TODO: Maybe refactor into a
     * setup background
     * class?
     */
    private void setupBackground() {
        this.background = new TextureRegion[6];

        background[0] = new TextureRegion(
                assetManager.get("images/space/background/bkgd_1.png", Texture.class));
        background[1] = new TextureRegion(
                assetManager.get("images/space/background/bkgd_2.png", Texture.class));
        background[2] = new TextureRegion(
                assetManager.get("images/space/background/bkgd_3.png", Texture.class));
        background[3] = new TextureRegion(
                assetManager.get("images/space/background/bkgd_4.png", Texture.class));
        background[4] = new TextureRegion(
                assetManager.get("images/space/background/bkgd_6.png", Texture.class));
        background[5] = new TextureRegion(
                assetManager.get("images/space/background/bkgd_7.png", Texture.class));

        this.backgroundParallax = new float[background.length];
        this.backgroundDrift = new float[background.length];

        int driftOffset = 4;

        for (int i = 0; i < background.length; i++) {
            background[i].getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            backgroundParallax[i] = 0.005f + ((float) Math.pow(i, 1.4)) / 1000f;
            if (i < driftOffset) {
                backgroundDrift[i] = 0f;
            } else {
                backgroundDrift[i] = backgroundParallax[i - driftOffset];
            }
        }
    }

    private void setupMenu() {

        float worldHeight = viewport.getWorldHeight();
        float worldCenterX = viewport.getWorldWidth() / 2f;

        // position buttons in middle of screen
        float startY = worldHeight * 0.5f;
        float spacing = BUTTON_PADDING + BUTTON_PADDING;

        // create menu buttons
        menuButtons.clear();
        menuButtons.add(new MenuButton("START GAME", worldCenterX, startY));
        menuButtons.add(
                new MenuButton("OPTIONS", worldCenterX, startY - spacing));
        menuButtons.add(
                new MenuButton("EXIT", worldCenterX, startY - 2 * spacing));

        menuInitialized = true;
    }

    public void playBlipSound(float volume) {
        if (blipSound != null) {
            blipSound.play(volume);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
        gameStateModel.changeState(GameState.START_GAME);
        setupMenu();

        // reset mouse position
        mousePosition.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        lastMousePosition.set(mousePosition);
    }

    @Override
    public void render(float delta) {
        if (!menuInitialized) {
            setupMenu();
        }

        stateTime += delta;
        pulseTime += delta;

        // update mouse position for background parallax
        updateMousePosition();
        updateBackground(delta);

        ScreenUtils.clear(Color.BLACK);

        // Draw background with separate viewport
        backgroundViewport.apply();
        spriteBatch.setProjectionMatrix(backgroundViewport.getCamera().combined);
        spriteBatch.begin();
        for (TextureRegion layer : background) {
            spriteBatch.draw(layer, 0, 0, backgroundViewport.getWorldWidth(),
                    backgroundViewport.getWorldHeight());
        }
        spriteBatch.end();

        // Switch to main viewport for UI elements
        viewport.apply();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // draw title
        float titlePulseScale = 1.0f + 0.05f * (float) Math.sin(pulseTime * 2.0f);
        titleFont.getData().setScale(viewport.getUnitsPerPixel() * titlePulseScale);

        // use the glyphLayout to measure the width of the title text for centering
        glyphLayout.setText(titleFont, GAME_TITLE);
        float titleX = (viewport.getWorldWidth() - glyphLayout.width) / 2f;
        float titleY = viewport.getWorldHeight() * 0.7f + glyphLayout.height / 2f;

        titleFont.setColor(TITLE_COLOR);
        titleFont.draw(spriteBatch, GAME_TITLE, titleX, titleY);

        // reset to base scale
        titleFont.getData().setScale(viewport.getUnitsPerPixel());

        // draw buttons
        renderButtons(spriteBatch);

        spriteBatch.end();

        controller.update(delta);
    }

    private void updateMousePosition() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        lastMousePosition.set(mousePosition);

        float smoothing = 0.05f;
        mousePosition.x += (mouseX - mousePosition.x) * smoothing;
        mousePosition.y += (mouseY - mousePosition.y) * smoothing;
    }

    private void updateBackground(float delta) {
        float screenCenterX = Gdx.graphics.getWidth() / 2f;
        float screenCenterY = Gdx.graphics.getHeight() / 2f;

        float relativeX = (mousePosition.x - screenCenterX) / screenCenterX;
        float relativeY = (mousePosition.y - screenCenterY) / screenCenterY;

        Vector2 mouseVelocity = new Vector2(
                relativeX * mouseInfluence,
                relativeY * mouseInfluence);

        // update each background layer
        for (int i = 0; i < background.length; i++) {
            float parallax = backgroundParallax[i];
            float drift = backgroundDrift[i];

            // scrollX is negative when mouse is to the right
            // scrollY is positive when mouse is to the bottom
            background[i].scroll(
                    delta * (drift - parallax * mouseVelocity.x),
                    delta * (drift + parallax * mouseVelocity.y));
        }
    }

    private void renderButtons(SpriteBatch spriteBatch) {
        if (!menuInitialized) {
            return;
        }

        for (int i = 0; i < menuButtons.size(); i++) {
            MenuButton button = menuButtons.get(i);

            // set color
            if (i == gameStateModel.getSelectedButtonIndex()) {
                float pulse = 1.0f + 0.1f * (float) Math.sin(stateTime * 5.0f);
                regularFont.getData().setScale(viewport.getUnitsPerPixel() * pulse);
                regularFont.setColor(BUTTON_HOVER_COLOR);
            } else {
                regularFont.getData().setScale(viewport.getUnitsPerPixel());
                regularFont.setColor(BUTTON_COLOR);
            }

            String buttonText = button.getText();
            glyphLayout.setText(regularFont, buttonText);

            // calculate centered position for text
            float textX = button.getX() - glyphLayout.width / 2f;
            float textY = button.getY() + glyphLayout.height / 2f;

            // update button bounds for click detection
            button.setBounds(new Rectangle(
                    button.getX() - BUTTON_WIDTH / 2f,
                    button.getY() - BUTTON_HEIGHT / 2f,
                    BUTTON_WIDTH,
                    BUTTON_HEIGHT));

            // draw button text
            regularFont.draw(spriteBatch, buttonText, textX, textY);
        }

        // reset font scale
        regularFont.getData().setScale(viewport.getUnitsPerPixel());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        // also update background
        backgroundViewport.update(width, height, true);

        setupFonts();

        // recalculate button positions
        if (menuInitialized) {
            float worldCenterX = viewport.getWorldWidth() / 2f;
            float startY = viewport.getWorldHeight() * 0.5f;
            float spacing = BUTTON_HEIGHT + BUTTON_PADDING;

            for (int i = 0; i < menuButtons.size(); i++) {
                MenuButton button = menuButtons.get(i);
                button.setX(worldCenterX);
                button.setY(startY - i * spacing);
            }
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);

    }

    @Override
    public void dispose() {

    }

    public List<MenuButton> getMenuButtons() {
        return menuButtons;
    }

    public Vector3 unprojectScreenCoords(int screenX, int screenY) {
        Vector3 worldCoords = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoords);
        return worldCoords;
    }
}
