package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
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

        Pixmap pm = new Pixmap(manager.getFileHandleResolver().resolve("images/pointer_scifi_b.png")); // Custom cursor
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 8, 8));

        queueAssets();
    }

    private void queueAssets() {
        // Textures:
            // upgrades
        queueTexture("images/upgrade_grid_tile_green.png");
        queueTexture("images/upgrade_grid_tile_red.png");
        queueTexture("images/upgrade_grid_tile_gray.png");
        queueTexture("images/upgrades/turret_laser_stage_0.png");
        queueTexture("images/upgrades/fuselage_alt_stage_0.png");
        queueTexture("images/upgrades/rocket_stage_0.png");
        queueTexture("images/upgrades/shield_stage_0.png");
            
            // ui
        queueTexture("images/ui/Mouse_Left_Key_Light.png");
        queueTexture("images/ui/Mouse_Middle_Key_Light.png");
        queueTexture("images/ui/Mouse_Right_Key_Light.png");
        queueTexture("images/ui/T_Key_Light.png");

        // Sounds:
        queueSound("audio/blipp.ogg");

        // Fonts:
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

            // Set params for bold font
        FreeTypeFontLoaderParameter fontBold = new FreeTypeFontLoaderParameter();
        fontBold.fontFileName = "fonts/AGENCYB.ttf";
        fontBold.fontParameters.size = 42;
        manager.load(fontBold.fontFileName, BitmapFont.class, fontBold);

            // Set params for regular font
        FreeTypeFontLoaderParameter fontRegular = new FreeTypeFontLoaderParameter();
        fontRegular.fontFileName = "fonts/AGENCYR.ttf";
        fontRegular.fontParameters.size = 36;
        manager.load(fontRegular.fontFileName, BitmapFont.class, fontRegular);
    }

    private void queueTexture(String path) {
        manager.load(new AssetDescriptor<>(Gdx.files.internal(path), Texture.class));
    }

    private void queueSound(String path) {
        manager.load(new AssetDescriptor<>(Gdx.files.internal(path), Sound.class));
    }

    @Override
    public void render(float delta) {
        //First assets are queued for loading in the constructor (before this block of code runs), and then calling .update() here will *actually* load them. 
        if(manager.update(17)) { //all assets are loaded 1 by 1 //blocks for at least 17ms before passing over to render() - roughly 60fps (depends on size of asset, a large enough file might block for longer)
            // ONLY CALL ONE OF THESE FOR TESTING:
            game.setScreen(new UpgradeScreen(game)); //test the UpgradeScreen class
            //game.setScreen(new SpaceScreen(game));  //test the SpaceScreen class
        }

        float progress = manager.getProgress();
        System.out.println("Loading assets: " + 100f*progress + "%");
    }

    @Override
    public void dispose() {
        //TODO: This will recurse infinitely, no?
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
