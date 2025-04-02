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
import grid.GridCell;
import grid.IGridDimension;
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
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;
import view.ViewableSpaceGameModel;

public class SpaceGameModel implements ViewableSpaceGameModel, ControllableSpaceGameModel {

    private Player player;
    private LinkedList<SpaceShip> spaceShips;
    private final HitDetection hitDetection;
    private LinkedList<Asteroid> asteroids;
    private Pool<Asteroid> asteroidPool;
    private LinkedList<Bullet> lasers;
    private Pool<Bullet> laserPool;
    private boolean enemyRotationActive;
    private boolean rotateClockwise;

    private final Matrix3 rotationMatrix;
    private final Matrix4 transformMatrix;

    private AnimationCallback animationCallback;
    private ScreenBoundsProvider screenBoundsProvider;

    public SpaceGameModel() {

        createAsteroidPool(100);
        createLaserPool(300);

        createSpaceShips();

        createAsteroids();

        this.lasers = new LinkedList<>();

        this.hitDetection = new HitDetection(this);

        registerColliders();

        this.rotationMatrix = new Matrix3();
        this.transformMatrix = new Matrix4();
    }

    private void createAsteroidPool(int asteroidPreFill) {
        this.asteroidPool = new Pool<>() {
            @Override
            protected Asteroid newObject() {
                return new Asteroid();
            }
        };

        asteroidPool.fill(asteroidPreFill);
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
                ShipFactory.playerShip(), "player", "the player's spaceship", 1, 8, 1);
        this.player.setRotationSpeed(0f);

        EnemyShip enemyShip = new EnemyShip(
                ShipFactory.createShipFromJson("enemy2.json"),
                "enemy",
                "an enemy ship",
                1,
                1,
                5,
                0f);

        EnemyShip enemyShip2 = new EnemyShip(
                ShipFactory.createShipFromJson("enemy1.json"), "enemy", "an enemy ship", 7, -3, 3, 0f);

        this.spaceShips = new LinkedList<>(
                Arrays.asList(this.player, enemyShip, enemyShip2));
    }

    private void createAsteroids() {
        float radiusLarge = 1f;
        float radiusSmall = 0.5f;

        Asteroid asteroidLarge = asteroidPool.obtain();
        asteroidLarge.init(1f, 6f, 0.35f, 4, 4f, 10f, radiusLarge, 60f, true);

        Asteroid asteroidSmall = asteroidPool.obtain();
        asteroidSmall.init(5f, 5f, 0.25f, 1, 1f, 170f, radiusSmall, -30f, false);

        Asteroid asteroidSmall2 = asteroidPool.obtain();
        asteroidSmall2.init(6f, 6f, 0.3f, 1, 1f, 175f, radiusSmall, 40f, false);

        this.asteroids = new LinkedList<>();
        asteroids.add(asteroidLarge);
        asteroids.add(asteroidSmall);
        asteroids.add(asteroidSmall2);
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
            if (cullLaser(laser)) {// Remove if too distant to player
                hitDetection.removeCollider(laser);
                laserPool.free(laser);
                laserIterator.remove();
            }
        }

        for (SpaceShip spaceShip : spaceShips) {
            spaceShip.update(delta);
        }

        // TODO: remove this call once model is finished such
        // that it receives model.update(delta) in the future.
        rotateEnemy(delta);
        hitDetection.checkCollisions();
    }

    /**
     * Delete a laser if it moves out of range.
     */
    private boolean cullLaser(Bullet laser) {
        Rectangle bounds = this.screenBoundsProvider.getBounds();

        return (laser.getX() + laser.getRadius() < bounds.x
                || laser.getY() + laser.getRadius() < bounds.y
                || laser.getX() - laser.getRadius() > bounds.x + bounds.width
                || laser.getY() - laser.getRadius() > bounds.y + bounds.height);
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
                            asteroidPool.free(asteroid);
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

            //TODO: Remove when displayed on screen
            System.out.println(player.getInventory().listInventory());
        }
    }

    public void shoot() {
        for (CellPosition cell : player.getUpgradeTypePositions(UpgradeType.TURRET)) {

            float x = (float) cell.col() + Turret.turretBarrelLocation().x();
            float y = (float) cell.row() + Turret.turretBarrelLocation().y();
            FloatPair point = SpaceCalculator.rotatePoint(x, y, player.getRelativeCenterOfMass(),
                    getPlayerCenterOfMass(), player.getRotationAngle());

            addLaser(point.x(), point.y(), PhysicsParameters.laserVelocity, player.getRotationAngle() + 90f,
                    0.125f, true);
        }
    }

    // TODO: Remove this once proper model is in place - currently used for testing
    // rendering of rotated ships in SpaceScreen
    public void rotateEnemy(float deltaTime) {
        if (spaceShips.size() <= 1) {
            return;
        }
        if (!this.enemyRotationActive) {
            spaceShips.getLast().setRotationSpeed(0f);
            return;
        }

        float rotationalVelocity = 45f; // degrees per second
        if (rotateClockwise) {
            spaceShips.getLast().setRotationSpeed(-rotationalVelocity);
        } else {
            spaceShips.getLast().setRotationSpeed(rotationalVelocity);
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

    public void toggleEnemyRotationActive() {
        this.enemyRotationActive = !this.enemyRotationActive;
    }

    public void setEnemyRotationClockwise(boolean clockwise) {
        this.rotateClockwise = clockwise;
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
    public IGridDimension getDimension() {
        return null;
    }

    @Override
    public Iterable<GridCell<Character>> getPixels() {
        return null;
    }

    @Override
    public Iterable<GridCell<Character>> getPixelsInSpaceBody() {
        return null;
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
