package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.TestSpaceGame;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import controller.UpgradeScreenController;
import grid.CellPosition;
import java.util.HashSet;
import java.util.Set;
import model.ShipComponents.ShipFactory;
import model.ShipComponents.ShipStructure;
import model.ShipComponents.UpgradeHandler;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.Ships.Player;
import model.World.StoreItem;
import model.utils.SpaceCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpgradeScreenModelTest {

    // TODO: Finish testing as we keep implementing methods in spaceGame.
    private UpgradeScreenModel model;
    private UpgradeScreenController controller;

    private Player player;

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
    void setUp() {
        setup();
        player = new Player(ShipFactory.playerShip(), "Player", "Player", 100, 0, 0);
        model = new UpgradeScreenModel(player);
    }

    @Test
    void testInitialization() {
        assertNotNull(model.getExpandedGrid());
    }

    @Test
    void testStoreShelf() {
        Set<StoreItem> storeItemSet = new HashSet<>();
        StoreItem fuselage = new StoreItem(UpgradeType.FUSELAGE, 100, "A fuselage");
        StoreItem turret = new StoreItem(UpgradeType.TURRET, 130, "A turret");
        storeItemSet.add(fuselage);
        storeItemSet.add(turret);

        model.addNewStoreStock(storeItemSet);
        assertEquals(2, model.getStoreShelf().size());
        assertTrue(model.getStoreShelf().contains(fuselage));
        assertTrue(model.getStoreShelf().contains(turret));

    }

    @Test
    void testInvalidFuselagePlacement() {
        ShipStructure structure = player.getShipStructure();
        UpgradeHandler uh = model.getUpgradeHandler();
        CellPosition cp = new CellPosition(0, 0);

        // TODO: this part seems broken, cp should have a neighbouring fuselage, so we
        // shouldn't assert false for all neighbouring cells

        // for (CellPosition pos : SpaceCalculator.getOrthogonalNeighbours(cp)) {
        // assertFalse(structure.hasFuselage(pos));
        // }
        assertFalse(uh.canPlaceItem(new CellPosition(0, 0), UpgradeType.FUSELAGE));
    }
}