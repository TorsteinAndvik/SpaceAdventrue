package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import controller.SpaceGameScreenController;
import grid.GridCell;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.SpaceShip;
import model.constants.PhysicsParameters;
import model.SpaceGameModel;
import model.Animation.AnimationCallback;
import model.Animation.AnimationState;
import model.Animation.AnimationType;
import model.utils.FloatPair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SpaceScreen implements Screen, AnimationCallback {

    final SpaceGame game;
    final SpaceGameModel model;
    private final SpaceGameScreenController controller;
    private final SpriteBatch batch;
    private final FitViewport viewport;

    private final OrthographicCamera camera;
    private Vector3 cameraPosition;
    private Vector2 lerpPosition;
    private final float zoomMin = 1f;
    private final float zoomMax = 2f;

    private BitmapFont fontBold; // Agency FB Bold
    private BitmapFont fontRegular; // Agency FB Regular
    private final AssetManager manager;

    private Sprite asteroidLarge;
    private Sprite asteroidSmall;
    private Sprite laser;

    private Sprite fuselagePlayer;
    private Sprite fuselageEnemy;

    private HashMap<UpgradeType, Sprite> upgradeIcons;

    // Animations
    private LinkedList<AnimationState> animationStates;
    private HashMap<AnimationType, Animation<TextureRegion>> animationMap;

    // TODO: Add a list of AnimationStates
    // TODO: loop over list of AnimationStates in render to draw animations
    // TODO: remove AnimationStates from list once animation is done looping.

    public SpaceScreen(final SpaceGame game, final SpaceGameModel model) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();
        this.viewport = game.getFitViewport();
        this.camera = (OrthographicCamera) viewport.getCamera();

        this.model = model;
        this.controller = new SpaceGameScreenController(this, model, game);

        setupFonts();
        loadSprites();
        setupAnimationHashMap();
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

    private void setupAnimationHashMap() {
        animationStates = new LinkedList<AnimationState>();
        animationMap = new HashMap<>();

        TextureAtlas atlas = manager.get("images/animations/explosion_A.atlas",
                TextureAtlas.class);

        Animation<TextureRegion> explosionAnimation = new Animation<TextureRegion>(1f / 12f,
                atlas.findRegions("explosion"), PlayMode.NORMAL);

        animationMap.put(AnimationType.EXPLOSION, explosionAnimation);
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
        Sprite sprite = new Sprite(loadTexture(path));
        sprite.setSize(width, height);
        sprite.setOrigin(width / 2, height / 2);
        return sprite;
    }

    private Texture loadTexture(String path) {
        return manager.get(path, Texture.class);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        ScreenUtils.clear(Color.DARK_GRAY);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        updateCamera();

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

            laser.setX(asteroid.getX());
            laser.setY(asteroid.getY());
            laser.draw(batch);
        }

        for (SpaceShip ship : model.getSpaceShips()) {
            // Get transformation matrix from model
            Matrix4 transformMatrix = model.getShipTransformMatrix(ship);
            batch.setTransformMatrix(transformMatrix);

            for (GridCell<Fuselage> cell : ship.getShipStructure()) {
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

        for (Bullet laser : model.getLasers()) {
            this.laser.setX(laser.getX() - 0.125f);
            this.laser.setY(laser.getY() + 0.875f);
            this.laser.draw(batch);
        }

        // Draw explosion animations:
        Iterator<AnimationState> animationStatesIterator = animationStates.iterator();
        while (animationStatesIterator.hasNext()) {
            AnimationState state = animationStatesIterator.next();
            state.update(delta);

            Animation<TextureRegion> animation = animationMap.get(state.getAnimationType());

            if (animation.isAnimationFinished(state.getStateTime())) {
                animationStatesIterator.remove();
            } else {
                TextureRegion currentFrame = animation.getKeyFrame(state.getStateTime());
                batch.draw(currentFrame, state.getX(), state.getY(), state.getWidth(), state.getHeight());
            }
        }

        batch.end();
    }

    /**
     * perform one-dimensional linear interpolation
     */
    private float lerp(float source, float target, float alpha) {
        float alphaInverse = 1f - alpha;
        return alpha * target + alphaInverse * source;
    }

    private FloatPair lerp(FloatPair source, FloatPair target, float alpha) {
        float x = lerp(source.x(), target.x(), alpha);
        float y = lerp(source.y(), target.y(), alpha);
        return new FloatPair(x, y);
    }

    private FloatPair lerp(Vector3 source, FloatPair target, float alpha) {
        FloatPair sourceFloatPair = new FloatPair(source.x, source.y);
        return lerp(sourceFloatPair, target, alpha);
    }

    private void updateCamera() {
        cameraLerpToPlayer();
        cameraZoom();
    }

    private void cameraLerpToPlayer() {
        FloatPair newPosition = lerp(camera.position, model.getPlayerSpaceShip().getCenter(), 0.1f);
        setCameraPosition(newPosition);
    }

    private void setCameraPosition(FloatPair pos) {
        camera.position.set(pos.x(), pos.y(), 0f);
    }

    private float getZoomLevel() {
        float velocityRatio = model.getPlayerSpaceShip().getSpeed() / PhysicsParameters.maxVelocityLongitudonal;
        float zoomRange = zoomMax - zoomMin;
        return zoomMin + velocityRatio * zoomRange;
    }

    private void cameraZoom() {
        float zoom = lerp(camera.zoom, getZoomLevel(), 0.01f);
        camera.zoom = zoom;
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
        viewport.update(width, height, false);
        camera.position.set(model.getPlayerSpaceShip().getCenter().x(), model.getPlayerSpaceShip().getCenter().y(), 0f);
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void addAnimationState(AnimationState state) {
        this.animationStates.addFirst(state);
    }

}
