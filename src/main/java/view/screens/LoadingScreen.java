package view.screens;

import app.TestSpaceGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import model.GameStateModel;
import view.SpaceGame;
import view.bars.PercentageBar;

public class LoadingScreen implements Screen {

    private final SpaceGame game;
    private final GameStateModel gameStateModel;
    private ScreenViewport viewport;
    private ShapeRenderer shape;
    private AssetManager manager;
    private PercentageBar loadingProgressBar;

    // Constants
    int boldFontSize = 42;
    int regularFontSize = 36;

    public LoadingScreen(SpaceGame game, GameStateModel gameStateModel) {
        this.game = game;
        this.gameStateModel = gameStateModel;
        this.manager = this.game.getAssetManager();
        this.viewport = this.game.getScreenViewport();
        this.shape = this.game.getShapeRenderer();

        Pixmap pm = new Pixmap(
                manager.getFileHandleResolver().resolve("images/pointer_scifi_b.png")); // Custom cursor
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 8, 8));

        setupProgressBar();

        queueAssets();
    }

    private void setupProgressBar() {
        loadingProgressBar = new PercentageBar();
        loadingProgressBar.setColors(Palette.BACKGROUND_GREEN, Color.WHITE);
        loadingProgressBar.setScale(10f, 0.2f);
    }

    private void queueAssets() {
        // Textures:
        // background
        queueTexture("images/space/background/bkgd_1.png");
        queueTexture("images/space/background/bkgd_2.png");
        queueTexture("images/space/background/bkgd_3.png");
        queueTexture("images/space/background/bkgd_4.png");
        queueTexture("images/space/background/bkgd_6.png");
        queueTexture("images/space/background/bkgd_7.png");

        // space objects
        queueTexture("images/space/asteroid_0.png");
        queueTexture("images/space/asteroid_1.png");
        queueTexture("images/space/laser_shot_0.png");

        // upgrade screen
        queueTexture("images/upgrade_grid_tile_green.png");
        queueTexture("images/upgrade_grid_tile_red.png");
        queueTexture("images/upgrade_grid_tile_gray.png");

        // ship parts
        queueTexture("images/upgrades/turret_laser_stage_0.png");
        queueTexture("images/upgrades/fuselage_alt_stage_0.png");
        queueTexture("images/upgrades/rocket_stage_0.png");
        queueTexture("images/upgrades/shield_stage_0.png");
        queueTexture("images/upgrades/fuselage_enemy_stage_0.png");

        // ui
        queueTexture("images/ui/Mouse_Left_Key_Light.png");
        queueTexture("images/ui/Mouse_Middle_Key_Light.png");
        queueTexture("images/ui/Mouse_Right_Key_Light.png");
        queueTexture("images/ui/T_Key_Light.png");
        queueTexture("images/ui/Esc_Key_Light.png");

        // animations
        queueTexture("images/animations/explosion_A.png");
        queueTextureAtlas("images/animations/explosion_A.atlas");

        // Sounds:
        queueSound("audio/blipp.ogg");

        // Fonts:
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        // Set params for bold font
        FreeTypeFontLoaderParameter fontBold = new FreeTypeFontLoaderParameter();
        fontBold.fontFileName = "fonts/AGENCYB.ttf";
        fontBold.fontParameters.size = boldFontSize;
        manager.load(fontBold.fontFileName, BitmapFont.class, fontBold);

        // Set params for regular font
        FreeTypeFontLoaderParameter fontRegular = new FreeTypeFontLoaderParameter();
        fontRegular.fontFileName = "fonts/AGENCYR.ttf";
        fontRegular.fontParameters.size = regularFontSize;
        manager.load(fontRegular.fontFileName, BitmapFont.class, fontRegular);
    }

    private void queueTexture(String path) {
        manager.load(new AssetDescriptor<>(Gdx.files.internal(path), Texture.class));
    }

    private void queueTextureAtlas(String path) {
        manager.load(new AssetDescriptor<>(Gdx.files.internal(path), TextureAtlas.class));
    }

    private void queueSound(String path) {
        manager.load(new AssetDescriptor<>(Gdx.files.internal(path), Sound.class));
    }

    @Override
    public void render(float delta) {
        // First assets are queued for loading in the constructor (before this block of
        // code runs), and then calling .update() here will *actually* load them.
        if (manager.update(17)) { // 17ms ~= 60fps
            // notify the model that all assets are loaded
            gameStateModel.onAssetsLoaded();

            // show start screen
            if (Gdx.input.isTouched() && game instanceof TestSpaceGame) {
                loadingProgressBar.setVisible(false);
                ((TestSpaceGame) game).setStartScreen();
            }
        }

        float progress = manager.getProgress();
        loadingProgressBar.setCurrentValue(progress);

        viewport.apply(true);
        shape.setProjectionMatrix(viewport.getCamera().combined);

        shape.begin(ShapeType.Filled);
        loadingProgressBar.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
        loadingProgressBar.draw(shape);
        shape.end();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }
}
