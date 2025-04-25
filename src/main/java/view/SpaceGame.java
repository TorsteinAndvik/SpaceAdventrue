package view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import controller.audio.MusicManager;
import controller.audio.SoundManager;

public interface SpaceGame {

    /**
     * @return the application's <code>FitViewport</code>.
     */
    FitViewport getFitViewport();

    /**
     * @return the application's <code>ScreenViewport</code>.
     */
    ScreenViewport getScreenViewport();

    /**
     * @return the application's <code>ScreenViewport</code>.
     */
    ExtendViewport getExtendViewport();

    /**
     * @return the application's <code>SpriteBatch</code>.
     */
    SpriteBatch getSpriteBatch();

    /**
     * @return the application's <code>ShapeRenderer</code>.
     */
    ShapeRenderer getShapeRenderer();

    /**
     * @return the application's <code>AssetManager</code>.
     */
    AssetManager getAssetManager();

    /**
     * Change active screen to SpaceScreen.
     */
    void setSpaceScreen();

    /**
     * Change active screen to UpgradeScreen.
     */
    void setUpgradeScreen();

    /**
     * Change active screen to OptionsScreen
     */
    void setOptionsScreen();

    /**
     * Change active screen to HighScoreScreen
     */
    void setHighScoreScreen();

    /**
     * Change active screen to StartScreen
     */
    void setStartScreen();

    /**
     * @return the application's <code>MusicManager</code>.
     */
    MusicManager getMusicManager();

    /**
     * @return the application's <code>SoundManager</code>.
     */
    SoundManager getSoundManager();
}
