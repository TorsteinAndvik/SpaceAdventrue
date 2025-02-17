package inf112.skeleton.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface SpaceGame {
    /**
     * @return the currently active <code>ViewPort</code>
     */
    Viewport getViewport();

    /**
     * @return the application's <code>SpriteBatch</code>
     */
    SpriteBatch getSpriteBatch();

    /**
     * @return the application's <code>BitMapFont</code>
     */
    BitmapFont getFont();

    /**
     * @return the application's <code>AssetManager</code>
     */
    AssetManager getAssetManager();

    /**
     * TODO: Make an abstract class "between" the different screens and the Screen interface which implements most of the same boilerplate code, and use that as the input type for this method
     * @param spaceScreen
     */
    void setScreen(SpaceScreen spaceScreen);
}
