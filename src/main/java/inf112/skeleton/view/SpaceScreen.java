package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SpaceScreen implements Screen {

    final SpaceGame game;
    final ViewableSpaceGameModel model;
    SpriteBatch batch;
    FitViewport viewport;
    BitmapFont fontBold;    //Agency FB Bold
    BitmapFont fontRegular; //Agency FB Regular
    AssetManager manager; 

    Sprite asteroid;

    public SpaceScreen(final SpaceGame game, final ViewableSpaceGameModel model) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();
        this.viewport = game.getFitViewport();

        this.model = model;

        fontBold = manager.get("fonts/AGENCYB.ttf", BitmapFont.class);
        fontRegular = manager.get("fonts/AGENCYR.ttf", BitmapFont.class);

        // font are set as [integer]pt, need to scale them to our viewport by ratio of viewport height to screen height in order to use world-unit sized font
        fontBold.setUseIntegerPositions(false);
        fontBold.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        fontRegular.setUseIntegerPositions(false);
        fontRegular.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        asteroid = new Sprite(manager.get("images/space/asteroid_0.png", Texture.class));
        asteroid.setSize(2, 2);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GRAY); 
        
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        fontBold.setColor(Color.GREEN);
        fontRegular.setColor(Color.RED);
        
        batch.begin();
        asteroid.setX(model.getAsteroid().getX());
        asteroid.setY(model.getAsteroid().getY());
        asteroid.draw(batch);

        fontBold.draw(batch, "Hello, World!", 1f, 1f);
        fontRegular.draw(batch, "The helloest of Worlds!", 2f, 2f);
        batch.end();
    }

    @Override
    public void dispose() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void resume() {}

    @Override
    public void show() {}
    
}
