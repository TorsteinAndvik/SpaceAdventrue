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
    private Asteroid asteroid;
    private EnemyShip enemyShip;
    private Bullet laser;
    private ShipFactory shipFactory;
    private SpaceShip[] spaceShips;
    public boolean laserExists;
    private boolean enemyRotationActive;
    private boolean rotateClockwise;

    public SpaceGameModel() {
        this.shipFactory = new ShipFactory();
        this.player = new Player(
                shipFactory.simpleShip(), "player", "the player's spaceship", 1, 3, 1, 1);
        this.enemyShip = new EnemyShip(
                shipFactory.createShipFromJson("enemy1.json"),
                "enemy",
                "an enemy ship",
                1,
                1,
                5,
                0,
                1);
        this.asteroid = new Asteroid("asteroid", "an asteroid", 1, 6, 6, 1, 1, 0, 2, 4);
        this.laser = new Bullet("laser", "a laser shot", 0, 0, 1, 1, 0, 1);

        spaceShips = new SpaceShip[] { player, enemyShip };
    }

    public void shoot() {
        // TODO: This is awful, never do this.
        this.laser = new Bullet("laser", "a laser shot", player.getX(), player.getY() + 1, 1, 1, 0, 1);
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
    public void rotateEnemy() {
        if (!this.enemyRotationActive) {
            return;
        }

        float rotationalVelocity = 0.5f;
        if (rotateClockwise) {
            this.enemyShip.rotate(-rotationalVelocity);
        } else {
            this.enemyShip.rotate(rotationalVelocity);
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
    public Asteroid getAsteroid() {
        return this.asteroid;
    }

    @Override
    public Bullet getLaser() {
        return this.laser;
    }
}
