package model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Rectangle;

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
        initialPlayerX = gameModel.getPlayer().getX();
        initialPlayerY = gameModel.getPlayer().getY();
        gameModel.setAnimationCallback(state -> {
        });
        gameModel.setScreenBoundsProvider(() -> new Rectangle(initialPlayerX, initialPlayerY, 10000f, 10000f));
    }

    @Test
    public void testInitialization() {
        assertNotNull(gameModel.getSpaceShips());
        assertNotNull(gameModel.getAsteroids());
        assertNotNull(gameModel.getLasers());
    }

    @Test
    void friendlyFireBulletBulletTest() {
        gameModel.setShipToShoot(gameModel.getPlayer());
        gameModel.handleShootingLogic(gameModel.getPlayer());
        gameModel.update(2f);
        gameModel.setShipToShoot(gameModel.getPlayer());
        gameModel.handleShootingLogic(gameModel.getPlayer());

        assertEquals(2, gameModel.getLasers().size());

        Bullet firstLaser = gameModel.getLasers().get(0);
        Bullet secondLaser = gameModel.getLasers().get(1);
        assertTrue(HitDetection.isFriendlyFire(firstLaser, secondLaser));

        SpaceShip enemy_1 = new EnemyShip(ShipFactory.simpleShip(), "enemy 1", "small", 0f, 0f, 0f);
        Bullet thirdLaser = new Bullet("Interrupting laser", "blue", 0,
                0, 0, 0, 1, false);
        thirdLaser.setSourceID(enemy_1.getID());
        assertFalse(HitDetection.isFriendlyFire(firstLaser, thirdLaser));

        SpaceShip enemy_2 = new EnemyShip(ShipFactory.simpleShip(), "enemy 2", "small", 0f, 0f, 0f);
        Bullet fourthLaser = new Bullet("Interrupting laser", "red",
                firstLaser.getX() + firstLaser.getRadius(),
                firstLaser.getY() + firstLaser.getRadius(), 0, 0, 1, false);
        fourthLaser.setSourceID(enemy_2.getID());
        assertFalse(HitDetection.isFriendlyFire(thirdLaser, fourthLaser));

    }

    @Test
    void friendlyFireBulletShipTest() {
        gameModel.playerShoot();
        gameModel.handleShootingLogic(gameModel.getPlayer());
        Bullet laser = gameModel.getLasers().get(0);

        SpaceShip enemy_1 = new EnemyShip(ShipFactory.simpleShip(), "enemy 1", "small", 0f, 0f, 0f);

        assertTrue(HitDetection.isFriendlyFire(gameModel.getPlayer(), laser));
        assertFalse(HitDetection.isFriendlyFire(laser, enemy_1));
    }
}