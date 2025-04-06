package model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import controller.ControllableSpaceGameModel;
import grid.CellPosition;
import model.Animation.AnimationCallback;
import model.Animation.AnimationStateImpl;
import model.Animation.AnimationType;
import model.Globals.Collidable;
import model.Globals.Damageable;
import model.ShipComponents.ShipFactory;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.Turret;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.EnemyShip;
import model.SpaceCharacters.Player;
import model.SpaceCharacters.SpaceBody;
import model.SpaceCharacters.SpaceShip;
import model.ai.LerpBrain;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;
import view.ViewableSpaceGameModel;

public class SpaceGameModel implements ViewableSpaceGameModel, ControllableSpaceGameModel {

    private Player player;
    private LinkedList<SpaceShip> spaceShips;
    private final HitDetection hitDetection;
    private LinkedList<Asteroid> asteroids;
    private LinkedList<Bullet> lasers;
    private Pool<Bullet> laserPool;

    private final Matrix3 rotationMatrix;
    private final Matrix4 transformMatrix;

    private AnimationCallback animationCallback;
    private ScreenBoundsProvider screenBoundsProvider;

    private RandomAsteroidFactory randomAsteroidFactory;
    private float asteroidTimer = 0;

    public SpaceGameModel() {

        createSpaceShips();

        this.asteroids = new LinkedList<>();
        this.lasers = new LinkedList<>();

        this.hitDetection = new HitDetection(this);

        createAsteroidFactory(50);
        createLaserPool(300);

        registerColliders();

        this.rotationMatrix = new Matrix3();
        this.transformMatrix = new Matrix4();
    }

    private void createAsteroidFactory(int asteroidPreFill) {
        randomAsteroidFactory = new RandomAsteroidFactory();
        randomAsteroidFactory.setShip(player);
        randomAsteroidFactory.fill(asteroidPreFill);
    }

    private void createLaserPool(int laserPreFill) {
        this.laserPool = new Pool<>() {
            @Override
            protected Bullet newObject() {
                return new Bullet("laser", "a laser shot", 0f, 0f, 0f, 0f, 1, 0f, 0f, false);
            }
        };

        laserPool.fill(laserPreFill);
    }

    private void createSpaceShips() {
        this.player = new Player(
                ShipFactory.playerShip(), "player", "the player's spaceship", 20, 8, 1);
        this.player.setRotationSpeed(0f);

        EnemyShip enemyShip = new EnemyShip(
                ShipFactory.createShipFromJson("enemy2.json"),
                "enemy",
                "an enemy ship",
                1,
                2,
                5,
                -90f);
        enemyShip.setBrain(new LerpBrain(enemyShip, player));

        EnemyShip enemyShip2 = new EnemyShip(
                ShipFactory.createShipFromJson("enemy1.json"), "enemy", "an enemy ship", 7, -3, 3, 0f);
        enemyShip2.setBrain(new LerpBrain(enemyShip2, player));

        this.spaceShips = new LinkedList<>(
                Arrays.asList(this.player, enemyShip, enemyShip2));
    }

    private void createAsteroids() {
        randomAsteroidFactory.setSpawnPerimeter(this.screenBoundsProvider.getBounds());

        for (Asteroid asteroid : randomAsteroidFactory.getAsteroidShower()) {
            asteroids.add(asteroid);
            hitDetection.addCollider(asteroid);
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
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
        }

        Iterator<Bullet> laserIterator = lasers.iterator();
        while (laserIterator.hasNext()) {
            Bullet laser = laserIterator.next();
            laser.update(delta);
            if (cullSpaceBody(laser)) {// Remove if too distant to player
                hitDetection.removeCollider(laser);
                laserPool.free(laser);
                laserIterator.remove();
            }
        }

        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid iter = asteroidIterator.next();
            iter.update(delta);
            if (cullSpaceBody(iter, 3f)) {// Remove if too distant to player
                hitDetection.removeCollider(iter);
                randomAsteroidFactory.free(iter);
                asteroidIterator.remove();
            }
        }

        this.asteroidTimer += delta;
        if (asteroidTimer > 5) { // 5 for testing
            createAsteroids();
            asteroidTimer = 0;
        }

        for (SpaceShip spaceShip : spaceShips) {
            spaceShip.update(delta);
            if (spaceShip.isShooting()) {
                shoot(spaceShip);
            }
        }

        hitDetection.checkCollisions();
    }

    /**
     * Remove an object if it moves out of range.
     * 
     * @param body the <code>SpaceBody</code> object to potentially remove.
     */
    private boolean cullSpaceBody(SpaceBody body) {
        return cullSpaceBody(body, 0f);
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

    void handleCollision(Collidable A, Collidable B) {
        if (HitDetection.isFriendlyFire(A, B)) {
            return;
        }
        boolean destroyA = false;
        boolean destroyB = false;
        SpaceBody.crash(A, B);

        if (B instanceof Damageable b) {
            destroyB = b.isDestroyed();
        }

        if (A instanceof Damageable a) {
            destroyA = a.isDestroyed();
        }

        if (destroyA) {
            if (B instanceof Bullet bullet && bullet.isPlayerBullet()) {
                collectResources(A);
            }
            remove(A, true);
        }

        if (destroyB) {
            if (A instanceof Bullet bullet && bullet.isPlayerBullet()) {
                collectResources(B);
            }
            remove(B, true);
        }
    }

    private void remove(Collidable c, boolean drawExplosion) {
        hitDetection.removeCollider(c);
        if (c instanceof SpaceBody) {
            System.out.println(c + " destroyed");
            switch (((SpaceBody) c).getCharacterType()) {
                case ASTEROID:
                    if (drawExplosion) {
                        addAnimationState(c, AnimationType.EXPLOSION);
                    }
                    for (Asteroid asteroid : asteroids) {
                        if (asteroid == c) {
                            asteroids.remove(asteroid);
                            randomAsteroidFactory.free(asteroid);
                            break;
                        }
                    }
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
                    break;

                case ENEMY_SHIP:
                    for (SpaceShip ship : this.spaceShips) {
                        if (ship == c) {
                            if (drawExplosion) {
                                addAnimationState(ship.getAbsoluteCenterOfMass().x(),
                                        ship.getAbsoluteCenterOfMass().y(),
                                        ship.getRadius(), AnimationType.EXPLOSION);
                            }
                            spaceShips.remove(c);
                            break;
                        }
                    }
                    break;

                case PLAYER: // TODO: Implement remove(Player) case (game over)
                    if (drawExplosion) {
                        addAnimationState(getPlayerCenterOfMass().x(), getPlayerCenterOfMass().y(), player.getRadius(),
                                AnimationType.EXPLOSION);
                    }
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

    private void collectResources(Collidable collidable) {
        if (collidable instanceof SpaceBody spaceBody) {
            player.getInventory().addResource(spaceBody.getResourceValue());

            // TODO: Remove when displayed on screen
            System.out.println(player.getInventory().listInventory());
        }
    }

    public void playerShoot() {
        player.setIsShooting(true);
    }

    public void shoot(SpaceShip ship) {
        for (CellPosition cell : ship.getUpgradeTypePositions(UpgradeType.TURRET)) {

            float x = (float) cell.col() + Turret.turretBarrelLocation().x();
            float y = (float) cell.row() + Turret.turretBarrelLocation().y();
            FloatPair point = SpaceCalculator.rotatePoint(x, y, ship.getRelativeCenterOfMass(),
                    ship.getAbsoluteCenterOfMass(), ship.getRotationAngle());

            addLaser(point.x(), point.y(), PhysicsParameters.laserVelocity, ship.getRotationAngle() + 90f,
                    0.125f, ship.isPlayerShip()).setSourceID(ship.getID());
        }
        ship.hasShot();
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
     * Gets the center of mass coordinates for a ship
     *
     * @param ship the ship to get center of mass for
     * @return the x,y coordinates of the center of mass
     */
    public FloatPair getShipCenterOfMass(SpaceShip ship) {
        return ship.getAbsoluteCenterOfMass();
    }

    /**
     * Gets the center of mass coordinates for the player
     *
     * @return the x,y coordinates of the player's center of mass
     */
    public FloatPair getPlayerCenterOfMass() {
        return player.getAbsoluteCenterOfMass();
    }

    @Override
    public void gameStateActive() {
    }

    @Override
    public void gameStatePaused() {
    }

    @Override
    public void stopGame() {
    }

    @Override
    public void startNewGame() {
    }

    @Override
    public void stopMoving() {
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
        return 0;
    }

    @Override
    public int getProgression() {
        return 0;
    }

    @Override
    public List<SpaceShip> getSpaceShips() {
        return this.spaceShips;
    }

    public SpaceShip getPlayer() {
        return this.spaceShips.get(0);
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
    public void setAnimationCallback(AnimationCallback animationCallback) {
        this.animationCallback = animationCallback;
    }

    @Override
    public void setScreenBoundsProvider(ScreenBoundsProvider screenBoundsProvider) {
        this.screenBoundsProvider = screenBoundsProvider;
    }
}
