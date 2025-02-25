package inf112.skeleton.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SpaceScreen implements Screen {

    final SpaceGame game;
    SpriteBatch batch;
    FitViewport viewport;
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

        viewport = game.getFitViewport();
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
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resize(int width, int height) {
        game.getFitViewport().update(width, height, true);
    }

    @Override
    public void resume() {}

    @Override
    public void show() {}
    
}
