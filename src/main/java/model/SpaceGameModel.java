package model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import controller.ControllableSpaceGameModel;
import grid.GridCell;
import grid.IGridDimension;
import model.Animation.AnimationCallback;
import model.Animation.AnimationState;
import model.Animation.AnimationStateImpl;
import model.Animation.AnimationType;
import model.Globals.Collideable;
import model.Globals.DamageDealer;
import model.Globals.Damageable;
import model.ShipComponents.ShipFactory;
import model.SpaceCharacters.Asteroid;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.EnemyShip;
import model.SpaceCharacters.Player;
import model.SpaceCharacters.SpaceBody;
import model.SpaceCharacters.SpaceShip;
import model.utils.FloatPair;
import view.ViewableSpaceGameModel;

public class SpaceGameModel implements ViewableSpaceGameModel, ControllableSpaceGameModel {

    private Player player;
    private SpaceShip enemyShip; // TODO: Remove
    private Bullet laser;
    private final ShipFactory shipFactory;
    private LinkedList<SpaceShip> spaceShips;
    private final HitDetection hitDetection;
    private LinkedList<Asteroid> asteroids;
    public boolean laserExists;
    private boolean enemyRotationActive;
    private boolean rotateClockwise;

    private final Matrix3 rotationMatrix;
    private final Matrix4 transformMatrix;

    private AnimationCallback view;

    public SpaceGameModel() {
        this.shipFactory = new ShipFactory();

        createSpaceShips();

        createAsteroids();

        this.laser = new Bullet("laser", "a laser shot", 0f, 0f, 1, 1f, 0f, 1f);

        this.hitDetection = new HitDetection(this);
        hitDetection.addColliders(Arrays.asList(player, enemyShip, asteroids.get(0), asteroids.get(1), this.laser)); // TODO:
                                                                                                                     // Refactor

        this.rotationMatrix = new Matrix3();
        this.transformMatrix = new Matrix4();
    }

    private void createSpaceShips() {
        this.player = new Player(
                shipFactory.playerShip(), "player", "the player's spaceship", 1, 5, 1);
        this.enemyShip = new EnemyShip(
                shipFactory.createShipFromJson("enemy2.json"),
                "enemy",
                "an enemy ship",
                1,
                1,
                5,
                0);

        this.spaceShips = new LinkedList<SpaceShip>(Arrays.asList(this.player, this.enemyShip));
    }

    private void createAsteroids() {
        float radiusLarge = 1f;
        float radiusSmall = 0.5f;

        Asteroid asteroidLarge = new Asteroid("large asteroid", "a large asteroid", 1f + radiusLarge, 6f + radiusLarge,
                0.3f,
                -0.10f, 4, 4f, 30f,
                1f, true);
        asteroidLarge.setRotationSpeed(60f);

        Asteroid asteroidSmall = new Asteroid("small asteroid", "a small asteroid", 5f + radiusSmall, 4f + radiusSmall,
                -0.1f,
                0.15f, 1, 1f, 0f, 0.5f,
                false);
        asteroidSmall.setRotationSpeed(-30f);

        this.asteroids = new LinkedList<>();
        asteroids.add(asteroidLarge);
        asteroids.add(asteroidSmall);
    }

    @Override
    public void update(float delta) {
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
        }
        laser.update(delta);
        for (SpaceShip spaceShip : spaceShips) {
            spaceShip.update(delta);
        }
        // TODO: remove this call once model is finished such
        // that it receives model.update(delta) in the future.
        rotateEnemy(delta);
        hitDetection.checkCollisions();
    }

    void handleCollision(Collideable A, Collideable B) {
        if (A instanceof DamageDealer && B instanceof Damageable) {
            ((DamageDealer) A).dealDamage((Damageable) B);
            if (((Damageable) B).isDestroyed()) {
                remove(B);
            }
        }

        if (B instanceof DamageDealer && A instanceof Damageable) {
            ((DamageDealer) B).dealDamage((Damageable) A);
            if (((Damageable) A).isDestroyed()) {
                remove(A);
            }
        }
    }

    private void remove(Collideable c) {
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
                    addAnimationState(c, AnimationType.EXPLOSION);
                    break;

                case BULLET: // TODO: Implement remove(Bullet) case
                    break;

                case ENEMY_SHIP:// TODO: Implement remove(Enemy) case
                    for (SpaceShip ship : this.spaceShips) {
                        if (ship == c) {
                            spaceShips.remove(c);
                            break;
                        }
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
        view.addAnimationState(new AnimationStateImpl(c, type));
    }

    public void shoot() {
        // TODO: This is awful, never do this.
        this.laser = new Bullet("laser", "a laser shot", player.getCenter().x(),
                player.getCenter().y(), 1, 1, 0,
                1);
        laserExists = true;
    }

    public void moveLaser() {
        // TODO: This is also awful.
        laser.setY(laser.getY() + 1);
        if (laser.getY() >= 9) {
            System.out.println("laser deleted offscreen");
            laserExists = false;
            this.laser = null;
        }
    }

    // TODO: Remove this once proper model is in place - currently used for testing
    // rendering of rotated ships in SpaceScreen
    public void rotateEnemy(float deltaTime) {
        if (!this.enemyRotationActive) {
            return;
        }

        float rotationalVelocity = 45f; // degrees per second
        if (rotateClockwise) {
            this.enemyShip.rotate(-rotationalVelocity * deltaTime);
        } else {
            this.enemyShip.rotate(rotationalVelocity * deltaTime);
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
        float x = ship.getX() + cm.x() + 0.5f;
        float y = ship.getY() + cm.y() + 0.5f;

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
        FloatPair cm = ship.getShipStructure().getCenterOfMass();
        float x = ship.getX() + cm.x() + 0.5f;
        float y = ship.getY() + cm.y() + 0.5f;
        return new FloatPair(x, y);
    }

    public void toggleEnemyRotationActive() {
        this.enemyRotationActive = !this.enemyRotationActive;
    }

    public void setEnemyRotationClockwise(boolean clockwise) {
        this.rotateClockwise = clockwise;
    }

    @Override
    public boolean moveSpaceShip(int deltaRow, int deltaCol) {
        return false;
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
    public void moveUp() {
        player.setY(player.getY() + 1);
    }

    @Override
    public void moveDown() {
        player.setY(player.getY() - 1);
    }

    @Override
    public void moveLeft() {
        player.setX(player.getX() - 1);
    }

    @Override
    public void moveRight() {
        player.setX(player.getX() + 1);
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

    public SpaceShip getPlayerSpaceShip() {
        return this.spaceShips.get(0);
    }

    @Override
    public List<Asteroid> getAsteroids() {
        return this.asteroids;
    }

    @Override
    public Bullet getLaser() {
        return this.laser;
    }

    @Override
    public void setAnimationCallback(AnimationCallback view) {
        this.view = view;
    }
}
