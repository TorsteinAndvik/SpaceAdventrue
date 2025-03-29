package model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import controller.ControllableSpaceGameModel;
import grid.CellPosition;
import grid.GridCell;
import grid.IGridDimension;
import model.Animation.AnimationCallback;
import model.Animation.AnimationStateImpl;
import model.Animation.AnimationType;
import model.Globals.Collideable;
import model.Globals.DamageDealer;
import model.Globals.Damageable;
import model.ShipComponents.ShipFactory;
import model.ShipComponents.Components.Turret;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.EnemyShip;
import model.SpaceCharacters.Player;
import model.SpaceCharacters.SpaceBody;
import model.SpaceCharacters.SpaceShip;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
import view.ViewableSpaceGameModel;

public class SpaceGameModel implements ViewableSpaceGameModel, ControllableSpaceGameModel {

    private Player player;
    private final ShipFactory shipFactory;
    private LinkedList<SpaceShip> spaceShips;
    private final HitDetection hitDetection;
    private LinkedList<Asteroid> asteroids;
    private LinkedList<Bullet> lasers;
    private boolean enemyRotationActive;
    private boolean rotateClockwise;

    private final Matrix3 rotationMatrix;
    private final Matrix4 transformMatrix;

    private AnimationCallback animationCallback;
    private ScreenBoundsProvider screenBoundsProvider;

    public SpaceGameModel() {
        this.shipFactory = new ShipFactory();

        createSpaceShips();

        createAsteroids();

        this.lasers = new LinkedList<Bullet>();

        this.hitDetection = new HitDetection(this);

        registerColliders();

        this.rotationMatrix = new Matrix3();
        this.transformMatrix = new Matrix4();
    }

    private void createSpaceShips() {
        this.player = new Player(
                shipFactory.createShipFromJson("enemy2.json"), "player", "the player's spaceship", 1, 8, 1);
        this.player.setRotationSpeed(0f);

        EnemyShip enemyShip = new EnemyShip(
                shipFactory.createShipFromJson("enemy2.json"),
                "enemy",
                "an enemy ship",
                1,
                1,
                5,
                0f);

        EnemyShip enemyShip2 = new EnemyShip(
                shipFactory.createShipFromJson("enemy1.json"), "enemy", "an enemy ship", 7, -3, 3, 0f);

        this.spaceShips = new LinkedList<SpaceShip>(Arrays.asList(this.player, enemyShip, enemyShip2));
    }

    private void createAsteroids() {
        float radiusLarge = 1f;
        float radiusSmall = 0.5f;

        Asteroid asteroidLarge = new Asteroid("large asteroid", "a large asteroid", 1f, 6f,
                0.3f,
                -0.10f, 10, 4f, 30f,
                radiusLarge, true);
        asteroidLarge.setRotationSpeed(60f);

        Asteroid asteroidSmall = new Asteroid("small asteroid", "a small asteroid", 5f, 4f,
                -0.1f, 0.15f, 1, 1f, 0f, radiusSmall,
                false);
        asteroidSmall.setRotationSpeed(-30f);

        Asteroid asteroidSmall2 = new Asteroid("small asteroid", "a small asteroid", 6f,
                4.5f, -0.1f, 0.15f, 1, 1f, 0f, radiusSmall, false);
        asteroidSmall2.setRotationSpeed(40f);

        this.asteroids = new LinkedList<>();
        asteroids.add(asteroidLarge);
        asteroids.add(asteroidSmall);
        asteroids.add(asteroidSmall2);
    }

    private void registerColliders() {
        hitDetection.addColliders(spaceShips);
        hitDetection.addColliders(asteroids);
    }

    private void addLaser(float x, float y, int hitPoints, float angle, float speed, float radius,
            boolean isPlayerLaser) {
        Bullet laser = new Bullet("laser", "a laser shot", x, y, hitPoints, angle, speed, radius, isPlayerLaser);
        lasers.addFirst(laser);
        hitDetection.addCollider(laser);
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

    void handleCollision(Collideable A, Collideable B) {
        if (isFriendlyFire(A, B)) {
            return;
        }

        if (A instanceof DamageDealer && B instanceof Damageable) {
            ((DamageDealer) A).dealDamage((Damageable) B);
            if (((Damageable) B).isDestroyed()) {
                remove(B, true);
            }
        }

        if (B instanceof DamageDealer && A instanceof Damageable) {
            ((DamageDealer) B).dealDamage((Damageable) A);
            if (((Damageable) A).isDestroyed()) {
                remove(A, true);
            }
        }
    }

    // TODO: Refactor relevant code with a source SpaceShip, such that enemy ships
    // can fire at each other without damaging themselves
    private boolean isFriendlyFire(Collideable A, Collideable B) {
        if (A instanceof Player && B instanceof Bullet) {
            if (((Bullet) B).isPlayerBullet) {
                return true;
            }
        } else if (B instanceof Player && A instanceof Bullet) {
            if (((Bullet) A).isPlayerBullet) {
                return true;
            }
        }

        return false;
    }

    private void remove(Collideable c, boolean drawExplosion) {
        hitDetection.removeCollider(c);
        if (c instanceof SpaceBody) {
            System.out.println(c + " destroyed");
            switch (((SpaceBody) c).getCharacterType()) {
                case ASTEROID:
                    for (Asteroid asteroid : asteroids) {
                        if (asteroid == c) {
                            asteroids.remove(c);
                            break;
                        }
                    }
                    if (drawExplosion) {
                        addAnimationState(c, AnimationType.EXPLOSION);
                    }
                    break;

                case BULLET:
                    for (Bullet laser : lasers) {
                        if (laser == c) {
                            lasers.remove(c);
                            break;
                        }
                    }
                    if (drawExplosion) {
                        addAnimationState(c, AnimationType.EXPLOSION);
                    }
                    break;

                case ENEMY_SHIP:
                    for (SpaceShip ship : this.spaceShips) {
                        if (ship == c) {
                            spaceShips.remove(c);
                            break;
                        }
                    }
                    if (drawExplosion) {
                        addAnimationState(c, AnimationType.EXPLOSION);
                    }
                    break;

                case PLAYER: // TODO: Implement remove(Player) case (game over)
                    break;

                default:
                    break;
            }
        }
    }

    private void addAnimationState(Collideable c, AnimationType type) {
        animationCallback.addAnimationState(new AnimationStateImpl(c, type));
    }

    public void shoot() {
        for (CellPosition cell : player.getTurretPositions()) {
            float x0 = (float) cell.col() + Turret.turretBarrelLocation().x() - player.getRelativeCenterOfMass().x();
            float y0 = (float) cell.row() + Turret.turretBarrelLocation().y() - player.getRelativeCenterOfMass().y();
            float r = (float) Math.sqrt(Math.pow(x0, 2) + Math.pow(y0, 2));

            float offsetAngle = (float) Math.toDegrees(Math.atan2(y0, x0));

            float x1 = r * (float) Math.cos(Math.toRadians(player.getRotationAngle() + offsetAngle));
            float y1 = r * (float) Math.sin(Math.toRadians(player.getRotationAngle() + offsetAngle));

            float x2 = getPlayerCenterOfMass().x() + x1;
            float y2 = getPlayerCenterOfMass().y() + y1;

            addLaser(x2, y2, 1, player.getRotationAngle() + 90f, PhysicsParameters.laserVelocity, 0.125f,
                    true);
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
