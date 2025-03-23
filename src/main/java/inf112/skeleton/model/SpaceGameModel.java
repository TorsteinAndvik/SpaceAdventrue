package inf112.skeleton.model;

import inf112.skeleton.controller.ControllableSpaceGameModel;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.grid.IGridDimension;
import inf112.skeleton.model.ShipComponents.ShipFactory;
import inf112.skeleton.model.SpaceCharacters.Asteroid;
import inf112.skeleton.model.SpaceCharacters.Bullet;
import inf112.skeleton.model.SpaceCharacters.EnemyShip;
import inf112.skeleton.model.SpaceCharacters.Player;
import inf112.skeleton.model.SpaceCharacters.SpaceShip;
import inf112.skeleton.view.ViewableSpaceGameModel;

public class SpaceGameModel implements ViewableSpaceGameModel, ControllableSpaceGameModel {

    private Player player;
    private EnemyShip enemyShip;
    private Bullet laser;
    private ShipFactory shipFactory;
    private SpaceShip[] spaceShips;
    private Asteroid[] asteroids;
    public boolean laserExists;
    private boolean enemyRotationActive;
    private boolean rotateClockwise;

    public SpaceGameModel() {
        this.shipFactory = new ShipFactory();
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

        createAsteroids();

        this.laser = new Bullet("laser", "a laser shot", 0f, 0f, 1, 1f, 0f, 1f);

        spaceShips = new SpaceShip[] { player, enemyShip };
    }

    private void createAsteroids() {
        Asteroid asteroidLarge = new Asteroid("large asteroid", "a large asteroid", 1f, 6f, 0.6f, -0.20f, 4, 4f, 30f,
                2f, true);
        asteroidLarge.setRotationSpeed(60f);

        Asteroid asteroidSmall = new Asteroid("small asteroid", "a small asteroid", 5f, 4f, -0.2f, 0.3f, 1, 1f, 0f, 1f,
                false);
        asteroidSmall.setRotationSpeed(-30f);

        this.asteroids = new Asteroid[] { asteroidLarge, asteroidSmall };
    }

    @Override
    public void update(float delta) {
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
        }
        laser.update(delta);
        for (int i = 0; i < spaceShips.length; i++) {
            spaceShips[i].update(delta);
        }
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
    public GameState getGameState() {
        return null;
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
    public SpaceShip[] getSpaceShips() {
        return this.spaceShips;
    }

    @Override
    public Asteroid[] getAsteroids() {
        return this.asteroids;
    }

    @Override
    public Bullet getLaser() {
        return this.laser;
    }
}
