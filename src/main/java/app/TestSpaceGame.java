package app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import view.SpaceGame;
import view.SpaceScreen;
import view.UpgradeScreen;
import model.SpaceGameModel;
import view.LoadingScreen;

public class TestSpaceGame extends Game implements SpaceGame {

    private SpriteBatch batch;
    private AssetManager manager;
    private FitViewport fitViewport;
    private ExtendViewport extendViewport;
    private ScreenViewport screenViewport;
    private LoadingScreen loadingScreen;
    private UpgradeScreen upgradeScreen;
    private SpaceGameModel spaceGameModel;
    private SpaceScreen spaceScreen;
    private final int METERS = 9; // screen will be meters x meters (we use meters as Game coordinates, NOT pixel
    // coordinates - these depend on window size, awful to work with)

    @Override
    public void create() {
        batch = new SpriteBatch();

        // screen will be meters x meters (we use meters as Game coordinates, NOT pixel
        // coordinates - these depend on window size, awful to work with)
        fitViewport = new FitViewport(METERS, METERS);
        screenViewport = new ScreenViewport();
        screenViewport.setUnitsPerPixel((float) METERS / (float) Gdx.graphics.getWidth());
        extendViewport = new ExtendViewport(Gdx.graphics.getWidth() / METERS,
            Gdx.graphics.getHeight() / METERS);

        manager = new AssetManager();

        this.loadingScreen = new LoadingScreen(this);

        this.setScreen(new LoadingScreen(this));
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
    public SpriteBatch getSpriteBatch() {
        return batch;
    }

    @Override
    public AssetManager getAssetManager() {
        return manager;
    }

    @Override
    public void dispose() {
        this.screen.dispose();
        this.batch.dispose();
        this.manager.dispose();
    }

    /**
     * Change active screen to UpgradeScreen.
     */
    public void setUpgradeScreen() {
        if (this.upgradeScreen == null) {
            if (this.spaceGameModel == null) {
                this.spaceGameModel = new SpaceGameModel();
            }
            this.upgradeScreen = new UpgradeScreen(this, this.spaceGameModel);
        }

        setScreen(this.upgradeScreen);
    }

    /**
     * Change active screen to SpaceScreen.
     */
    public void setSpaceScreen() {
        if (this.spaceScreen == null) {
            if (this.spaceGameModel == null) {
                this.spaceGameModel = new SpaceGameModel();
            }
            this.spaceScreen = new SpaceScreen(this, this.spaceGameModel);
        }

        setScreen(this.spaceScreen);
    }
}
