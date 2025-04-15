package model.ShipComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grid.CellPosition;
import model.SpaceCharacters.Ships.Player;
import model.SpaceCharacters.Ships.SpaceShip;
import model.utils.FloatPair;
import model.utils.MassProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpgradeHandlerTest {

    private UpgradeHandler upgradeHandler;
    private ShipStructure structure;
    SpaceShip player;

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
    void getGridTest() {
        assertNotNull(upgradeHandler.getGrid());
        for (int i = 0; i < upgradeHandler.getGrid().rows(); i++) {
            for (int j = 0; j < upgradeHandler.getGrid().cols(); j++) {
                assertEquals(upgradeHandler.getGrid().get(new CellPosition(i, j)).getUpgrade().getType(),
                        structure.getGridCopy().get(new CellPosition(i, j)).getUpgrade().getType());
            }
        }
    }
}
