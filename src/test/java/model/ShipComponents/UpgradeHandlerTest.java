package model.ShipComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grid.CellPosition;
import grid.GridCell;
import grid.IGrid;
import model.ShipComponents.Components.Fuselage;
import model.SpaceCharacters.Ships.Player;
import model.SpaceCharacters.Ships.SpaceShip;
import model.utils.FloatPair;
import model.utils.MassProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpgradeHandlerTest {

    private UpgradeHandler upgradeHandler;
    private ShipStructure structure;
    private SpaceShip player;

    @BeforeEach
    void setup() {
        player = new Player(ShipFactory.simpleShip(), "name", "description", 0f, 0f);
        structure = player.getShipStructure();
        upgradeHandler = new UpgradeHandler(structure);
    }

    @Test
    void setupTest() {
        assertTrue(structure.hasFuselage(new CellPosition(0, 0)));
        assertTrue(structure.hasFuselage(new CellPosition(1, 0)));
    }

    @Test
    void fuselageTest() {
        assertTrue(upgradeHandler.hasFuselage(new CellPosition(0, 0)));
        assertNotNull(upgradeHandler.getFuselage(new CellPosition(0, 0)));
        upgradeHandler.expand();
        assertFalse(upgradeHandler.hasFuselage(new CellPosition(0, 0)));
        assertNull(upgradeHandler.getFuselage(new CellPosition(0, 0)));
    }

    @Test
    void canPlaceItemTest() {

        CellPosition cp = new CellPosition(2, 0).offset(1, 1);
        assertFalse(upgradeHandler.canPlaceItem(cp, UpgradeType.FUSELAGE));
        assertFalse(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));

        upgradeHandler.expand();

        assertTrue(upgradeHandler.canPlaceItem(cp, UpgradeType.FUSELAGE));
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));

        assertTrue(upgradeHandler.canPlaceItem(cp, UpgradeType.SHIELD));
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.SHIELD));
        assertFalse(upgradeHandler.canPlaceItem(cp, UpgradeType.SHIELD)); // cp taken
        assertFalse(upgradeHandler.placeItem(cp, UpgradeType.SHIELD)); // cp taken
    }

    @Test
    void placeItemUpdatesMassTest() {

        MassProperties mp = structure.getMassProperties();
        upgradeHandler.expand();
        CellPosition cp = new CellPosition(2, 0).offset(1, 1);
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));

        assertNotEquals(mp, structure.getMassProperties());
    }

    @Test
    void placeUpgradeTest() {
        upgradeHandler.expand();
        CellPosition cp = new CellPosition(2, 0).offset(1, 1);
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.THRUSTER));
    }

    @Test
    void absoluteCenterOfMassTest() {
        SpaceShip player = new Player(new ShipStructure(3, 3), "name", "desc", 0f, 0f);
        UpgradeHandler upgradeHandler = new UpgradeHandler(player.getShipStructure());

        CellPosition cp = new CellPosition(1, 1);
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));
        upgradeHandler.exit();
        assertEquals(new FloatPair(0, 0), player.getAbsoluteCenterOfMass());
    }

    @Test
    void testCantPlaceItem() {
        SpaceShip player = new Player(new ShipStructure(3, 3), "name", "desc", 0f, 0f);
        UpgradeHandler upgradeHandler = new UpgradeHandler(player.getShipStructure());

        CellPosition cp = new CellPosition(1, 1);
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));
        assertFalse(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));
    }

    @Test
    void testGetGridCopy() {

        SpaceShip player = new Player(ShipFactory.playerShip(), "name", "desc", 0f, 0f);
        IGrid<Fuselage> originalGrid = player.getShipStructure().getGridCopy();
        UpgradeHandler upgradeHandler = new UpgradeHandler(player.getShipStructure());
        IGrid<Fuselage> gridCopy = upgradeHandler.getGrid();

        for (GridCell<Fuselage> cp : originalGrid) {
            assertEquals(gridCopy.get(cp.pos()), cp.value());
        }
    }

    @Test
    void upgradeStageTest() {
        assertFalse(upgradeHandler.upgradeStageIncreaseIsAllowed(new Fuselage()));

        CellPosition cp = new CellPosition(0, 0);

        Fuselage fuselage = upgradeHandler.getFuselage(cp);
        assertEquals(fuselage.getStage(), fuselage.getUpgrade().getStage());
        assertFalse(upgradeHandler.upgradeStageIncreaseIsAllowed(fuselage));

        assertTrue(fuselage.upgrade());

        assertTrue(upgradeHandler.upgradeStageIncreaseIsAllowed(fuselage));
        assertTrue(upgradeHandler.upgradeStage(cp, false));

        assertFalse(upgradeHandler.upgradeStage(cp, false));
        assertTrue(upgradeHandler.upgradeStage(cp, true));
    }
}
