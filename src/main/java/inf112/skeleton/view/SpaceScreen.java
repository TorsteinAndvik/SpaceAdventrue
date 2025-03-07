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

import inf112.skeleton.controller.SpaceGameScreenController;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.model.SpaceGameModel;
import inf112.skeleton.model.ShipComponents.Components.Fuselage;

public class SpaceScreen implements Screen {

    final SpaceGame game;
    final SpaceGameModel model;
    private final SpaceGameScreenController controller;
    SpriteBatch batch;
    FitViewport viewport;
    BitmapFont fontBold;    //Agency FB Bold
    BitmapFont fontRegular; //Agency FB Regular
    AssetManager manager; 

    Sprite asteroid;
    Sprite laser;

    Sprite fuselagePlayer;
    Sprite fuselageEnemy;
    Sprite thruster;
    Sprite turret;
    Sprite shield;

    float laserUpdateCutoff = 0.5f;
    float laserUpdateTimer;

    public SpaceScreen(final SpaceGame game, final SpaceGameModel model) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();
        this.viewport = game.getFitViewport();

        this.model = model;

        this.controller = new SpaceGameScreenController(this, model);

        fontBold = manager.get("fonts/AGENCYB.ttf", BitmapFont.class);
        fontRegular = manager.get("fonts/AGENCYR.ttf", BitmapFont.class);

        // font are set as [integer]pt, need to scale them to our viewport by ratio of viewport height to screen height in order to use world-unit sized font
        fontBold.setUseIntegerPositions(false);
        fontBold.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        fontRegular.setUseIntegerPositions(false);
        fontRegular.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        loadSprites();
    }

    private void loadSprites() {
        asteroid = createSprite("images/space/asteroid_0.png", 2, 2);
        laser = createSprite("images/space/laser_shot_0.png", 0.25f, 0.25f);
        
        fuselagePlayer = createSprite("images/upgrades/fuselage_alt_stage_0.png", 1, 1);
        fuselageEnemy = createSprite("images/upgrades/fuselage_enemy_stage_0.png", 1, 1);
        turret = createSprite("images/upgrades/turret_laser_stage_0.png", 1, 1);
        thruster = createSprite("images/upgrades/rocket_stage_0.png", 1, 1);
        shield = createSprite("images/upgrades/shield_stage_0.png", 1, 1);
    }

    private Sprite createSprite(String path, float width, float height) {
        Sprite sprite = new Sprite(manager.get(path, Texture.class));
        sprite.setSize(width, height);
        return sprite;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY); 
        
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        fontBold.setColor(Color.GREEN);
        fontRegular.setColor(Color.RED);
        
        batch.begin();

        asteroid.setX(model.getAsteroid().getX());
        asteroid.setY(model.getAsteroid().getY());
        asteroid.draw(batch);
        
        for (GridCell<Fuselage> cell : model.getEnemyShip().getShipStructure().iterable()) {
            fuselageEnemy.setX(model.getEnemyShip().getX() + cell.pos().col());
            fuselageEnemy.setY(model.getEnemyShip().getY() + cell.pos().row());
            fuselageEnemy.draw(batch);
        }

        
        for (GridCell<Fuselage> cell : model.getPlayer().getShipStructure().iterable()) {
            fuselagePlayer.setX(model.getPlayer().getX() + cell.pos().col());
            fuselagePlayer.setY(model.getPlayer().getY() + cell.pos().row());
            fuselagePlayer.draw(batch);
        }

        if(model.laserExists) { //TODO: refactor, I beg thee
            laser.setX(model.getLaser().getX() + 0.375f);
            laser.setY(model.getLaser().getY() + 0.375f);
            laser.draw(batch);

            laserUpdateTimer += delta;
            if(laserUpdateTimer >= laserUpdateCutoff) {
                laserUpdateTimer = 0f;
                model.moveLaser();
            }
        }

        //fontBold.draw(batch, "Hello, World!", 1f, 1f);
        //fontRegular.draw(batch, "The helloest of Worlds!", 2f, 2f);
        batch.end();
    }

    @Override
    public void dispose() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void resume() {}

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }
    
}
