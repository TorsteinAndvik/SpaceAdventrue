package view.screens;

import app.TestSpaceGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import controller.OptionsScreenController;
import java.util.ArrayList;
import java.util.List;
import model.GameStateModel;
import model.constants.GameState;
import model.utils.MenuButton;
import view.Palette;

public class OptionsScreen implements Screen {

    private final TestSpaceGame game;
    private final GameStateModel gameStateModel;
    private final SpriteBatch spriteBatch;
    private final ScreenViewport viewport;
    private final OrthographicCamera camera;
    private final AssetManager assetManager;
    private final OptionsScreenController controller;

    private final BitmapFont titleFont;
    private final BitmapFont regularFont;

    private final Color TITLE_COLOR = Palette.TITLE_FONT_COLOR;
    private final Color BUTTON_COLOR = Color.WHITE;
    private final Color BUTTON_HOVER_COLOR = Palette.TITLE_FONT_HIGHLIGHT_COLOR;
    private final String SCREEN_TITLE = "OPTIONS";

    private final List<MenuButton> optionButtons = new ArrayList<>();
    private final float BUTTON_WIDTH = 8f;
    private final float BUTTON_HEIGHT = 1f;
    private final float BUTTON_PADDING = 0.5f;

    private boolean menuInitialized = false;
    private float stateTime = 0f;
    private float pulseTime = 0f;
    private final GlyphLayout glyphLayout;

    //controls
    private boolean showingControls = false;
    private MenuButton controlsBackButton;
    private List<String> controlCategories = new ArrayList<>();
    private List<String> controlKeys = new ArrayList<>();
    private List<String> controlDescriptions = new ArrayList<>();
    private Runnable backButtonAction;

    public OptionsScreen(TestSpaceGame game, GameStateModel gameStateModel) {
        this.game = game;
        this.gameStateModel = gameStateModel;
        this.spriteBatch = game.getSpriteBatch();
        this.assetManager = game.getAssetManager();
        this.viewport = game.getScreenViewport();
        this.camera = (OrthographicCamera) viewport.getCamera();

        this.titleFont = assetManager.get("fonts/PixelOperatorMono-Bold.ttf", BitmapFont.class);
        this.regularFont = assetManager.get("fonts/PixelOperatorMonoHB.ttf", BitmapFont.class);
        this.glyphLayout = new GlyphLayout();

        this.controller = new OptionsScreenController(this, gameStateModel, game);

        setupFonts();
    }

    public void showControlsScreen() {
        showingControls = true;
        controlCategories.clear();
        controlKeys.clear();
        controlDescriptions.clear();
    }

    public void addControlCategory(String category) {
        controlCategories.add(category);
    }

    public void addControlDescription(String key, String description) {
        controlKeys.add(key);
        controlDescriptions.add(description);
    }

    public void addControlsBackButton(String text, Runnable action) {
        float worldHeight = viewport.getWorldHeight();
        float worldCenterX = viewport.getWorldWidth() / 2;

        controlsBackButton = new MenuButton(text, worldCenterX, worldHeight * 0.15f);
        backButtonAction = action;
    }

    public void resetToOptionsMenu() {
        showingControls = false;
        setupMenu();
    }

    private void setupMenu() {
        float worldHeight = viewport.getWorldHeight();
        float worldCenterX = viewport.getWorldWidth() / 2f;

        float startY = worldHeight * 0.5f;
        float spacing = BUTTON_HEIGHT + BUTTON_PADDING;

        optionButtons.clear();
        optionButtons.add(
            new MenuButton("SOUND: " + (game.getSoundManager().isSoundEnabled() ? "ON" : "OFF"),
                worldCenterX, startY));
        optionButtons.add(
            new MenuButton("MUSIC: " + (game.getMusicManager().isMusicEnabled() ? "ON" : " OFF"),
                worldCenterX, startY - spacing));
        optionButtons.add(new MenuButton("CONTROLS", worldCenterX, startY - 2 * spacing));
        optionButtons.add(new MenuButton("BACK", worldCenterX, startY - 3 * spacing));
    }

    private void setupFonts() {
        titleFont.setUseIntegerPositions(false);
        titleFont.getData().setScale(viewport.getUnitsPerPixel());

        regularFont.setUseIntegerPositions(false);
        regularFont.getData().setScale(viewport.getUnitsPerPixel());

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
        gameStateModel.changeState(GameState.OPTIONS);
        setupMenu();
        menuInitialized = true;
    }

    @Override
    public void render(float delta) {
        if (!menuInitialized) {
            setupMenu();
            menuInitialized = true;
        }
        stateTime += delta;
        pulseTime += delta;
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // draw title
        float titlePulseScale = 1.0f + 0.05f * (float) Math.sin(pulseTime * 2.0f);
        titleFont.getData().setScale(viewport.getUnitsPerPixel() * titlePulseScale);

        glyphLayout.setText(titleFont, SCREEN_TITLE);
        float titleX = (viewport.getWorldWidth() - glyphLayout.width) / 2f;
        float titleY = viewport.getWorldHeight() * 0.7f + glyphLayout.height / 2f;

        titleFont.setColor(TITLE_COLOR);
        titleFont.draw(spriteBatch, SCREEN_TITLE, titleX, titleY);

        titleFont.getData().setScale(viewport.getUnitsPerPixel());

        renderButtons(spriteBatch);

        if (showingControls) {
            renderControlsScreen(spriteBatch);
        } else {
            renderButtons(spriteBatch);
        }
        spriteBatch.end();
        controller.update(delta);
    }

    private void renderControlsScreen(SpriteBatch spriteBatch) {
        float startY = viewport.getWorldHeight() * 0.8f;
        float categoryStartX = viewport.getWorldWidth() * 0.1f;
        float controlStartX = viewport.getWorldWidth() * 0.2f;
        float lineHeight = regularFont.getLineHeight() * 1.5f;

        // draw Controls header
        titleFont.setColor(TITLE_COLOR);
        glyphLayout.setText(titleFont, "CONTROLS");
        float titleX = (viewport.getWorldWidth() - glyphLayout.width) / 2f;
        float titleY = viewport.getWorldHeight() * 0.9f + glyphLayout.height / 2f;
        titleFont.draw(spriteBatch, "CONTROLS", titleX, titleY);

        // track position for drawing
        float currentY = startY;
        int controlIndex = 0;

        // draw category and controls
        for (String category : controlCategories) {
            // draw header for each category
            regularFont.setColor(BUTTON_HOVER_COLOR);
            regularFont.getData().setScale(viewport.getUnitsPerPixel() * 1.1f);
            regularFont.draw(spriteBatch, category, categoryStartX, currentY);
            currentY -= lineHeight;

            // count controls to draw for a given category
            int controlsInCategory = 0;
            while (controlIndex + controlsInCategory < controlCategories.size() && (
                controlIndex + controlsInCategory == 0
                    || controlIndex + controlsInCategory < controlKeys.size())) {
                controlsInCategory++;
            }

            //draw controls
            regularFont.getData().setScale(viewport.getUnitsPerPixel());
            for (int i = 0; i < controlsInCategory; i++) {
                //draw key name
                regularFont.setColor(Color.YELLOW);
                regularFont.draw(spriteBatch, controlKeys.get(controlIndex), controlStartX,
                    currentY);

                //draw description
                regularFont.setColor(BUTTON_COLOR);
                regularFont.draw(spriteBatch, controlDescriptions.get(controlIndex),
                    controlStartX + viewport.getWorldWidth() * 0.15f, currentY);

                currentY -= lineHeight;
                controlIndex++;
            }

            //add padding between categories
            currentY -= lineHeight * 0.5f;
        }
        // draw back button
        if (controlsBackButton != null) {
            regularFont.setColor(BUTTON_HOVER_COLOR);
            glyphLayout.setText(regularFont, controlsBackButton.getText());
            float buttonX = controlsBackButton.getX() - glyphLayout.width / 2f;
            float buttonY = controlsBackButton.getY() - glyphLayout.height / 2f;
            regularFont.draw(spriteBatch, controlsBackButton.getText(), buttonX, buttonY);

            controlsBackButton.setBounds(new Rectangle(
                controlsBackButton.getX() - BUTTON_WIDTH / 2f,
                controlsBackButton.getY() - BUTTON_HEIGHT / 2f,
                BUTTON_WIDTH,
                BUTTON_HEIGHT
            ));
        }
    }

    private void renderButtons(SpriteBatch spriteBatch) {
        if (!menuInitialized) {
            return;
        }

        for (int i = 0; i < optionButtons.size(); i++) {
            MenuButton button = optionButtons.get(i);

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

            float textX = button.getX() - glyphLayout.width / 2f;
            float textY = button.getY() + glyphLayout.height / 2f;

            button.setBounds(new Rectangle(
                button.getX() - BUTTON_WIDTH / 2f,
                button.getY() - BUTTON_HEIGHT / 2f,
                BUTTON_WIDTH,
                BUTTON_HEIGHT));

            regularFont.draw(spriteBatch, buttonText, textX, textY);
        }
        regularFont.getData().setScale(viewport.getUnitsPerPixel());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        setupFonts();

        // recalculate button positions
        if (menuInitialized) {
            float worldCenterX = viewport.getWorldWidth() / 2f;
            float startY = viewport.getWorldHeight() * 0.5f;
            float spacing = BUTTON_HEIGHT + BUTTON_PADDING;

            for (int i = 0; i < optionButtons.size(); i++) {
                MenuButton button = optionButtons.get(i);
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

    public List<MenuButton> getOptionButtons() {
        return optionButtons;
    }

    public Vector3 unprojectScreenCoords(int screenX, int screenY) {
        Vector3 worldCoords = new Vector3(screenX, screenY, 0f);
        camera.unproject(worldCoords);
        return worldCoords;
    }

    public boolean isShowingControls() {
        return showingControls;
    }

    public MenuButton getControlsBackButton() {
        return controlsBackButton;
    }

    public Runnable getBackButtonAction() {
        return backButtonAction;
    }
}
