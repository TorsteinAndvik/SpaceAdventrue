package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import controller.SpaceGameScreenController;
import grid.GridCell;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.SpaceShip;
import model.constants.PhysicsParameters;
import model.ScreenBoundsProvider;
import model.SpaceGameModel;
import model.Animation.AnimationCallback;
import model.Animation.AnimationState;
import model.Animation.AnimationType;
import model.utils.FloatPair;
import view.lighting.LaserLight;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SpaceScreen implements Screen, AnimationCallback, ScreenBoundsProvider {

    final SpaceGame game;
    final SpaceGameModel model;
    private final SpaceGameScreenController controller;
    private final SpriteBatch batch;
    private final ScreenViewport viewport;
    private final ScreenViewport bgViewport;

    private final OrthographicCamera camera;
    private final float zoomMin = 1f;
    private final float zoomMax = 1.5f;

    private final AssetManager manager;

    private Sprite asteroidLarge;
    private Sprite asteroidSmall;
    private Sprite laser;

    private Sprite fuselagePlayer;
    private Sprite fuselageEnemy;

    private HashMap<UpgradeType, Sprite> upgradeIcons;

    // Background
    private TextureRegion[] background;
    private float[] backgroundParallax;
    private float[] backgroundDrift;

    // Animations
    private LinkedList<AnimationState> animationStates;
    private HashMap<AnimationType, Animation<TextureRegion>> animationMap;

    // Lighting
    public static final RayHandler rayHandler = new RayHandler(null);
    private LinkedList<LaserLight> lights;
    private Pool<LaserLight> lightPool;

    public SpaceScreen(final SpaceGame game, final SpaceGameModel model) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();
        this.viewport = game.getScreenViewport();
        this.bgViewport = new ScreenViewport();
        bgViewport.setUnitsPerPixel(viewport.getUnitsPerPixel());
        this.camera = (OrthographicCamera) viewport.getCamera();

        this.model = model;
        this.controller = new SpaceGameScreenController(this, model, game);

        setupBackground();
        setupSprites();
        setupAnimationHashMap();
        setupUpgradeHashMap();
        setupLighting();
    }

    private void setupBackground() {
        this.background = new TextureRegion[6];

        background[0] = new TextureRegion(manager.get("images/space/background/bkgd_1.png", Texture.class));
        background[1] = new TextureRegion(manager.get("images/space/background/bkgd_2.png", Texture.class));
        background[2] = new TextureRegion(manager.get("images/space/background/bkgd_3.png", Texture.class));
        background[3] = new TextureRegion(manager.get("images/space/background/bkgd_4.png", Texture.class));
        background[4] = new TextureRegion(manager.get("images/space/background/bkgd_6.png", Texture.class));
        background[5] = new TextureRegion(manager.get("images/space/background/bkgd_7.png", Texture.class));

        this.backgroundParallax = new float[background.length];
        this.backgroundDrift = new float[background.length];

        int driftOffset = 4; // must be in interval [0, background.length - 1]

        for (int i = 0; i < background.length; i++) {
            background[i].getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            backgroundParallax[i] = 0.005f + ((float) Math.pow(i, 1.4)) / 1000f;
            if (i < driftOffset) {
                backgroundDrift[i] = 0f;
            } else {
                backgroundDrift[i] = backgroundParallax[i - driftOffset];
            }
        }
    }

    private void setupSprites() {
        asteroidLarge = createSprite("images/space/asteroid_0.png", 2, 2);
        // asteroidLarge.setOrigin(1f, 1f);

        asteroidSmall = createSprite("images/space/asteroid_1.png", 1, 1);

        laser = createSprite("images/space/laser_shot_0.png", 0.25f, 0.25f);

        fuselagePlayer = createSprite("images/upgrades/fuselage_alt_stage_0.png", 1, 1);

        fuselageEnemy = createSprite("images/upgrades/fuselage_enemy_stage_0.png", 1, 1);
    }

    private void setupAnimationHashMap() {
        animationStates = new LinkedList<>();
        animationMap = new HashMap<>();

        TextureAtlas atlas = manager.get("images/animations/explosion_A.atlas",
                TextureAtlas.class);

        Animation<TextureRegion> explosionAnimation = new Animation<>(1f / 12f,
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

    private void setupLighting() {
        rayHandler.setAmbientLight(new Color(0f, 0f, 0f, 0.85f));

        this.lights = new LinkedList<>();
        this.lightPool = new Pool<LaserLight>() {
            @Override
            protected LaserLight newObject() {
                return new LaserLight();
            }
        };
        lightPool.fill(300);
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
        updateLaserLightsCount();

        ScreenUtils.clear(Color.BLACK);

        // draw background
        this.bgViewport.apply();
        batch.setProjectionMatrix(bgViewport.getCamera().combined);
        batch.begin();
        for (int i = 0; i < background.length; i++) {
            float parallax = backgroundParallax[i];
            float drift = backgroundDrift[i];
            background[i].scroll(
                    delta * (drift + parallax * model.getPlayer().getVelocity().x),
                    -delta * (drift + parallax * model.getPlayer().getVelocity().y));

            batch.draw(background[i], 0, 0, bgViewport.getWorldWidth(), bgViewport.getWorldHeight());
        }
        batch.end();

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        updateCamera();

        batch.begin();

        // draw asteroids
        for (Asteroid asteroid : model.getAsteroids()) {
            Sprite asteroidSprite = asteroid.isLarge() ? asteroidLarge : asteroidSmall;
            asteroidSprite.setRotation(asteroid.getRotationAngle());
            asteroidSprite.setCenterX(asteroid.getX());
            asteroidSprite.setCenterY(asteroid.getY());

            asteroidSprite.draw(batch);
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
                    fuselagePlayer.setCenterX(shipX);
                    fuselagePlayer.setCenterY(shipY);
                    fuselagePlayer.draw(batch);
                } else {
                    fuselageEnemy.setCenterX(shipX);
                    fuselageEnemy.setCenterY(shipY);
                    fuselageEnemy.draw(batch);
                }

                if (cell.value().getUpgrade() != null) {
                    upgradeIcons.get(cell.value().getUpgrade().getType()).setCenterX(shipX);
                    upgradeIcons.get(cell.value().getUpgrade().getType()).setCenterY(shipY);
                    upgradeIcons.get(cell.value().getUpgrade().getType()).draw(batch);
                }
            }
        }

        // Reset the transform matrix to the identity matrix
        batch.setTransformMatrix(new Matrix4().idt());

        // draw lasers
        Iterator<Bullet> laserIterator = model.getLasers().iterator();
        Iterator<LaserLight> lightsIterator = this.lights.iterator();
        while (laserIterator.hasNext() && lightsIterator.hasNext()) {
            Bullet laser = laserIterator.next();
            this.laser.setRotation(laser.getRotationAngle() - 90f);
            this.laser.setCenterX(laser.getX());
            this.laser.setCenterY(laser.getY());
            this.laser.draw(batch);

            LaserLight light = lightsIterator.next();
            light.setPosition(laser.getX(), laser.getY());
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
                batch.draw(currentFrame, state.getX() - state.getRadius(),
                        state.getY() - state.getRadius(),
                        2f * state.getRadius(), 2f * state.getRadius());
            }
        }

        batch.end();

        // Lighting
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }

    private void updateLaserLightsCount() {
        // add lights if necessary
        int lightDiff = model.getLasers().size() - this.lights.size();
        for (int i = 0; i < lightDiff; i++) {
            LaserLight light = lightPool.obtain();
            light.setActive(true);
            this.lights.addFirst(light);
        }

        // remove lights if necessary
        if (lightDiff < 0) {
            Iterator<LaserLight> lightsIterator = this.lights.iterator();
            while (lightDiff < 0) {
                LaserLight light = lightsIterator.next();
                lightPool.free(light);
                lightsIterator.remove();
                lightDiff++;
            }
        }
        if (model.getLasers().size() != this.lights.size()) {
            System.out.println("ERROR: model.getLasers().size() [" + model.getLasers().size()
                    + "] != this.lights.size() [" + this.lights.size() + "]");
        }
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
        FloatPair newPosition = lerp(camera.position, model.getPlayerCenterOfMass(),
                0.1f);
        setCameraPosition(newPosition);
    }

    private void setCameraPosition(FloatPair pos) {
        camera.position.set(pos.x(), pos.y(), 0f);
    }

    private float getZoomLevel() {
        float velocityRatio = model.getPlayer().getSpeed() / PhysicsParameters.maxVelocityLongitudonal;
        float zoomRange = zoomMax - zoomMin;
        return zoomMin + velocityRatio * zoomRange;
    }

    private void cameraZoom() {
        float zoom = lerp(camera.zoom, getZoomLevel(), 0.02f);
        camera.zoom = zoom;
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
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
        bgViewport.update(width, height, true);
        setCameraPosition(model.getPlayerCenterOfMass());
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

    @Override
    public Rectangle getBounds() {

        float maxWidth = viewport.getWorldWidth() * zoomMax;
        float maxHeight = viewport.getWorldHeight() * zoomMax;

        Rectangle bounds = new Rectangle(-maxWidth / 2f + camera.position.x,
                -maxHeight / 2f + camera.position.y, maxWidth, maxHeight);

        return bounds;
    }
}
