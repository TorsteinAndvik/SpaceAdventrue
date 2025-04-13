package model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import model.ShipComponents.ShipFactory;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.SpaceShip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceGameModelTest {

    // TODO: Finish testing as we keep implementing methods in spaceGame.
    private SpaceGameModel gameModel;
    float initialPlayerX;
    float initialPlayerY;

    // Borrowing from the Wiki here:
    // https://git.app.uib.no/inf112/25v/inf112-25v/-/wikis/notater/Testing-og-Mocking
    // [accessed 14.03.25]
    @BeforeAll
    static void setup() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);
    }

    @BeforeEach
    public void setUp() {
        setup();
        gameModel = new SpaceGameModel();
        gameModel.setAnimationCallback(state -> { });
        initialPlayerX = gameModel.getPlayer().getX();
        initialPlayerY = gameModel.getPlayer().getY();
    }

    @Test
    public void testInitialization() {
        assertNotNull(gameModel.getSpaceShips());
        assertNotNull(gameModel.getAsteroids());
        assertNotNull(gameModel.getLasers());
    }

    /*
     * // TODO: Not compatible with new movement physics
     *
     * @Test
     * public void testPlayerMovement() {
     * gameModel.accelerateForward();
     * assertEquals(initialPlayerY + 1, gameModel.getPlayerSpaceShip().getY());
     *
     * gameModel.accelerateBackward();
     * assertEquals(initialPlayerY, gameModel.getPlayerSpaceShip().getY());
     *
     * gameModel.accelerateCounterClockwise();
     * assertEquals(initialPlayerX - 1, gameModel.getPlayerSpaceShip().getX());
     *
     * gameModel.accelerateClockwise();
     * assertEquals(initialPlayerX, gameModel.getPlayerSpaceShip().getX());
     * }
     */

    /*
     * //TODO: Rewrite test to be compatible with refactored lasers in model
     *
     * @Test
     * public void testShoot() {
     * gameModel.shoot();
     * Bullet laser = gameModel.getLaser();
     * assertNotNull(laser);
     * assertTrue(gameModel.laserExists);
     *
     * assertEquals(2, laser.getY()); // TODO: Fix. This test depends on ship grid
     * size.
     * assertEquals(gameModel.getPlayerSpaceShip().getCenter().x(), laser.getX());
     * }
     */

    @Test
    void friendlyFireBulletBulletTest() {
        gameModel.shoot(gameModel.getPlayer());
        gameModel.shoot(gameModel.getPlayer());

        assertEquals(2, gameModel.getLasers().size());

        Bullet firstLaser = gameModel.getLasers().get(0);
        Bullet secondLaser = gameModel.getLasers().get(1);
        assertTrue(HitDetection.isFriendlyFire(firstLaser, secondLaser));

        SpaceShip enemy_1 = new EnemyShip(ShipFactory.simpleShip(), "enemy 1", "small", 0, 0, 1, 0);
        Bullet thirdLaser = new Bullet("Interrupting laser", "blue", 0,
                0, 0, 0, 1, false);
        thirdLaser.setSourceID(enemy_1.getID());
        assertFalse(HitDetection.isFriendlyFire(firstLaser, thirdLaser));

        SpaceShip enemy_2 = new EnemyShip(ShipFactory.simpleShip(), "enemy 2", "small", 0, 0, 1, 0);
        Bullet fourthLaser = new Bullet("Interrupting laser", "red",
                firstLaser.getX() + firstLaser.getRadius(),
                firstLaser.getY() + firstLaser.getRadius(), 0, 0, 1, false);
        fourthLaser.setSourceID(enemy_2.getID());
        assertFalse(HitDetection.isFriendlyFire(thirdLaser, fourthLaser));

    }

    @Test
    void friendlyFireBulletShipTest() {
        gameModel.shoot(gameModel.getPlayer());
        Bullet laser = gameModel.getLasers().get(0);

        SpaceShip enemy_1 = new EnemyShip(ShipFactory.simpleShip(), "enemy 1", "small", 0, 0, 1, 0);

        assertTrue(HitDetection.isFriendlyFire(gameModel.getPlayer(), laser));
        assertFalse(HitDetection.isFriendlyFire(laser, enemy_1));

    }

    /*
     * // TODO: This must be rewritten
     *
     * @Test
     * void collectResourcesTest() {
     * Inventory inventory = ((Player) gameModel.getPlayer()).getInventory();
     * Asteroid a = gameModel.getAsteroids().get(0);
     * assertFalse(inventory.hasResourceAmount(a.getResourceValue()));
     *
     * while (!a.isDestroyed()) {
     * gameModel.shoot();
     * Bullet laser = gameModel.getLasers().get(0);
     * laser.setX(a.getX());
     * laser.setY(a.getY());
     * gameModel.handleCollision(a, laser);
     * }
     * assertTrue(inventory.hasResourceAmount(a.getResourceValue()));
     * }
     */
}