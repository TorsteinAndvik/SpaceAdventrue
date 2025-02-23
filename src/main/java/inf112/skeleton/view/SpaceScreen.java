package inf112.skeleton.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SpaceScreen implements Screen {

    final SpaceGame game;
    SpriteBatch batch;
    Viewport viewport;
    BitmapFont font;
    AssetManager manager; //An assetmanager helps with loading assets and disposing them once they are no longer needed 

    public SpaceScreen(final SpaceGame game) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLUE); // Always wipe screen before drawing

        viewport = game.getFitViewport();  // TODO: If using multiple viewports, need some way to signal which one to use for different screens (maybe GameState?)
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        font = game.getFont();
        font.setColor(Color.RED);

        batch.begin();
        batch.draw(manager.get("src/main/resources/images/obligator.png", Texture.class), 0f, 3f, 5f, 1f);
        font.draw(batch, "Hello, World!", 1f, 1f);
        batch.end();
    }

    @Override
    public void dispose() {
        this.dispose();
    }

    @Override
    public void hide() {
        // TODO: If using per-Screen InputProcessors (recommended), need to *unregister* it here
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resize(int width, int height) {
        game.getFitViewport().update(width, height, true);
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void show() {
        // TODO: If using per-Screen InputProcessors (recommended), need to *register* it here
    }
    
}
