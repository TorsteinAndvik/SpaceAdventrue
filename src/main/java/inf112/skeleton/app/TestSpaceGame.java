package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import inf112.skeleton.view.SpaceGame;
import inf112.skeleton.view.LoadingScreen;

public class TestSpaceGame extends Game implements SpaceGame {

    private SpriteBatch batch;
    private BitmapFont font;
    private AssetManager manager;
    private FitViewport fitViewport;
    private ScreenViewport screenViewport;
    private ExtendViewport extendViewport;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        
        int meters = 7; // screen will be meters x meters (we use meters as Game coordinates, NOT pixel coordinates - these depend on window size, awful to work with)
        fitViewport = new FitViewport(meters, meters);
        screenViewport = new ScreenViewport();
        screenViewport.setUnitsPerPixel((float)meters / (float)Gdx.graphics.getWidth());
        extendViewport = new ExtendViewport(Gdx.graphics.getWidth() / meters, Gdx.graphics.getHeight() / meters);

        // font is 15pt, need to scale it to our viewport by ratio of viewport height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale(fitViewport.getWorldHeight() / Gdx.graphics.getHeight());
    
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
    public BitmapFont getFont() {
        return font;
    }

    @Override
    public AssetManager getAssetManager() {
        return manager;
    }
}
