package app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import controller.audio.MusicManager;
import controller.audio.SoundManager;
import model.GameStateModel;
import model.SpaceGameModel;
import view.SpaceGame;
import view.screens.LoadingScreen;
import view.screens.SpaceScreen;
import view.screens.StartGameScreen;
import view.screens.UpgradeScreen;

public class TestSpaceGame extends Game implements SpaceGame {

    private SpriteBatch batch;
    private ShapeRenderer shape;
    private AssetManager manager;
    private MusicManager musicManager;
    private SoundManager soundManager;
    private FitViewport fitViewport;
    private ExtendViewport extendViewport;
    private ScreenViewport screenViewport;

    private GameStateModel gameStateModel;
    private final int METERS = 15; // screen will be meters x meters (we use meters as Game coordinates, NOT pixel
    // coordinates - these depend on window size, awful to work with)

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        // screen will be meters x meters (we use meters as Game coordinates, NOT pixel
        // coordinates - these depend on window size, awful to work with)
        fitViewport = new FitViewport(METERS, METERS);
        screenViewport = new ScreenViewport();
        screenViewport.setUnitsPerPixel((float) METERS / (float) Gdx.graphics.getWidth());
        extendViewport = new ExtendViewport(Gdx.graphics.getWidth() / METERS,
                Gdx.graphics.getHeight() / METERS);

        manager = new AssetManager();

        musicManager = new MusicManager(manager);

        soundManager = new SoundManager(manager);

        gameStateModel = new GameStateModel();

        this.setScreen(new LoadingScreen(this, gameStateModel));
    }

    @Override
    public FitViewport getFitViewport() {
        return fitViewport;
    }

    @Override
    public ScreenViewport getScreenViewport() {
        return screenViewport;
    }

    @Override
    public ExtendViewport getExtendViewport() {
        return extendViewport;
    }

    @Override
    public ShapeRenderer getShapeRenderer() {
        return shape;
    }

    @Override
    public SpriteBatch getSpriteBatch() {
        return batch;
    }

    @Override
    public AssetManager getAssetManager() {
        return manager;
    }

    @Override
    public void dispose() {
        if (this.screen != null) {
            this.screen.dispose();
        }
        this.batch.dispose();
        this.manager.dispose();
    }

    /**
     * Get the game state model
     *
     * @return the game state model for this game
     */
    public GameStateModel getGameStateModel() {
        return this.gameStateModel;
    }

    /**
     * Change to the start screen
     */
    public void setStartScreen() {
        setScreen(new StartGameScreen(this, gameStateModel));
    }

    /**
     * Change active screen to UpgradeScreen.
     */
    public void setUpgradeScreen() {
        gameStateModel.showUpgradeScreen();
        SpaceGameModel spaceGameModel = gameStateModel.getSpaceGameModel();
        UpgradeScreen upgradeScreen = new UpgradeScreen(this, spaceGameModel);
        setScreen(upgradeScreen);
    }

    /**
     * Change active screen to SpaceScreen.
     */
    public void setSpaceScreen() {
        if (gameStateModel.getSpaceGameModel() == null) {
            gameStateModel.startNewGame();
        } else {
            gameStateModel.returnToGame();
        }

        SpaceGameModel spaceGameModel = gameStateModel.getSpaceGameModel();
        SpaceScreen spaceScreen = new SpaceScreen(this, spaceGameModel);

        spaceGameModel.setAnimationCallback(spaceScreen);
        spaceGameModel.setScreenBoundsProvider(spaceScreen);
        setScreen(spaceScreen);
    }

    @Override
    public MusicManager getMusicManager() {
        return this.musicManager;
    }

    @Override
    public SoundManager getSoundManager() {
        return this.soundManager;
    }
}
