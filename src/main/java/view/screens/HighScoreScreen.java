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
import controller.HighScoreScreenController;
import java.util.List;
import model.GameStateModel;
import model.Score.ScoreBoard;
import model.Score.ScoreEntry;
import model.constants.GameState;
import model.utils.MenuButton;
import view.Palette;

public class HighScoreScreen implements Screen {

    private final GameStateModel gameStateModel;
    private final SpriteBatch spriteBatch;
    private final ScreenViewport viewport;
    private final OrthographicCamera camera;
    private final HighScoreScreenController controller;

    private final List<ScoreEntry> scoreEntries;

    private final BitmapFont titleFont;
    private final BitmapFont regularFont;

    private final Color TITLE_COLOR = Palette.TITLE_FONT_COLOR;
    private final Color BUTTON_COLOR = Color.WHITE;
    private final Color BUTTON_HOVER_COLOR = Palette.TITLE_FONT_HIGHLIGHT_COLOR;

    private MenuButton backButton;

    private float stateTime = 0f;
    private float pulseTime = 0f;
    private final GlyphLayout glyphLayout;

    public HighScoreScreen(TestSpaceGame game, GameStateModel gameStateModel) {
        this.gameStateModel = gameStateModel;
        this.spriteBatch = game.getSpriteBatch();
        AssetManager assetManager = game.getAssetManager();
        this.viewport = game.getScreenViewport();
        this.camera = (OrthographicCamera) viewport.getCamera();

        this.titleFont = assetManager.get("fonts/PixelOperatorMono-Bold.ttf", BitmapFont.class);
        this.regularFont = assetManager.get("fonts/PixelOperatorMonoHB.ttf", BitmapFont.class);
        this.glyphLayout = new GlyphLayout();

        this.controller = new HighScoreScreenController(this, gameStateModel, game);
        scoreEntries = new ScoreBoard().getHighScores();
        setupFonts();
        setupMenu();
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
        gameStateModel.changeState(GameState.HIGH_SCORE);
    }

    @Override
    public void render(float delta) {
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

        String titleText = "HIGH SCORES";
        glyphLayout.setText(titleFont, titleText);
        float titleX = (viewport.getWorldWidth() - glyphLayout.width) / 2f;
        float titleY = viewport.getWorldHeight() * 0.85f + glyphLayout.height / 2f;

        titleFont.setColor(TITLE_COLOR);
        titleFont.draw(spriteBatch, titleText, titleX, titleY);
        titleFont.getData().setScale(viewport.getUnitsPerPixel());

        // draw headers and entries in columns
        float scoreStartY = backButton.getY() + 6.0f;
        float lineHeight = 1.0f;
        regularFont.setColor(Color.WHITE);
        regularFont.getData().setScale(viewport.getUnitsPerPixel());

        String headerNum = "#";
        String headerName = "NAME";
        String headerScore = "SCORE";

        float centerX = viewport.getWorldWidth() / 2f;
        float colNumX = centerX - 3.5f;
        float colNameX = centerX - 2.5f;
        float colScoreX = centerX + 2.5f;

        // draw headers
        glyphLayout.setText(regularFont, headerNum);
        regularFont.draw(spriteBatch, headerNum, colNumX, scoreStartY + lineHeight);

        glyphLayout.setText(regularFont, headerName);
        regularFont.draw(spriteBatch, headerName, colNameX, scoreStartY + lineHeight);

        glyphLayout.setText(regularFont, headerScore);
        regularFont.draw(spriteBatch, headerScore, colScoreX, scoreStartY + lineHeight);

        // draw score entries
        for (int i = 0; i < scoreEntries.size(); i++) {
            ScoreEntry entry = scoreEntries.get(i);
            float y = scoreStartY - i * lineHeight;
            regularFont.setColor(i == 0 ? Color.GOLD : Color.WHITE);
            
            regularFont.draw(spriteBatch, String.valueOf(i + 1), colNumX, y);
            regularFont.draw(spriteBatch, entry.name(), colNameX, y);
            regularFont.draw(spriteBatch, String.valueOf(entry.score()), colScoreX, y);
        }

        renderButton(spriteBatch);

        spriteBatch.end();
        controller.update(delta);
    }

    private void setupMenu() {
        float worldHeight = viewport.getWorldHeight();
        float worldCenterX = viewport.getWorldWidth() / 2f;

        float startY = worldHeight * 0.25f;
        backButton = new MenuButton("BACK", worldCenterX, startY);
    }

    private void renderButton(SpriteBatch spriteBatch) {
        MenuButton button = backButton;

        if (0 == gameStateModel.getSelectedButtonIndex()) {
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

        float BUTTON_WIDTH = 8f;
        float BUTTON_HEIGHT = 1f;
        button.setBounds(new Rectangle(
                button.getX() - BUTTON_WIDTH / 2f,
                button.getY() - BUTTON_HEIGHT / 2f,
                BUTTON_WIDTH,
                BUTTON_HEIGHT));

        regularFont.draw(spriteBatch, buttonText, textX, textY);
        regularFont.getData().setScale(viewport.getUnitsPerPixel());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        setupFonts();

        // recalculate button position
        float worldCenterX = viewport.getWorldWidth() / 2f;
        float startY = viewport.getWorldHeight() * 0.25f;

        backButton.setX(worldCenterX);
        backButton.setY(startY);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() { }

    public Vector3 unprojectScreenCoords(int screenX, int screenY) {
        Vector3 worldCoords = new Vector3(screenX, screenY, 0f);
        camera.unproject(worldCoords);
        return worldCoords;
    }

    public MenuButton getBackButton() {
        return backButton;
    }
}
