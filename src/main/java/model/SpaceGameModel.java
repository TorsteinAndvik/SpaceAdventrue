package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import controller.ControllableSpaceGameModel;
import controller.audio.AudioCallback;
import controller.audio.SoundEffect;
import grid.GridCell;
import model.Animation.AnimationCallback;
import model.Animation.AnimationStateImpl;
import model.Animation.AnimationType;
import model.Globals.Collectable;
import model.Globals.Collidable;
import model.Globals.Damageable;
import model.Score.BasicScoreFormula;
import model.Score.GameStats;
import model.Score.ScoreBoard;
import model.Score.SystemUserNameProvider;
import model.ShipComponents.ShipFactory;
import model.ShipComponents.Components.Turret;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.Diamond;
import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;
import model.SpaceCharacters.SpaceBody;
import model.SpaceCharacters.Ships.SpaceShip;
import model.ai.EnhancedLerpBrain;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;
import view.ViewableSpaceGameModel;

public class SpaceGameModel implements ViewableSpaceGameModel, ControllableSpaceGameModel {

    private Player player;
    private final LinkedList<SpaceShip> spaceShips;
    private final HitDetection hitDetection;
    private final LinkedList<Asteroid> asteroids;
    private final LinkedList<Bullet> lasers;
    private final LinkedList<Collectable> collectables;
    private Pool<Bullet> laserPool;

    private final Matrix3 rotationMatrix;
    private final Matrix4 transformMatrix;

    private AnimationCallback animationCallback;
    private ScreenBoundsProvider screenBoundsProvider;
    private AudioCallback audioCallback;

    private final ScoreBoard scoreBoard;
    private boolean scoreSubmitted;
    private float timeSurvived;
    private int objectsDestroyed;
    private RandomAsteroidFactory randomAsteroidFactory;
    private DirectionalAsteroidFactory directionalAsteroidFactory;
    private float asteroidTimer = asteroidSpawnTimer() / 2f; // make first wave spawn earlier

    private float enemySpawnTimer = 0f;
    private int spawnedShipCounter = 0;

    private boolean gameOver = false;
    private float timeSinceGameOver = 0f;

    private final Random rng = new Random();
    private DiamondFactory diamondFactory;

    public SpaceGameModel() {

        setupPlayer();
        this.spaceShips = new LinkedList<>();
        this.spaceShips.add(player);

        this.asteroids = new LinkedList<>();
        this.lasers = new LinkedList<>();
        this.collectables = new LinkedList<>();

        this.hitDetection = new HitDetection(this);
        scoreBoard = new ScoreBoard(new BasicScoreFormula());
        timeSurvived = 0;
        objectsDestroyed = 0;
        scoreSubmitted = false;

        createDiamondFactory();
        createAsteroidFactory();
        createLaserPool();

        registerColliders();

        this.rotationMatrix = new Matrix3();
        this.transformMatrix = new Matrix4();
    }

    private void createDiamondFactory() {
        int diamondPreFill = 20;
        diamondFactory = new DiamondFactory();
        diamondFactory.fill(diamondPreFill);
    }

    private void createAsteroidFactory() {
        int asteroidPreFill = 30;
        randomAsteroidFactory = new RandomAsteroidFactory();
        randomAsteroidFactory.setShip(player);
        randomAsteroidFactory.fill(asteroidPreFill);
        directionalAsteroidFactory = new DirectionalAsteroidFactory();
        directionalAsteroidFactory.setShip(player);
        directionalAsteroidFactory.fill(asteroidPreFill);

    }

    private void createLaserPool() {
        int laserPreFill = 60;
        this.laserPool = new Pool<>() {
            @Override
            protected Bullet newObject() {
                return new Bullet("laser", "a laser shot", 0f, 0f, 0f, 0f, 1, 0f, 0f, false);
            }
        };

        laserPool.fill(laserPreFill);
    }

    private void setupPlayer() {
        player = new Player(
                ShipFactory.playerShip(), "player", "the player's spaceship", 8f, 1f);
        player.setRotationSpeed(0f);
    }

    protected void createAsteroids() {
        randomAsteroidFactory.setSpawnPerimeter(screenBoundsProvider.getBounds());
        directionalAsteroidFactory.setSpawnPerimeter(screenBoundsProvider.getBounds());

        if (player.getSpeed() > 0.75 * PhysicsParameters.maxVelocityLongitudonal) {
            for (Asteroid asteroid : directionalAsteroidFactory.getAsteroidShower()) {
                asteroids.add(asteroid);
                hitDetection.addCollider(asteroid);
            }
        } else {
            for (Asteroid asteroid : randomAsteroidFactory.getAsteroidShower()) {
                asteroids.add(asteroid);
                hitDetection.addCollider(asteroid);
            }
        }
    }

    private void registerColliders() {
        hitDetection.addColliders(spaceShips);
        hitDetection.addColliders(asteroids);
    }

    private Bullet addLaser(float x, float y, float speed, float angle, float radius,
            boolean isPlayerLaser) {
        Bullet laser = laserPool.obtain();
        laser.init(x, y, speed, angle, radius, isPlayerLaser);
        lasers.addLast(laser);
        hitDetection.addCollider(laser);
        return laser;
    }

    @Override
    public void update(float delta) {
        if (gameOver) {
            timeSinceGameOver += delta;
            return;
        }

        timeSurvived += delta;
        if (player.isDestroyed() && !scoreSubmitted) {
            submitScore();
        }

        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
        }

        Iterator<Bullet> laserIterator = lasers.iterator();
        while (laserIterator.hasNext()) {
            Bullet laser = laserIterator.next();
            laser.update(delta);
            if (cullSpaceBody(laser, 3f)) {// Remove if too distant to player
                hitDetection.removeCollider(laser);
                laserPool.free(laser);
                laserIterator.remove();
            }
        }

        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid iter = asteroidIterator.next();
            iter.update(delta);
            if (cullSpaceBody(iter, 5f)) {// Remove if too distant to player
                hitDetection.removeCollider(iter);
                randomAsteroidFactory.free(iter);
                directionalAsteroidFactory.free(iter);
                asteroidIterator.remove();
            }
        }

        asteroidTimer += delta;
        if (asteroidTimer > asteroidSpawnTimer()) {
            createAsteroids();
            asteroidTimer = 0f;
        }

        enemySpawnTimer += delta;
        if (enemySpawnTimer > enemySpawnTimer()) {
            spawnRandomShip();
            spawnedShipCounter++;
            enemySpawnTimer = 0f;
        }

        for (SpaceShip spaceShip : spaceShips) {
            spaceShip.update(delta);
            handleShootingLogic(spaceShip);
        }

        // cull distant enemies
        Iterator<SpaceShip> shipIterator = spaceShips.iterator();
        if (shipIterator.hasNext()) {
            shipIterator.next(); // skip player ship
            while (shipIterator.hasNext()) {
                SpaceShip iter = shipIterator.next();
                if (cullSpaceBody(iter, iter.getProximityRadius())) {// Remove if too distant to player
                    hitDetection.removeCollider(iter);
                    shipIterator.remove();
                }
            }
        }

        hitDetection.checkCollisions();
    }

    private float asteroidSpawnTimer() {
        return 12f;
    }

    private float enemySpawnTimer() {
        return Math.min(30f, 5f + 2.5f * spawnedShipCounter);
    }

    /**
     * Remove an object if it moves out of range.
     *
     * @param body   the <code>SpaceBody</code> object to potentially remove.
     * @param offset an additional distance the object needs to exceed before
     *               deletion
     */
    private boolean cullSpaceBody(SpaceBody body, float offset) {
        Rectangle bounds = this.screenBoundsProvider.getBounds();

        return (body.getX() + body.getRadius() + offset < bounds.x
                || body.getY() + body.getRadius() + offset < bounds.y
                || body.getX() - body.getRadius() - offset > bounds.x + bounds.width
                || body.getY() - body.getRadius() - offset > bounds.y + bounds.height);
    }

    public void handleCollision(Collidable A, Collidable B) {
        if (HitDetection.isFriendlyFire(A, B)) {
            return;
        }

        if (handleCollection(A, B)) {
            return;
        }

        boolean destroyA = false;
        boolean destroyB = false;
        SpaceCalculator.crash(A, B);

        if (B instanceof Damageable b) {
            destroyB = b.isDestroyed();
        }

        if (A instanceof Damageable a) {
            destroyA = a.isDestroyed();
        }

        if (destroyA) {
            if (B instanceof Bullet) {
                if (A instanceof SpaceBody && !(A instanceof Bullet) && !(A instanceof Player)) {

                    Collectable diamond = diamondFactory.spawn((SpaceBody) A);
                    collectables.add(diamond);
                    hitDetection.addCollider(diamond);
                    objectsDestroyed++;
                }
            }
            remove(A, true);
        }

        if (destroyB) {
            if (A instanceof Bullet) {
                if (B instanceof SpaceBody && !(B instanceof Bullet) && !(B instanceof Player)) {
                    Diamond diamond = diamondFactory.spawn((SpaceBody) B);
                    collectables.add(diamond);
                    hitDetection.addCollider(diamond);
                }
            }
            remove(B, true);
        }
    }

    private boolean handleCollection(Collidable A, Collidable B) {
        if (A instanceof Collectable c && B instanceof Player p) {

            p.getInventory().addResource(c);
            remove(c, false);
            return true;
        }
        if (B instanceof Collectable c && A instanceof Player p) {

            p.getInventory().addResource(c);
            remove(c, false);
            return true;
        }
        return false;
    }

    public void handleShipProximity(SpaceShip shipA, SpaceShip shipB) {
        if (shipA.isPlayerShip() || shipB.isPlayerShip()) {
            return;
        }

        notifyBrains((EnemyShip) shipA, (EnemyShip) shipB);
    }

    private void notifyBrains(EnemyShip shipA, EnemyShip shipB) {
        if (shipA.getBrain() instanceof EnhancedLerpBrain brainA) {
            brainA.nearCollision(shipB);
        }

        if (shipB.getBrain() instanceof EnhancedLerpBrain brainB) {
            brainB.nearCollision(shipA);
        }
    }

    private void remove(Collidable c, boolean drawExplosion) {
        hitDetection.removeCollider(c);
        if (c instanceof SpaceBody sb) {
            switch (sb.getCharacterType()) {
                case ASTEROID:
                    if (drawExplosion) {
                        addAnimationState(c, AnimationType.EXPLOSION);
                    }
                    for (Asteroid asteroid : asteroids) {
                        if (asteroid == c) {
                            asteroids.remove(asteroid);
                            randomAsteroidFactory.free(asteroid);
                            directionalAsteroidFactory.free(asteroid);
                            break;
                        }
                    }
                    playAudio(SoundEffect.SHIP_EXPLOSION_SMALL);
                    break;

                case BULLET:
                    if (drawExplosion) {
                        addAnimationState(c, AnimationType.EXPLOSION);
                    }
                    for (Bullet laser : lasers) {
                        if (laser == c) {
                            lasers.remove(laser);
                            laserPool.free(laser);
                            break;
                        }
                    }

                case DIAMOND:
                    for (Collectable collectable : this.collectables) {
                        if (collectable == c) {
                            collectables.remove(c);
                            if (c instanceof Diamond d) {
                                diamondFactory.free(d);
                            }
                            break;
                        }
                    }

                case ENEMY_SHIP:
                    for (SpaceShip ship : this.spaceShips) {
                        if (ship == c) {
                            if (drawExplosion) {
                                addAnimationState(ship.getAbsoluteCenterOfMass().x(),
                                        ship.getAbsoluteCenterOfMass().y(),
                                        ship.getRadius(), AnimationType.EXPLOSION);
                            }
                            if (ship.getMass() > 10f) {
                                playAudio(SoundEffect.SHIP_EXPLOSION_BIG);
                            } else {
                                playAudio(SoundEffect.SHIP_EXPLOSION_SMALL);
                            }
                            spaceShips.remove(c);
                            break;
                        }
                    }
                    break;

                case PLAYER:
                    if (drawExplosion) {
                        addAnimationState(getPlayerCenterOfMass().x(), getPlayerCenterOfMass().y(), player.getRadius(),
                                AnimationType.EXPLOSION);
                    }
                    if (player.getMass() > 10f) {
                        playAudio(SoundEffect.SHIP_EXPLOSION_BIG);
                    } else {
                        playAudio(SoundEffect.SHIP_EXPLOSION_SMALL);
                    }

                    gameOver = true;
                    break;

                default:
                    break;
            }
        }
    }

    private void addAnimationState(Collidable c, AnimationType type) {
        animationCallback.addAnimationState(new AnimationStateImpl(c, type));
    }

    private void addAnimationState(float x, float y, float radius, AnimationType type) {
        animationCallback.addAnimationState(new AnimationStateImpl(x, y, radius, type));
    }

    private void playAudio(SoundEffect soundEffect) {
        if (audioCallback != null) {
            audioCallback.play(soundEffect);
        }
    }

    private void submitScore() {
        scoreBoard.submitScore(new SystemUserNameProvider(), getGameStats());
        scoreSubmitted = true;
    }

    private GameStats getGameStats() {
        return new GameStats(timeSurvived, objectsDestroyed, player.getResourceValue(),
                player.getInventory().getResourceCount());
    }

    public void playerShoot() {
        setShipToShoot(player);
    }

    protected void setShipToShoot(SpaceShip ship) {
        ship.setToShoot(true);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public float timeSinceGameOver() {
        return timeSinceGameOver;
    }

    protected void handleShootingLogic(SpaceShip ship) {
        for (GridCell<Turret> turretCell : ship.getTurretGridCells()) {

            if (!turretCell.value().shoot()) {
                continue;
            }

            float x = (float) turretCell.pos().col() + Turret.turretBarrelLocation().x();
            float y = (float) turretCell.pos().row() + Turret.turretBarrelLocation().y();
            FloatPair point = SpaceCalculator.rotatePoint(x, y, ship.getRelativeCenterOfMass(),
                    ship.getAbsoluteCenterOfMass(), ship.getRotationAngle());

            addLaser(point.x(), point.y(), PhysicsParameters.laserVelocity, ship.getRotationAngle() + 90f,
                    0.125f, ship.isPlayerShip()).setSourceID(ship.getID());

            int effect = rng.nextInt(3);
            if (effect == 0) {
                playAudio(SoundEffect.LASER_0);
            } else if (effect == 1) {
                playAudio(SoundEffect.LASER_1);
            } else {
                playAudio(SoundEffect.LASER_2);
            }
        }
    }

    /**
     * Gets the transformed position of a ship based on its rotation
     *
     * @param ship the ship to calculate position for
     * @return the matrix transformation for this ship
     */
    public Matrix4 getShipTransformMatrix(SpaceShip ship) {
        float angle = ship.getRotationAngle();

        // reset the transform matrix
        transformMatrix.idt();

        // translate the transformation matrix to the ship's center of rotation
        FloatPair cm = ship.getShipStructure().getCenterOfMass();
        float x = ship.getX() + cm.x();
        float y = ship.getY() + cm.y();

        transformMatrix.translate(x, y, 0f);

        // apply rotation
        rotationMatrix.setToRotation(angle);
        transformMatrix.mul(new Matrix4().set(rotationMatrix));

        // undo the translation
        transformMatrix.translate(-x, -y, 0f);
        return new Matrix4(transformMatrix);
    }

    /**
     * Gets the center of mass coordinates for the player
     *
     * @return the x,y coordinates of the player's center of mass
     */
    public FloatPair getPlayerCenterOfMass() {
        return player.getAbsoluteCenterOfMass();
    }

    public void spawnRandomShip() {
        int numFuselage = 2 + spawnedShipCounter / 2;
        int numUpgrades = rng.nextInt(Math.max(2, numFuselage / 2), numFuselage + 1);
        int stageUpgradeBudget = getScore() / 500;

        Rectangle spawnPerimeter = screenBoundsProvider.getBounds();

        float x, y;
        int side = rng.nextInt(4);
        if (side == 0) { // Top
            y = spawnPerimeter.y + spawnPerimeter.height + numFuselage * PhysicsParameters.fuselageRadius;
            x = rng.nextFloat(spawnPerimeter.x - numFuselage * PhysicsParameters.fuselageRadius,
                    spawnPerimeter.x + spawnPerimeter.width + numFuselage * PhysicsParameters.fuselageRadius);

        } else if (side == 1) { // Right
            x = spawnPerimeter.x + spawnPerimeter.width + numFuselage * PhysicsParameters.fuselageRadius;
            y = rng.nextFloat(spawnPerimeter.y - numFuselage * PhysicsParameters.fuselageRadius,
                    spawnPerimeter.y + spawnPerimeter.height + numFuselage * PhysicsParameters.fuselageRadius);

        } else if (side == 2) { // Bot
            y = spawnPerimeter.y - numFuselage * PhysicsParameters.fuselageRadius;
            x = rng.nextFloat(spawnPerimeter.x - numFuselage * PhysicsParameters.fuselageRadius,
                    spawnPerimeter.x + spawnPerimeter.width + numFuselage * PhysicsParameters.fuselageRadius);

        } else { // Left
            x = spawnPerimeter.x - numFuselage * PhysicsParameters.fuselageRadius;
            y = rng.nextFloat(spawnPerimeter.y - numFuselage * PhysicsParameters.fuselageRadius,
                    spawnPerimeter.y + spawnPerimeter.height + numFuselage * PhysicsParameters.fuselageRadius);
        }

        float angle = SpaceCalculator.angleBetweenPoints(player.getAbsoluteCenterOfMass(), new FloatPair(x, y));
        EnemyShip enemyShip = new EnemyShip(
                ShipFactory.generateShipStructure(numFuselage, numUpgrades),
                "enemy",
                "an enemy ship",
                x,
                y,
                angle);

        enemyShip.setBrain(new EnhancedLerpBrain(enemyShip, player));
        ShipFactory.upgradeStages(enemyShip, stageUpgradeBudget);

        spaceShips.addLast(enemyShip);
        hitDetection.addCollider(enemyShip);
    }

    @Override
    public void setAccelerateForward(boolean accelerate) {
        player.setAccelerateForward(accelerate);
    }

    @Override
    public void setAccelerateBackward(boolean accelerate) {
        player.setAccelerateBackward(accelerate);
    }

    @Override
    public void setAccelerateCounterClockwise(boolean accelerate) {
        player.setAccelerateCounterClockwise(accelerate);
    }

    @Override
    public void setAccelerateClockwise(boolean accelerate) {
        player.setAccelerateClockwise(accelerate);
    }

    @Override
    public int getScore() {
        return scoreBoard.getScore(getGameStats());
    }

    @Override
    public List<SpaceShip> getSpaceShips() {
        return this.spaceShips;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public List<Asteroid> getAsteroids() {
        return this.asteroids;
    }

    @Override
    public List<Bullet> getLasers() {
        return this.lasers;
    }

    @Override
    public List<Collectable> getCollectables() {
        return this.collectables;
    }

    @Override
    public void setAnimationCallback(AnimationCallback animationCallback) {
        this.animationCallback = animationCallback;
    }

    @Override
    public void setScreenBoundsProvider(ScreenBoundsProvider screenBoundsProvider) {
        this.screenBoundsProvider = screenBoundsProvider;
    }

    @Override
    public void setAudioCallback(AudioCallback audioCallback) {
        this.audioCallback = audioCallback;
    }
}
