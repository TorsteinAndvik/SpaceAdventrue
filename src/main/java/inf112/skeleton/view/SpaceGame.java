package inf112.skeleton.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public interface SpaceGame {
    /**
     * @return the application's <code>FitViewport</code>
     */
    FitViewport getFitViewport();

    /**
     * @return the application's <code>ScreenViewport</code>
     */
    ScreenViewport getScreenViewport();

    /**
     * @return the application's <code>ScreenViewport</code>
     */
    ExtendViewport getExtendViewport();

    /**
     * @return the application's <code>SpriteBatch</code>
     */
    SpriteBatch getSpriteBatch();

    /**
     * @return the application's <code>AssetManager</code>
     */
    AssetManager getAssetManager();

    /**
     * TODO: Make an abstract class "between" the different screens and the Screen
     * interface which implements most of the same boilerplate code, and use that as
     * the input type for this method
     * 
     * @param screen the Screen we wish to swap to
     */
    void setScreen(Screen screen);
}
