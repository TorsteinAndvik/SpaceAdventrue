package view.screens;

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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import box2dLight.RayHandler;
import controller.SpaceScreenController;
import grid.GridCell;
import model.Globals.Collectable;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.Thruster;
import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.Diamond;
import model.SpaceCharacters.Ships.SpaceShip;
import model.constants.PhysicsParameters;
import model.GameStateModel;
import model.ScreenBoundsProvider;
import model.SpaceGameModel;
import model.Animation.AnimationCallback;
import model.Animation.AnimationState;
import model.Animation.AnimationType;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;
import view.Palette;
import view.SpaceGame;
import view.lighting.LaserLight;
import view.lighting.ShipThrusterLightMap;
import view.lighting.ThrusterLight;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SpaceScreen implements Screen, AnimationCallback, ScreenBoundsProvider {

    final SpaceGame game;
    final SpaceGameModel model;
    private final SpaceScreenController controller;
    private final SpriteBatch batch;
    private final ShapeRenderer shape;
    private final ScreenViewport viewport;
    private final ScreenViewport viewportUI;
    private final ExtendViewport bgViewport;
    private BitmapFont fontRegular;
    private BitmapFont fontBold;

    private final OrthographicCamera camera;
    private final float zoomMin = 1f;
    private final float zoomMax = 1.5f;

    private final AssetManager manager;

    private Sprite asteroidLarge;
    private Sprite asteroidSmall;
    private Sprite laser;
    private Sprite diamond;

    private final Map<UpgradeStage, Sprite> enemyShipSprites = new HashMap<>();
    private final Map<UpgradeType, Map<UpgradeStage, Sprite>> shipSprites = new HashMap<>();

    // Background
    private TextureRegion[] background;
    private float[] backgroundParallax;
    private float[] backgroundDrift;

    // Animations
    private LinkedList<AnimationState> animationStates;
    private Map<AnimationType, Animation<TextureRegion>> animationMap;

    // Lighting
    public static final RayHandler rayHandler = new RayHandler(null);
    private LinkedList<LaserLight> laserLights;
    private Pool<LaserLight> laserLightPool;
    private ShipThrusterLightMap shipThrusterLightMap;

    // Game over text layout
    private GlyphLayout gameOverLayout;

    // Hitboxes (for testing/debugging)
    private final boolean showHitboxes = false;

    public SpaceScreen(final SpaceGame game, final GameStateModel gameStateModel) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.shape = this.game.getShapeRenderer();
        this.manager = this.game.getAssetManager();
        this.viewport = game.getScreenViewport();
        this.bgViewport = game.getExtendViewport();
        this.camera = (OrthographicCamera) viewport.getCamera();

        this.viewportUI = new ScreenViewport();
        viewportUI.setUnitsPerPixel(viewport.getUnitsPerPixel());

        this.model = gameStateModel.getSpaceGameModel();
        this.controller = new SpaceScreenController(this, gameStateModel, game);

        setupBackground();
        setupSprites();
        setupAnimationHashMap();
        setupLighting();
        setupFonts();
    }

    private void setupBackground() {
        this.background = new TextureRegion[6];

        background[0] = new TextureRegion(
                manager.get("images/space/background/bkgd_1.png", Texture.class));
        background[1] = new TextureRegion(
                manager.get("images/space/background/bkgd_2.png", Texture.class));
        background[2] = new TextureRegion(
                manager.get("images/space/background/bkgd_3.png", Texture.class));
        background[3] = new TextureRegion(
                manager.get("images/space/background/bkgd_4.png", Texture.class));
        background[4] = new TextureRegion(
                manager.get("images/space/background/bkgd_6.png", Texture.class));
        background[5] = new TextureRegion(
                manager.get("images/space/background/bkgd_7.png", Texture.class));

        this.backgroundParallax = new float[background.length];
        this.backgroundDrift = new float[background.length];

        int driftOffset = 4; // must be in interval [0, background.length - 1]

        for (int i = 0; i < background.length; i++) {
            background[i].getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            backgroundParallax[i] = 0.005f + ((float) Math.pow(i, 1.4)) / 1000f;
            if (i < driftOffset) {
                backgroundDrift[i] = 0f;
            } else {
                backgroundDrift[i] = backgroundParallax[i - driftOffset] / 2f;
            }
        }
    }

    private void setupSprites() {
        asteroidLarge = createSprite("images/space/asteroid_0.png", 2f, 2f);
        asteroidSmall = createSprite("images/space/asteroid_1.png", 1f, 1f);
        laser = createSprite("images/space/laser_shot_0.png", 0.25f, 0.25f);

        // setup upgrades and player fuselage
        for (UpgradeType type : UpgradeType.values()) {
            Map<UpgradeStage, Sprite> stageSprites = new HashMap<>();
            String basePath = "images/upgrades/" + type.name().toLowerCase() + "_stage_";
            for (UpgradeStage stage : UpgradeStage.values()) {
                String path = basePath + stage.ordinal() + ".png";

                Sprite sprite = createSprite(path, 1f, 1f);

                stageSprites.put(stage, sprite);
            }
            shipSprites.put(type, stageSprites);
        }

        // setup enemy fuselage
        String basePath = "images/upgrades/fuselage_enemy_stage_";
        for (UpgradeStage stage : UpgradeStage.values()) {
            String path = basePath + stage.ordinal() + ".png";

            Sprite sprite = createSprite(path, 1f, 1f);

            enemyShipSprites.put(stage, sprite);
        }

        diamond = createSprite("images/space/diamond.png", 1f, 1f);
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

    private void setupLighting() {
        rayHandler.setAmbientLight(Palette.AMBIENT_LIGHT);

        this.laserLights = new LinkedList<>();
        this.laserLightPool = new Pool<>() {
            @Override
            protected LaserLight newObject() {
                return new LaserLight();
            }
        };
        laserLightPool.fill(300);

        this.shipThrusterLightMap = new ShipThrusterLightMap(50);
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

    private void setupFonts() {
        fontRegular = manager.get("fonts/PixelOperatorMonoHB.ttf", BitmapFont.class);

        // fonts are set as [integer]pt, need to scale them to our viewport by ratio of
        // viewport height to screen height in order to use world-unit sized font

        fontRegular.setUseIntegerPositions(false);
        fontRegular.getData().setScale(viewportUI.getUnitsPerPixel());
        fontRegular.setColor(Palette.WHITE);

        fontBold = manager.get("fonts/PixelOperatorMono-Bold.ttf", BitmapFont.class);
        fontBold.setUseIntegerPositions(false);
        fontBold.getData().setScale(viewportUI.getUnitsPerPixel());
        fontBold.setColor(Palette.WHITE);

        gameOverLayout = new GlyphLayout();
        gameOverLayout.setText(fontBold, "GAME OVER");
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        updateCamera(delta);
        updateLightCounts();

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

        batch.begin();

        // draw asteroids
        for (Asteroid asteroid : model.getAsteroids()) {
            Sprite asteroidSprite = asteroid.isLarge() ? asteroidLarge : asteroidSmall;
            asteroidSprite.setRotation(asteroid.getRotationAngle());
            asteroidSprite.setCenterX(asteroid.getX());
            asteroidSprite.setCenterY(asteroid.getY());

            asteroidSprite.draw(batch);
        }

        // draw ships
        for (SpaceShip ship : model.getSpaceShips()) {
            // Get transformation matrix from model
            Matrix4 transformMatrix = model.getShipTransformMatrix(ship);
            batch.setTransformMatrix(transformMatrix);

            Iterator<ThrusterLight> shipThrusterLightsIterator = shipThrusterLightMap.get(ship).iterator();
            for (GridCell<Fuselage> cell : ship.getShipStructure()) {
                if (cell.value() == null) {
                    continue;
                }

                float shipX = ship.getX() + cell.pos().col();
                float shipY = ship.getY() + cell.pos().row();
                if (ship.isPlayerShip()) {
                    if (model.isGameOver()) {
                        continue;
                    }
                    Sprite fuselagePlayer = shipSprites.get(cell.value().getType()).get(cell.value().getStage());
                    fuselagePlayer.setCenterX(shipX);
                    fuselagePlayer.setCenterY(shipY);
                    fuselagePlayer.draw(batch);
                } else {
                    Sprite fuselageEnemy = enemyShipSprites.get(cell.value().getStage());
                    fuselageEnemy.setCenterX(shipX);
                    fuselageEnemy.setCenterY(shipY);
                    fuselageEnemy.draw(batch);
                }

                if (cell.value().getUpgrade() != null) {
                    Sprite upgradeSprite = shipSprites.get(cell.value().getUpgrade().getType())
                            .get(cell.value().getUpgrade().getStage());
                    upgradeSprite.setCenterX(shipX);
                    upgradeSprite.setCenterY(shipY);
                    upgradeSprite.draw(batch);

                    if (cell.value().getUpgrade().getType() == UpgradeType.THRUSTER) {

                        FloatPair point = SpaceCalculator.rotatePoint(
                                cell.pos().col() + Thruster.thrusterFlameLocation().x(),
                                cell.pos().row() + Thruster.thrusterFlameLocation().y(),
                                ship.getRelativeCenterOfMass(),
                                ship.getAbsoluteCenterOfMass(),
                                ship.getRotationAngle());

                        ThrusterLight light = shipThrusterLightsIterator.next();

                        light.setPosition(point.x(), point.y());
                        light.setDirection(ship.getRotationAngle() - 90f);
                        if (ship.isPlayerShip()) {
                            if (light.isActive() != ship.isAccelerating()) {
                                light.setActive(ship.isAccelerating());
                            }
                        }
                    }
                }
            }
        }

        // Reset the transform matrix to the identity matrix
        batch.setTransformMatrix(new Matrix4().idt());

        // draw lasers
        Iterator<Bullet> laserIterator = model.getLasers().iterator();
        Iterator<LaserLight> lightsIterator = this.laserLights.iterator();
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

        // Draw diamonds
        for (Collectable collectable : model.getCollectables()) {

            if (collectable instanceof Diamond) {
                this.diamond.setCenterX(collectable.getX());
                this.diamond.setCenterY(collectable.getY());
                this.diamond.draw(batch);
            }
        }

        batch.end();

        // Lighting
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        // Health bars
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeType.Filled);
        for (SpaceShip ship : model.getSpaceShips()) {
            if (ship.isPlayerShip() && model.isGameOver()) {
                continue;
            }
            ship.getHealthBar().draw(shape);
        }
        shape.end();

        // draw hitboxes for testing
        if (showHitboxes) {
            shape.setProjectionMatrix(camera.combined);
            shape.begin(ShapeType.Line);
            shape.setColor(Color.CYAN);
            for (Asteroid asteroid : model.getAsteroids()) {
                shape.circle(asteroid.getX(), asteroid.getY(), asteroid.getRadius(), 100);
            }
            shape.setColor(Color.MAGENTA);
            for (SpaceShip ship : model.getSpaceShips()) {
                shape.circle(
                        ship.getAbsoluteCenterOfMass().x(), ship.getAbsoluteCenterOfMass().y(),
                        ship.getProximityRadius(), 100);
            }
            shape.end();
        }

        // draw UI elements
        viewportUI.apply(true);
        batch.setProjectionMatrix(viewportUI.getCamera().combined);
        batch.begin();
        diamond.setY(viewportUI.getWorldHeight() - 1.06f * diamond.getHeight());
        diamond.setX(-0.15f * diamond.getWidth());
        diamond.draw(batch);
        fontRegular.draw(batch, String.valueOf(model.getPlayer().getInventory().getResourceCount()),
                diamond.getX() + diamond.getWidth(),
                diamond.getY() + 0.45f * diamond.getHeight() + 0.5f * fontRegular.getLineHeight());

        if (model.isGameOver()) {
            float textX = viewportUI.getWorldWidth() / 2f - gameOverLayout.width / 2f;
            float textY = 3f * viewportUI.getWorldHeight() / 4f - gameOverLayout.height / 2f;
            fontBold.draw(batch, "Game Over", textX, textY);
        }

        batch.end();

    }

    private void updateLightCounts() {
        updateLaserLightsCount();
        shipThrusterLightMap.update(model.getSpaceShips());
    }

    private void updateLaserLightsCount() {
        // add lights if necessary
        int lightDiff = model.getLasers().size() - this.laserLights.size();
        for (int i = 0; i < lightDiff; i++) {
            LaserLight light = laserLightPool.obtain();
            light.setActive(true);
            this.laserLights.addFirst(light);
        }

        // remove lights if necessary
        if (lightDiff < 0) {
            Iterator<LaserLight> lightsIterator = this.laserLights.iterator();
            while (lightDiff < 0) {
                LaserLight light = lightsIterator.next();
                laserLightPool.free(light);
                lightsIterator.remove();
                lightDiff++;
            }
        }
    }

    private void updateCamera(float delta) {
        cameraLerpToPlayer(delta);
        cameraZoom(delta);
    }

    private void cameraLerpToPlayer(float delta) {
        FloatPair newPosition = SpaceCalculator.lerp2D(camera.position, model.getPlayerCenterOfMass(),
                6f * delta);
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

    private void cameraZoom(float delta) {
        camera.zoom = SpaceCalculator.lerp1D(camera.zoom, getZoomLevel(), 1.2f * delta);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        for (LaserLight light : this.laserLights) {
            light.setActive(false);
        }

        for (List<ThrusterLight> lightList : shipThrusterLightMap.thrusterLightMap.values()) {
            for (ThrusterLight light : lightList) {
                light.setActive(false);
            }
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        bgViewport.update(width, height, true);
        viewportUI.update(width, height, true);
        setCameraPosition(model.getPlayerCenterOfMass());
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
        controller.reset();
    }

    @Override
    public void addAnimationState(AnimationState state) {
        this.animationStates.addFirst(state);
    }

    @Override
    public Rectangle getBounds() {

        float maxWidth = viewport.getWorldWidth() * zoomMax;
        float maxHeight = viewport.getWorldHeight() * zoomMax;

        return new Rectangle(-maxWidth / 2f + camera.position.x,
                -maxHeight / 2f + camera.position.y, maxWidth, maxHeight);
    }
}
