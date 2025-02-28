package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import inf112.skeleton.view.SpaceGame;
import inf112.skeleton.view.LoadingScreen;

public class TestSpaceGame extends Game implements SpaceGame {

    private SpriteBatch batch;
    private AssetManager manager;
    private FitViewport fitViewport;
    private ExtendViewport extendViewport;
    private ScreenViewport screenViewport;
    private final int METERS = 9; // screen will be meters x meters (we use meters as Game coordinates, NOT pixel coordinates - these depend on window size, awful to work with)

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        // screen will be meters x meters (we use meters as Game coordinates, NOT pixel coordinates - these depend on window size, awful to work with)
        fitViewport = new FitViewport(METERS, METERS);
        screenViewport = new ScreenViewport();
        screenViewport.setUnitsPerPixel((float)METERS / (float)Gdx.graphics.getWidth());
        extendViewport = new ExtendViewport(Gdx.graphics.getWidth() / METERS, Gdx.graphics.getHeight() / METERS);
    
        manager = new AssetManager();

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
}
