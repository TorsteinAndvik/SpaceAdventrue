package model.ShipComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        assertTrue(structure.hasFuselage(new CellPosition(0, 0)));
        assertTrue(structure.hasFuselage(new CellPosition(1, 0)));
        upgradeHandler = new UpgradeHandler(structure);
    }

    @Test
    void testCanPlaceItem() {

        CellPosition cp = new CellPosition(2, 0).offset(1, 1);
        assertFalse(upgradeHandler.canPlaceItem(cp, UpgradeType.FUSELAGE));
        upgradeHandler.expand();
        assertTrue(upgradeHandler.canPlaceItem(cp, UpgradeType.FUSELAGE));
    }

    @Test
    void testPlaceItem() {

        MassProperties mp = structure.getMassProperties();
        upgradeHandler.expand();
        CellPosition cp = new CellPosition(2, 0).offset(1, 1);
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));

        assertNotEquals(mp, structure.getMassProperties());
    }

    @Test
    void testPlaceUpgrade() {
        upgradeHandler.expand();
        CellPosition cp = new CellPosition(2, 0).offset(1, 1);
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.THRUSTER));
    }

    @Test
    void testAbsoluteCenterOfMass() {
        SpaceShip player = new Player(new ShipStructure(3, 3), "name", "desc", 0f, 0f);
        UpgradeHandler upgradeHandler = new UpgradeHandler(player.getShipStructure());

        CellPosition cp = new CellPosition(1, 1);
        assertTrue(upgradeHandler.placeItem(cp, UpgradeType.FUSELAGE));
        upgradeHandler.exit();
        assertEquals(new FloatPair(0, 0), player.getAbsoluteCenterOfMass());
    }
}
