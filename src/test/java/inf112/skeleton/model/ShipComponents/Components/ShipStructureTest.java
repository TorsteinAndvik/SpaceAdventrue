package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.model.ShipComponents.ShipConfig;
import inf112.skeleton.model.ShipComponents.UpgradeType;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipStructureTest {

    private ShipStructure shipStructure;
    private ShipConfig shipConfig;

    @BeforeEach
    void setUp() {
        ShipConfig.ShipComponent component1 = new ShipConfig.ShipComponent();
        component1.x = 1;
        component1.y = 1;
        component1.upgrade = null;

        ShipConfig.Upgrade upgrade = new ShipConfig.Upgrade();
        upgrade.type = UpgradeType.SHIELD;
        upgrade.level = 1;

        ShipConfig.ShipComponent component2 = new ShipConfig.ShipComponent();
        component2.x = 2;
        component2.y = 2;
        component2.upgrade = upgrade;

        shipConfig = new ShipConfig();
        shipConfig.width = 10;
        shipConfig.height = 10;
        shipConfig.components = Arrays.asList(component1, component2);

        shipStructure = new ShipStructure(shipConfig);
    }

    @Test
    void testInitializationWithDimensions() {
        ShipStructure structure = new ShipStructure(10, 10);
        CellPosition cellPosition = new CellPosition(0, 0);
        assertTrue(structure.set(cellPosition));
        assertNotNull(structure.iterable());
    }

    @Test
    void testSetFuselage() {
        CellPosition cellPosition = new CellPosition(0, 0);
        assertTrue(shipStructure.set(cellPosition));
        assertFalse(shipStructure.set(cellPosition)); // we shouldn't be allowed to set twice

        int occupiedX = 1;
        int occupiedY = 1;
        CellPosition occupied = new CellPosition(occupiedX, occupiedY);
        assertFalse(shipStructure.set(occupied));
    }

    @Test
    void testSetFuselageWithUpgrade() {
        CellPosition cellPosition = new CellPosition(0, 1);
        assertTrue(shipStructure.set(cellPosition, new Fuselage(new Shield())));
        assertFalse(shipStructure.set(cellPosition, new Fuselage(new Turret())));
    }

    @Test
    void testAddUpgrade() {
        CellPosition cellPosition = new CellPosition(1, 1);
        shipStructure.set(cellPosition);
        assertTrue(shipStructure.addUpgrade(cellPosition, new Shield()));
        assertFalse(shipStructure.addUpgrade(new CellPosition(5, 5),
            new Turret())); // this is an invalid position.

        CellPosition otherCellPosition = new CellPosition(2, 2);
        assertFalse(shipStructure.addUpgrade(otherCellPosition, new Shield()));
    }

    @Test
    void testInitializationsFromShipConfig() {
        for (GridCell<Fuselage> cell : shipStructure.iterable()) {
            CellPosition pos = cell.pos();
            Fuselage fuselage = cell.value();
            if (pos.equals(new CellPosition(1, 1))) {
                assertNull(fuselage.getUpgrade());
            } else if (pos.equals(new CellPosition(2, 2))) {
                assertNotNull(fuselage.getUpgrade());
                assertEquals(UpgradeType.SHIELD, fuselage.getUpgrade().getType());
            }
        }
    }

    @Test
    void testBoundaryConditions() {
        CellPosition outOfBounds = new CellPosition(11, 11);
        assertFalse(shipStructure.set(outOfBounds));
        assertFalse(shipStructure.addUpgrade(outOfBounds, new Shield()));
    }

    @Test
    void testDifferentUpgrades() {
        CellPosition cellPosition = new CellPosition(3, 3);
        shipStructure.set(cellPosition);
        assertTrue(shipStructure.addUpgrade(cellPosition, new Thruster()));
    }

    @Test
    void testOccupiedCells() {
        CellPosition cellPosition = new CellPosition(2, 2);
        assertFalse(shipStructure.addUpgrade(cellPosition, new Turret()));
    }

}