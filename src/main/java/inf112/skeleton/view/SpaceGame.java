package inf112.skeleton.view;

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
}
