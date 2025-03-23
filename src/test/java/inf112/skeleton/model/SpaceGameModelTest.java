package inf112.skeleton.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import inf112.skeleton.model.SpaceCharacters.Bullet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        initialPlayerX = gameModel.getPlayerSpaceShip().getX();
        initialPlayerY = gameModel.getPlayerSpaceShip().getY();
    }

    @Test
    public void testInitialization() {
        assertNotNull(gameModel.getSpaceShips());
        assertNotNull(gameModel.getAsteroids());
        assertNotNull(gameModel.getLaser());

    }

    @Test
    public void testPlayerMovement() {
        gameModel.moveUp();
        assertEquals(initialPlayerY + 1, gameModel.getPlayerSpaceShip().getY());

        gameModel.moveDown();
        assertEquals(initialPlayerY, gameModel.getPlayerSpaceShip().getY());

        gameModel.moveLeft();
        assertEquals(initialPlayerX - 1, gameModel.getPlayerSpaceShip().getX());

        gameModel.moveRight();
        assertEquals(initialPlayerX, gameModel.getPlayerSpaceShip().getX());
    }

    @Test
    public void testMoveLaser() {
        gameModel.shoot();
        Bullet laser = gameModel.getLaser();
        assertNotNull(laser);

        gameModel.moveLaser();
        assertEquals(
                initialPlayerY + gameModel.getPlayerSpaceShip().getShipStructure()
                        .getHeight() / 2f
                        + 1,
                laser.getY());

        // A laser shoots from a ship, and is deleted when offscreen, so
        // get the x coordinate of the laser for this loop.
        for (int i = (int) laser.getY(); i < 9; i++) {
            gameModel.moveLaser();
        }
        assertFalse(gameModel.laserExists);
        assertNull(gameModel.getLaser());
    }

    @Test
    public void testShoot() {
        gameModel.shoot();
        Bullet laser = gameModel.getLaser();
        assertNotNull(laser);
        assertTrue(gameModel.laserExists);
        assertEquals(2.5, laser.getY()); // TODO: Fix. This test depends on ship grid size.
        assertEquals(gameModel.getPlayerSpaceShip().getCenter().x(), laser.getX());

    }

    @Test
    public void testMoveSpaceShip() {
        boolean result = gameModel.moveSpaceShip(1, 1);
        assertFalse(result);
    }
}