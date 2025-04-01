package view;

import app.TestSpaceGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.List;
import model.GameStateModel;
import model.constants.GameState;
import model.utils.MenuButton;

public class StartGameScreen implements Screen, InputProcessor {

    private final TestSpaceGame game;
    private final GameStateModel gameStateModel;
    private final SpriteBatch spriteBatch;
    private final ScreenViewport viewport;
    private final OrthographicCamera camera;
    private final AssetManager assetManager;
    private final Sound blipSound;

    private final BitmapFont titleFont;
    private final BitmapFont regularFont;

    private TextureRegion[] background;
    private float[] backgroundOffsets;
    private final float backgroundSpeed = 1.5f;

    private final String GAME_TITLE = "SPACE ADVENTURE";
    private final Color TITLE_COLOR = new Color(0.9f, 0.9f, 1f, 1f);
    private final Color BUTTON_COLOR = Color.WHITE;
    private final Color BUTTON_HOVER_COLOR = new Color(0.8f, 0.8f, 1f, 1f);

    private final List<MenuButton> menuButtons = new ArrayList<>();
    private int selectedButtonIndex = 0;
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
        this.camera = (OrthographicCamera) viewport.getCamera();

        this.titleFont = assetManager.get("fonts/AGENCYB.ttf", BitmapFont.class);
        this.regularFont = assetManager.get("fonts/AGENCYR.ttf", BitmapFont.class);
        this.blipSound = assetManager.get("audio/blipp.ogg", Sound.class);
        this.glyphLayout = new GlyphLayout();

        setupBackground();
        setupFonts();

    }

    private void setupFonts() {
        titleFont.setUseIntegerPositions(false);
        titleFont.getData().setScale(viewport.getUnitsPerPixel());

        regularFont.setUseIntegerPositions(false);
        regularFont.getData().setScale(viewport.getUnitsPerPixel());

        System.out.println("Font scale set to: " + viewport.getUnitsPerPixel());
    }

    private void setupBackground() {
        this.background = new TextureRegion[3];

        background[0] = new TextureRegion(
            assetManager.get("images/space/background/bkgd_1.png", Texture.class));
        background[1] = new TextureRegion(
            assetManager.get("images/space/background/bkgd_3.png", Texture.class));
        background[2] = new TextureRegion(
            assetManager.get("images/space/background/bkgd_7.png", Texture.class));

        this.backgroundOffsets = new float[background.length];

        for (int i = 0; i < background.length; i++) {
            background[i].getTexture()
                .setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            backgroundOffsets[i] = 0;
        }

    }

    private void setupMenu() {

        float worldHeight = viewport.getWorldHeight();
        float worldCenterX = viewport.getWorldWidth() / 2f;

        //position buttons in middle of screen
        float startY = worldHeight * 0.5f;
        float spacing = BUTTON_PADDING + BUTTON_PADDING;

        //create menu buttons
        menuButtons.add(new MenuButton("START GAME", worldCenterX, startY));
        menuButtons.add(
            new MenuButton("OPTIONS", worldCenterX, startY - spacing));
        menuButtons.add(
            new MenuButton("EXIT", worldCenterX, startY - 2 * spacing));

        menuInitialized = true;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        gameStateModel.changeState(GameState.START_GAME);
        setupMenu();
    }

    @Override
    public void render(float delta) {
        if (!menuInitialized) {
            setupMenu();
        }

        stateTime += delta;
        pulseTime += delta;

        updateBackground(delta);

        ScreenUtils.clear(Color.BLACK);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();

        // Draw background
        for (TextureRegion textureRegion : background) {
            spriteBatch.draw(textureRegion, 0, 0, viewport.getWorldWidth(),
                viewport.getWorldHeight());
        }

        // Draw title
        float titlePulseScale = 1.0f + 0.05f * (float) Math.sin(pulseTime * 2.0f);
        titleFont.getData().setScale(viewport.getUnitsPerPixel() * titlePulseScale);

        // Use the glyphLayout to measure the width of the title text for centering
        glyphLayout.setText(titleFont, GAME_TITLE);
        float titleX = (viewport.getWorldWidth() - glyphLayout.width) / 2f;
        float titleY = viewport.getWorldHeight() * 0.7f + glyphLayout.height / 2f;

        titleFont.setColor(TITLE_COLOR);
        titleFont.draw(spriteBatch, GAME_TITLE, titleX, titleY);

        // Reset to base scale
        titleFont.getData().setScale(viewport.getUnitsPerPixel());

        // Draw buttons
        renderButtons(spriteBatch);

        spriteBatch.end();
    }

    private void updateBackground(float delta) {
        for (int i = 0; i < background.length; i++) {
            backgroundOffsets[i] += delta * backgroundSpeed * (0.2f + i * 0.1f);
            backgroundOffsets[i] %= 1.0f;
            background[i].scroll(0, backgroundOffsets[i]);
        }
    }

    private void renderButtons(SpriteBatch spriteBatch) {
        if (!menuInitialized) {
            return;
        }

        for (int i = 0; i < menuButtons.size(); i++) {
            MenuButton button = menuButtons.get(i);

            //set color
            if (i == selectedButtonIndex) {
                float pulse = 1.0f + 0.1f * (float) Math.sin(stateTime * 5.0f);
                regularFont.getData().setScale(viewport.getUnitsPerPixel() * pulse);
                regularFont.setColor(BUTTON_HOVER_COLOR);
            } else {
                regularFont.getData().setScale(viewport.getUnitsPerPixel());
                regularFont.setColor(BUTTON_COLOR);
            }

            String buttonText = button.getText();
            glyphLayout.setText(regularFont, buttonText);

            // Calculate centered position for text
            float textX = button.getX() - glyphLayout.width / 2f;
            float textY = button.getY() + glyphLayout.height / 2f;

            // Update button bounds for click detection
            button.setBounds(new Rectangle(
                    button.getX() - BUTTON_WIDTH / 2f,
                    button.getY() - BUTTON_HEIGHT / 2f,
                    BUTTON_WIDTH,
                    BUTTON_HEIGHT
                )
            );

            // Draw button text
            regularFont.draw(spriteBatch, buttonText, textX, textY);
        }

        // Reset font scale
        regularFont.getData().setScale(viewport.getUnitsPerPixel());
    }

    private void activateButton(int index) {
        blipSound.play(0.7f);

        switch (index) {
            case 0: //start game
                startGame();
                break;
            case 1: //EXIT
                showOptionsScreen();
                break;
            case 2:
                Gdx.app.exit();
                break;
        }
    }

    private void startGame() {
        gameStateModel.startNewGame();
        game.setSpaceScreen();
    }

    private void showOptionsScreen() {
        //TODO: Lager vi en?
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        setupFonts();

        // Recalculate button positions
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

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                selectedButtonIndex = Math.max(0, selectedButtonIndex - 1);
                blipSound.play(0.4f);
                return true;
            case Input.Keys.DOWN:
                selectedButtonIndex = Math.min(menuButtons.size() - 1, selectedButtonIndex + 1);
                blipSound.play(0.4f);
                return true;
            case Input.Keys.ENTER:
            case Input.Keys.SPACE:
                activateButton(selectedButtonIndex);
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to world coordinates
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

        // Check if any button was clicked
        for (int i = 0; i < menuButtons.size(); i++) {
            if (menuButtons.get(i).getBounds().contains(worldCoords.x, worldCoords.y)) {
                selectedButtonIndex = i;
                activateButton(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Convert screen coordinates to world coordinates
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

        // Check if mouse is over any button
        for (int i = 0; i < menuButtons.size(); i++) {
            if (menuButtons.get(i).getBounds().contains(worldCoords.x, worldCoords.y)) {
                if (selectedButtonIndex != i) {
                    selectedButtonIndex = i;
                    blipSound.play(0.2f);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}

