package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen implements Screen {

    final SpaceGame game;
    SpriteBatch batch;
    Viewport viewport;
    BitmapFont font;
    AssetManager manager; //An assetmanager helps with loading assets and disposing them once they are no longer needed 

    public LoadingScreen(final SpaceGame game) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();

        queueAssets();
    }

    private void queueAssets() {
        // Textures:
        manager.load("src/main/resources/images/obligator.png", Texture.class);
        manager.load("src/main/resources/images/upgrade_grid_tile_green.png", Texture.class);
        manager.load("src/main/resources/images/upgrade_grid_tile_red.png", Texture.class);

        // Sounds:
        manager.load("blipp.ogg", Sound.class);
    }

    @Override
    public void render(float delta) {
        //First assets are queued for loading in the constructor (before this block of code runs), and then calling .update() here will *actually* load them. 
        if(manager.update(17)) { //all assets are loaded 1 by 1 //blocks for at least 17ms before passing over to render() - roughly 60fps (depends on size of asset, a large enough file might block for longer)
            game.setScreen(new UpgradeScreen(game));
        }

        float progress = manager.getProgress();
        System.out.println("Loading assets: " + 100f*progress + "%");
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
        game.getViewport().update(width, height, true);
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
