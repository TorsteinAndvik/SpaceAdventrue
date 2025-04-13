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
    private final GlyphLayout glyphLayout;

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
    }

    private void setupMenu() {
        float worldHeight = viewport.getWorldHeight();
        float worldCenterX = viewport.getWorldWidth();

        float startY = worldHeight * 0.5f;
        float spacing = BUTTON_HEIGHT + BUTTON_PADDING;

        optionButtons.clear();
        optionButtons.add(new MenuButton("SOUNT: ON", worldCenterX, startY));
        optionButtons.add(new MenuButton("MUSIC: ON", worldCenterX, startY = spacing));
        optionButtons.add(new MenuButton("BACK", worldCenterX, startY - 2 * spacing));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
        gameStateModel.changeState(GameState.OPTIONS);
        setupMenu();
    }

    @Override
    public void render(float delta) {
        if (!menuInitialized) {
            setupMenu();
        }
        stateTime += delta;
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        titleFont.getData().setScale(viewport.getUnitsPerPixel() * 1.2f);

        glyphLayout.setText(titleFont, SCREEN_TITLE);
        float titleX = (viewport.getWorldWidth() - glyphLayout.width) / 2;
        float titleY = (viewport.getWorldHeight() * 0.7f + glyphLayout.height) / 2;

        titleFont.setColor(TITLE_COLOR);
        titleFont.draw(spriteBatch, SCREEN_TITLE, titleX, titleY);

        titleFont.getData().setScale(viewport.getUnitsPerPixel());

        renderButtons(spriteBatch);
        spriteBatch.end();
        controller.update(delta);
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
            float textY = button.getY() - glyphLayout.height / 2f;

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
        setupMenu();
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
}
