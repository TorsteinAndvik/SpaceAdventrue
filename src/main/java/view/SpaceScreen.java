package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import controller.SpaceGameScreenController;
import grid.GridCell;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.SpaceShip;
import model.SpaceGameModel;
import model.utils.FloatPair;
import java.util.HashMap;

public class SpaceScreen implements Screen {

    final SpaceGame game;
    final SpaceGameModel model;
    private final SpaceGameScreenController controller;
    private final SpriteBatch batch;
    private final FitViewport viewport;
    private BitmapFont fontBold; // Agency FB Bold
    private BitmapFont fontRegular; // Agency FB Regular
    private final AssetManager manager;

    private Sprite asteroidLarge;
    private Sprite asteroidSmall;
    private Sprite laser;

    private Sprite fuselagePlayer;
    private Sprite fuselageEnemy;

    private HashMap<UpgradeType, Sprite> upgradeIcons;

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
        asteroidLarge = createSprite("images/space/asteroid_0.png", 2, 2);
        asteroidSmall = createSprite("images/space/asteroid_1.png", 1, 1);
        laser = createSprite("images/space/laser_shot_0.png", 0.25f, 0.25f);

        fuselagePlayer = createSprite("images/upgrades/fuselage_alt_stage_0.png", 1, 1);
        fuselageEnemy = createSprite("images/upgrades/fuselage_enemy_stage_0.png", 1, 1);
    }

    private void setupUpgradeHashMap() {
        upgradeIcons = new HashMap<>();
        upgradeIcons.put(UpgradeType.TURRET,
            createSprite("images/upgrades/turret_laser_stage_0.png", 1, 1));
        upgradeIcons.put(UpgradeType.THRUSTER,
            createSprite("images/upgrades/rocket_stage_0.png", 1, 1));
        upgradeIcons.put(UpgradeType.SHIELD,
            createSprite("images/upgrades/shield_stage_0.png", 1, 1));
    }

    private Sprite createSprite(String path, float width, float height) {
        Sprite sprite = new Sprite(manager.get(path, Texture.class));
        sprite.setSize(width, height);
        sprite.setOrigin(width / 2, height / 2);
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

        model.rotateEnemy(delta); // TODO: Remove this once proper model is in place
        // view should only send model.update(delta) via controller in future
        batch.begin();

        // draw asteroids
        for (Asteroid asteroid : model.getAsteroids()) {
            Sprite asteroidSprite = asteroid.isLarge ? asteroidLarge : asteroidSmall;
            asteroidSprite.setRotation(asteroid.getRotationAngle());
            asteroidSprite.setX(asteroid.getX());
            asteroidSprite.setY(asteroid.getY());
            asteroidSprite.draw(batch);
        }

        for (int i = 0; i < model.getSpaceShips().length; i++) {
            SpaceShip ship = model.getSpaceShips()[i];

            // Get transformation matrix from model
            Matrix4 transformMatrix = model.getShipTransformMatrix(ship);
            batch.setTransformMatrix(transformMatrix);

            for (GridCell<Fuselage> cell : ship.getShipStructure().iterable()) {
                if (cell.value() == null) {
                    continue;
                }

                float shipX = ship.getX() + cell.pos().col();
                float shipY = ship.getY() + cell.pos().row();
                if (ship.isPlayerShip()) {
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

            // draw center of mass as a laser, for testing purposes
            FloatPair cm = model.getShipCenterOfMass(ship);
            laser.setX(cm.x() - 0.5f * laser.getWidth());
            laser.setY(cm.y() - 0.5f * laser.getHeight());
            laser.draw(batch);
        }

        // Reset the transform matrix to the identity matrix
        batch.setTransformMatrix(new Matrix4().idt());

        if (model.laserExists) { // TODO: refactor once cannon-firing logic is added to model
            laser.setX(model.getLaser().getX() - 0.125f);
            laser.setY(model.getLaser().getY() + 0.875f);
            laser.draw(batch);
        }

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
