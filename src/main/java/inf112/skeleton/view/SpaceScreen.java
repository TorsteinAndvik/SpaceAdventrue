package inf112.skeleton.view;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import inf112.skeleton.controller.SpaceGameScreenController;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.model.SpaceGameModel;
import inf112.skeleton.model.ShipComponents.UpgradeType;
import inf112.skeleton.model.ShipComponents.Components.Fuselage;
import inf112.skeleton.model.SpaceCharacters.SpaceShip;

public class SpaceScreen implements Screen {

    final SpaceGame game;
    final SpaceGameModel model;
    private final SpaceGameScreenController controller;
    SpriteBatch batch;
    FitViewport viewport;
    BitmapFont fontBold; // Agency FB Bold
    BitmapFont fontRegular; // Agency FB Regular
    AssetManager manager;

    Sprite asteroid;
    Sprite laser;

    Sprite fuselagePlayer;
    Sprite fuselageEnemy;

    HashMap<UpgradeType, Sprite> upgradeIcons;

    float laserUpdateCutoff = 0.5f;
    float laserUpdateTimer;

    // Matrices for handling rotation of space ships
    private Matrix3 rotationMatrix;
    private Matrix4 transformMatrix;

    public SpaceScreen(final SpaceGame game, final SpaceGameModel model) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();
        this.viewport = game.getFitViewport();

        this.model = model;
        this.controller = new SpaceGameScreenController(this, model, game);

        setupFonts();
        loadSprites();
        setupUpgradeHashMap();

        rotationMatrix = new Matrix3();
        transformMatrix = new Matrix4();
    }

    private void setupFonts() {
        fontBold = manager.get("fonts/AGENCYB.ttf", BitmapFont.class);
        fontRegular = manager.get("fonts/AGENCYR.ttf", BitmapFont.class);

        // font are set as [integer]pt, need to scale them to our viewport by ratio of
        // viewport height to screen height in order to use world-unit sized font
        fontBold.setUseIntegerPositions(false);
        fontBold.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        fontRegular.setUseIntegerPositions(false);
        fontRegular.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());
    }

    private void loadSprites() {
        asteroid = createSprite("images/space/asteroid_0.png", 2, 2);
        laser = createSprite("images/space/laser_shot_0.png", 0.25f, 0.25f);

        fuselagePlayer = createSprite("images/upgrades/fuselage_alt_stage_0.png", 1, 1);
        fuselageEnemy = createSprite("images/upgrades/fuselage_enemy_stage_0.png", 1, 1);
    }

    private void setupUpgradeHashMap() {
        upgradeIcons = new HashMap<UpgradeType, Sprite>();
        upgradeIcons.put(UpgradeType.TURRET, createSprite("images/upgrades/turret_laser_stage_0.png", 1, 1));
        upgradeIcons.put(UpgradeType.THRUSTER, createSprite("images/upgrades/rocket_stage_0.png", 1, 1));
        upgradeIcons.put(UpgradeType.SHIELD, createSprite("images/upgrades/shield_stage_0.png", 1, 1));
    }

    private Sprite createSprite(String path, float width, float height) {
        Sprite sprite = new Sprite(manager.get(path, Texture.class));
        sprite.setSize(width, height);
        return sprite;
    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        ScreenUtils.clear(Color.DARK_GRAY);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        fontBold.setColor(Color.GREEN);
        fontRegular.setColor(Color.RED);

        model.rotateEnemy(); // TODO: Remove this once proper model is in place
                             // view should only send model.update(delta) via controller in future
        batch.begin();

        asteroid.setX(model.getAsteroid().getX());
        asteroid.setY(model.getAsteroid().getY());
        asteroid.draw(batch);

        for (int i = 0; i < model.getSpaceShips().length; i++) {
            SpaceShip ship = model.getSpaceShips()[i];
            float angle = ship.getRotationAngle();

            // reset the transformation matrix
            transformMatrix.idt();

            // translate the transformation matrix to the ship's center of rotation
            float x = ship.getX() + 0.5f * (float) ship.getShipStructure().getWidth();
            float y = ship.getY() + 0.5f * (float) ship.getShipStructure().getHeight();
            transformMatrix.translate(x, y, 0f);

            // apply rotation
            rotationMatrix.setToRotation(angle);
            transformMatrix.mul(new Matrix4().set(rotationMatrix));

            // undo the translation
            transformMatrix.translate(-x, -y, 0f);

            // set the transform matrix for the batch
            batch.setTransformMatrix(transformMatrix);

            for (GridCell<Fuselage> cell : model.getSpaceShips()[i].getShipStructure().iterable()) {
                float shipX = model.getSpaceShips()[i].getX() + cell.pos().col();
                float shipY = model.getSpaceShips()[i].getY() + cell.pos().row();
                if (model.getSpaceShips()[i].isPlayerShip()) {
                    fuselagePlayer.setX(shipX);
                    fuselagePlayer.setY(shipY);
                    fuselagePlayer.draw(batch);
                } else {
                    fuselageEnemy.setX(shipX);
                    fuselageEnemy.setY(shipY);
                    fuselageEnemy.draw(batch);
                }

                if (cell.value().getUpgrade() != null) {
                    upgradeIcons.get(cell.value().getUpgrade().getType()).setX(shipX);
                    upgradeIcons.get(cell.value().getUpgrade().getType()).setY(shipY);
                    upgradeIcons.get(cell.value().getUpgrade().getType()).draw(batch);
                }
            }
        }

        // Reset the transform matrix to the identity matrix
        batch.setTransformMatrix(new Matrix4().idt());

        if (model.laserExists) { // TODO: refactor, I beg thee
            laser.setX(model.getLaser().getX() + 0.375f);
            laser.setY(model.getLaser().getY() + 0.875f);
            laser.draw(batch);

            laserUpdateTimer += delta;
            if (laserUpdateTimer >= laserUpdateCutoff) {
                laserUpdateTimer = 0f;
                model.moveLaser();
            }
        }

        // fontBold.draw(batch, "Hello, World!", 1f, 1f);
        // fontRegular.draw(batch, "The helloest of Worlds!", 2f, 2f);
        batch.end();
    }

    // Local backup of old render method
    public void renderOld(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        fontBold.setColor(Color.GREEN);
        fontRegular.setColor(Color.RED);

        batch.begin();

        asteroid.setX(model.getAsteroid().getX());
        asteroid.setY(model.getAsteroid().getY());
        asteroid.draw(batch);

        for (int i = 0; i < model.getSpaceShips().length; i++) {
            for (GridCell<Fuselage> cell : model.getSpaceShips()[i].getShipStructure().iterable()) {
                float shipX = model.getSpaceShips()[i].getX() + cell.pos().col();
                float shipY = model.getSpaceShips()[i].getY() + cell.pos().row();
                if (model.getSpaceShips()[i].isPlayerShip()) {
                    fuselagePlayer.setX(shipX);
                    fuselagePlayer.setY(shipY);
                    fuselagePlayer.draw(batch);
                } else {
                    fuselageEnemy.setX(shipX);
                    fuselageEnemy.setY(shipY);
                    fuselageEnemy.draw(batch);
                }

                if (cell.value().getUpgrade() != null) {
                    upgradeIcons.get(cell.value().getUpgrade().getType()).setX(shipX);
                    upgradeIcons.get(cell.value().getUpgrade().getType()).setY(shipY);
                    upgradeIcons.get(cell.value().getUpgrade().getType()).draw(batch);
                }
            }
        }

        if (model.laserExists) { // TODO: refactor, I beg thee
            laser.setX(model.getLaser().getX() + 0.375f);
            laser.setY(model.getLaser().getY() + 0.875f);
            laser.draw(batch);

            laserUpdateTimer += delta;
            if (laserUpdateTimer >= laserUpdateCutoff) {
                laserUpdateTimer = 0f;
                model.moveLaser();
            }
        }

        // fontBold.draw(batch, "Hello, World!", 1f, 1f);
        // fontRegular.draw(batch, "The helloest of Worlds!", 2f, 2f);
        batch.end();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
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
        Gdx.input.setInputProcessor(controller);
    }

}
