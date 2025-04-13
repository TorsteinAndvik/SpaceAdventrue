package model.ShipComponents.Components;

import grid.CellPosition;
import grid.Grid;
import grid.GridCell;
import grid.IGrid;
import model.ShipComponents.ShipConfig;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.Player;
import model.SpaceCharacters.SpaceShip;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipStructureTest {

    private ShipStructure shipStructure;
    private ShipStructure shipStructureLarge;
    private ShipConfig shipConfig;
    private ShipConfig shipConfigLarge;

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

        // make a larger to test adding all types of upgrades
        ShipConfig.Upgrade upgradeTurret = new ShipConfig.Upgrade();
        upgradeTurret.type = UpgradeType.TURRET;
        upgradeTurret.level = 1;

        ShipConfig.ShipComponent component3 = new ShipConfig.ShipComponent();
        component3.x = 3;
        component3.y = 3;
        component3.upgrade = upgradeTurret;

        ShipConfig.Upgrade upgradeThruster = new ShipConfig.Upgrade();
        upgradeThruster.type = UpgradeType.THRUSTER;
        upgradeThruster.level = 1;

        ShipConfig.ShipComponent component4 = new ShipConfig.ShipComponent();
        component4.x = 4;
        component4.y = 4;
        component4.upgrade = upgradeThruster;

        shipConfigLarge = new ShipConfig();
        shipConfigLarge.width = 10;
        shipConfigLarge.height = 10;
        shipConfigLarge.components = Arrays.asList(component1, component2, component3, component4);

        shipStructureLarge = new ShipStructure(shipConfigLarge);
    }

    @Test
    void testInitializationWithDimensions() {
        ShipStructure structure = new ShipStructure(10, 10);
        CellPosition cellPosition = new CellPosition(0, 0);
        assertTrue(structure.set(cellPosition));
    }

    @Test
    void testInitializationsFromGridTest() {
        IGrid<Fuselage> grid = new Grid<>(10, 10);
        grid.set(new CellPosition(0, 0), new Fuselage());
        ShipStructure structure = new ShipStructure(grid);
        assertNotNull(structure);
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
        assertFalse(shipStructure.addUpgrade(cellPosition, null));
        // upgrade can't be null.
        assertTrue(shipStructure.addUpgrade(cellPosition, new Shield()));
        assertFalse(shipStructure.addUpgrade(new CellPosition(5, 5),
                new Turret())); // this is an invalid position.

        CellPosition otherCellPosition = new CellPosition(2, 2);
        assertFalse(shipStructure.addUpgrade(otherCellPosition, new Shield()));
    }

    @Test
    void testInitializationsFromShipConfig() {
        for (GridCell<Fuselage> cell : shipStructureLarge) {
            CellPosition pos = cell.pos();
            Fuselage fuselage = cell.value();
            if (pos.equals(new CellPosition(1, 1))) {
                assertNull(fuselage.getUpgrade());

            } else if (pos.equals(new CellPosition(2, 2))) {
                assertNotNull(fuselage.getUpgrade());
                assertEquals(UpgradeType.SHIELD, fuselage.getUpgrade().getType());

            } else if (pos.equals(new CellPosition(3, 3))) {
                assertNotNull(fuselage.getUpgrade());
                assertEquals(UpgradeType.TURRET, fuselage.getUpgrade().getType());

            } else if (pos.equals(new CellPosition(4, 4))) {
                assertNotNull(fuselage.getUpgrade());
                assertEquals(UpgradeType.THRUSTER, fuselage.getUpgrade().getType());
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

    @Test
    void correctMassTest() {
        float fuselageMass = PhysicsParameters.fuselageMass;
        float upgradeMass = PhysicsParameters.shipUpgradeMass;

        float startMass = 2f * fuselageMass + upgradeMass;
        assertEquals(startMass, shipStructure.getMass());

        float upgradeAddedMass = startMass + upgradeMass;
        shipStructure.addUpgrade(new CellPosition(1, 1), new Thruster());
        assertEquals(upgradeAddedMass, shipStructure.getMass());

        float emptyFuselageAddedMass = upgradeAddedMass + fuselageMass;
        shipStructure.set(new CellPosition(1, 2));
        assertEquals(emptyFuselageAddedMass, shipStructure.getMass());

        float fuselageWithUgradeAddedMass = emptyFuselageAddedMass + fuselageMass + upgradeMass;
        shipStructure.set(new CellPosition(2, 1), new Fuselage(new Shield()));
        assertEquals(fuselageWithUgradeAddedMass, shipStructure.getMass());
    }

    @Test
    void correctCenterOfMassTest() {
        float fuselageMass = PhysicsParameters.fuselageMass;
        float upgradeMass = PhysicsParameters.shipUpgradeMass;

        float startMass = 2f * fuselageMass + upgradeMass;
        FloatPair startCM = new FloatPair(
                (1 * fuselageMass + 2 * (fuselageMass + upgradeMass)) / startMass,
                (1 * fuselageMass + 2 * (fuselageMass + upgradeMass)) / startMass);

        assertEquals(startCM, shipStructure.getCenterOfMass());
        assertEquals(startCM,
                ShipStructure.getMassProperties(shipStructure).centerOfMass());

        float upgradeAddedMass = startMass + upgradeMass;
        shipStructure.addUpgrade(new CellPosition(1, 1), new Thruster());
        FloatPair upgradeAddedCM = new FloatPair(
                (1 * (fuselageMass + upgradeMass) + 2 * (fuselageMass + upgradeMass))
                        / upgradeAddedMass,
                (1 * (fuselageMass + upgradeMass) + 2 * (fuselageMass + upgradeMass))
                        / upgradeAddedMass);
        assertEquals(upgradeAddedCM, shipStructure.getCenterOfMass());
        assertEquals(upgradeAddedCM,
                ShipStructure.getMassProperties(shipStructure).centerOfMass());

        float emptyFuselageAddedMass = upgradeAddedMass + fuselageMass;
        shipStructure.set(new CellPosition(1, 2));
        FloatPair emptyFuselageAddedCM = new FloatPair(
                (1 * (fuselageMass + upgradeMass) + 2 * (2 * fuselageMass + upgradeMass))
                        / emptyFuselageAddedMass,
                (1 * (2 * fuselageMass + upgradeMass) + 2 * (fuselageMass + upgradeMass))
                        / emptyFuselageAddedMass);
        assertEquals(emptyFuselageAddedCM, shipStructure.getCenterOfMass());
        assertEquals(emptyFuselageAddedCM,
                ShipStructure.getMassProperties(shipStructure).centerOfMass());

        float fuselageWithUgradeAddedMass = emptyFuselageAddedMass + fuselageMass + upgradeMass;
        shipStructure.set(new CellPosition(2, 1), new Fuselage(new Shield()));
        FloatPair fuselageWithUgradeAddedCM = new FloatPair(
                (1 * (2 * fuselageMass + 2 * upgradeMass) + 2 * (2 * fuselageMass + upgradeMass))
                        / fuselageWithUgradeAddedMass,
                (1 * (2 * fuselageMass + upgradeMass) + 2 * (2 * fuselageMass + 2 * upgradeMass))
                        / fuselageWithUgradeAddedMass);
        assertEquals(fuselageWithUgradeAddedCM, shipStructure.getCenterOfMass());
        assertEquals(fuselageWithUgradeAddedCM,
                ShipStructure.getMassProperties(shipStructure).centerOfMass());

    }

    @Test
    void expandStructureGridCenteredTest() {

        ShipStructure ship = new ShipStructure(1, 2);
        ship.set(new CellPosition(0, 0), new Fuselage(new Thruster()));
        ship.set(new CellPosition(1, 0), new Fuselage(new Turret()));

        assertTrue(ship.hasFuselage(new CellPosition(0, 0)));
        assertTrue(ship.hasFuselage(new CellPosition(1, 0)));

        ship.expandGrid(2, 2, true);

        assertFalse(ship.hasFuselage(new CellPosition(0, 0)));
        assertFalse(ship.hasFuselage(new CellPosition(1, 0)));

        assertTrue(ship.hasFuselage(new CellPosition(1, 1)));
        assertTrue(ship.hasFuselage(new CellPosition(2, 1)));

    }

    @Test
    void expandStructureGridTest() {

        ShipStructure ship = new ShipStructure(1, 2);
        ship.set(new CellPosition(0, 0), new Fuselage(new Thruster()));
        ship.set(new CellPosition(1, 0), new Fuselage(new Turret()));

        assertTrue(ship.hasFuselage(new CellPosition(0, 0)));
        assertTrue(ship.hasFuselage(new CellPosition(1, 0)));

        int offset = 2;
        ship.expandGrid(offset, offset, false);

        assertTrue(ship.hasFuselage(new CellPosition(offset, offset)));
        assertTrue(ship.hasFuselage(new CellPosition(offset + 1, offset)));

    }

    @Test
    void canBuildAtTest() {
        ShipStructure structure = new ShipStructure(1, 2);
        structure.set(new CellPosition(0, 0), new Fuselage(new Thruster()));
        structure.set(new CellPosition(1, 0), new Fuselage(new Turret()));

        CellPosition outsideGridPos = new CellPosition(8, 0);
        CellPosition alreadyTakenPos = new CellPosition(1, 1);
        CellPosition cornerPos = new CellPosition(2, 1);
        CellPosition validPos = new CellPosition(2, 0);

        assertFalse(structure.canBuildAt(outsideGridPos));
        assertFalse(structure.canBuildAt(alreadyTakenPos));
        assertFalse(structure.canBuildAt(cornerPos));
        assertTrue(structure.canBuildAt(validPos));

    }

    @Test
    void expandShipTopTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
        ShipStructure structure = ship.getShipStructure();
        structure.set(new CellPosition(0, 0), new Fuselage());
        structure.set(new CellPosition(1, 0), new Fuselage());

        assertNotNull(ShipStructure.getExpandedGrid(shipStructure.getGrid(), 1, 2, true));

        IGrid<Fuselage> shipGrid = structure.getGrid();

        assertEquals(shipGrid, ShipStructure.getExpandedGrid(shipGrid, 0, 0, true));
        assertEquals(shipGrid, ShipStructure.getExpandedGrid(shipGrid, -3, 0, true));
        assertEquals(shipGrid, ShipStructure.getExpandedGrid(shipGrid, 0, -1, true));
        assertEquals(shipGrid, ShipStructure.getExpandedGrid(shipGrid, -2, -1, true));
        assertNotEquals(shipGrid, ShipStructure.getExpandedGrid(shipGrid, 1, 1, true));
        assertNotEquals(shipGrid, ShipStructure.getExpandedGrid(shipGrid, 0, 1, true));
        assertNotEquals(shipGrid, ShipStructure.getExpandedGrid(shipGrid, 1, 0, true));

        IGrid<Fuselage> buildGrid = ShipStructure.getExpandedGrid(structure.getGrid(), 2, 2, true);

        // Offset position due to expanded grid in updateScreen
        CellPosition posAbove = new CellPosition(2, 0).offset(1, 1);

        assertEquals(1, structure.getWidth());
        assertEquals(2, structure.getHeight());

        assertTrue(buildGrid.positionIsOnGrid(posAbove));
        assertTrue(buildGrid.isEmptyAt(posAbove));
        assertTrue(structure.updateWithFuselage(posAbove));
        assertFalse(
                structure.updateWithFuselage(new CellPosition(structure.getHeight(), structure.getWidth())));

        assertEquals(1, structure.getWidth());
        assertEquals(3, structure.getHeight());
    }

    @Test
    void expandShipBottomTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
        ShipStructure structure = ship.getShipStructure();
        structure.set(new CellPosition(0, 0), new Fuselage());
        structure.set(new CellPosition(1, 0), new Fuselage());

        IGrid<Fuselage> buildGrid = ShipStructure.getExpandedGrid(structure.getGrid(), 2, 2, true);

        // Offset position due to expanded grid in updateScreen
        CellPosition posBelow = new CellPosition(0, 0).offset(0, 1);

        assertEquals(1, structure.getWidth());
        assertEquals(2, structure.getHeight());

        assertTrue(buildGrid.positionIsOnGrid(posBelow));
        assertTrue(buildGrid.isEmptyAt(posBelow));
        assertTrue(structure.updateWithFuselage(posBelow));

        assertEquals(1, structure.getWidth());
        assertEquals(3, structure.getHeight());
    }

    @Test
    void expandShipLeftTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
        ShipStructure structure = ship.getShipStructure();
        structure.set(new CellPosition(0, 0), new Fuselage());
        structure.set(new CellPosition(1, 0), new Fuselage());

        IGrid<Fuselage> buildGrid = ShipStructure.getExpandedGrid(structure.getGrid(), 2, 2, true);

        // Offset position due to expanded grid in updateScreen
        CellPosition posLeft = new CellPosition(1, 0).offset(1, 0);

        assertEquals(1, structure.getWidth());
        assertEquals(2, structure.getHeight());

        assertTrue(buildGrid.positionIsOnGrid(posLeft));
        assertTrue(buildGrid.isEmptyAt(posLeft));
        assertTrue(structure.updateWithFuselage(posLeft));

        assertEquals(2, structure.getWidth());
        assertEquals(2, structure.getHeight());
    }

    @Test
    void expandShipRightTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
        ShipStructure structure = ship.getShipStructure();
        structure.set(new CellPosition(0, 0), new Fuselage());
        structure.set(new CellPosition(1, 0), new Fuselage());

        IGrid<Fuselage> buildGrid = ShipStructure.getExpandedGrid(structure.getGrid(), 2, 2, true);

        // Offset position due to expanded grid in updateScreen
        CellPosition posRight = new CellPosition(1, 1).offset(1, 1);

        assertEquals(1, structure.getWidth());
        assertEquals(2, structure.getHeight());

        assertTrue(buildGrid.positionIsOnGrid(posRight));
        assertTrue(buildGrid.isEmptyAt(posRight));
        assertTrue(structure.updateWithFuselage(posRight));

        assertEquals(2, structure.getWidth());
        assertEquals(2, structure.getHeight());
    }

    @Test
    void getUpgradeTypePositionsTest() {
        ShipStructure structure = new ShipStructure(3, 3);
        structure.set(new CellPosition(0, 0), new Fuselage(new Thruster()));
        structure.set(new CellPosition(0, 1), new Fuselage(new Thruster()));
        structure.set(new CellPosition(0, 2), new Fuselage(new Shield()));
        structure.set(new CellPosition(1, 1), new Fuselage());

        List<CellPosition> actualShieldPositions = new ArrayList<>(Arrays.asList(new CellPosition(0, 2)));
        List<CellPosition> actualThrusterPositions = new ArrayList<>(
                Arrays.asList(new CellPosition(0, 0), new CellPosition(0, 1)));
        List<CellPosition> actualTurretPositions = new ArrayList<>();

        List<CellPosition> shieldPositions = structure.getUpgradeTypePositions(UpgradeType.SHIELD);
        List<CellPosition> thrusterPositions = structure.getUpgradeTypePositions(UpgradeType.THRUSTER);
        List<CellPosition> turretPositions = structure.getUpgradeTypePositions(UpgradeType.TURRET);

        assertEquals(actualShieldPositions.size(), shieldPositions.size());
        assertEquals(actualThrusterPositions.size(), thrusterPositions.size());
        assertEquals(actualTurretPositions.size(), turretPositions.size());

        for (CellPosition position : actualShieldPositions) {
            assertTrue(shieldPositions.contains(position));
        }

        for (CellPosition position : actualThrusterPositions) {
            assertTrue(thrusterPositions.contains(position));
        }

        for (CellPosition position : actualTurretPositions) {
            assertTrue(turretPositions.contains(position));
        }
    }

    @Test
    void upgradePositionTest() {
        assertFalse(shipStructure.hasUpgrade(new CellPosition(1, 1)));
        assertFalse(shipStructure.hasUpgrade(new CellPosition(5, 5)));
        assertTrue(shipStructure.hasUpgrade(new CellPosition(2, 2)));

        assertEquals(UpgradeType.SHIELD, shipStructure.getUpgradeType(new CellPosition(2, 2)));
    }

    @Test
    void isValidFuselagePositionTest() {
        assertFalse(shipStructure.isValidFuselagePosition(new CellPosition(1, 1)));
        assertFalse(shipStructure.isValidFuselagePosition(new CellPosition(2, 2)));
        assertTrue(shipStructure.isValidFuselagePosition(new CellPosition(1, 2)));
    }

    @Test
    void isValidUpgradePositionTest() {
        assertTrue(shipStructure.isValidUpgradePosition(new CellPosition(1, 1)));
        assertFalse(shipStructure.isValidUpgradePosition(new CellPosition(2, 2)));

        assertFalse(shipStructure.isValidUpgradePosition(new CellPosition(-1, 2)));
        assertFalse(shipStructure.isValidUpgradePosition(new CellPosition(3, 4)));

        shipStructure.set(new CellPosition(3, 4));

        assertTrue(shipStructure.isValidUpgradePosition(new CellPosition(3, 4)));
    }

    @Test
    void isOnGridTest() {
        assertTrue(shipStructure.isOnGrid(new CellPosition(0, 0)));
        assertTrue(shipStructure.isOnGrid(new CellPosition(5, 0)));
        assertTrue(shipStructure.isOnGrid(new CellPosition(0, 6)));
        assertTrue(shipStructure.isOnGrid(new CellPosition(9, 9)));

        assertFalse(shipStructure.isOnGrid(new CellPosition(0, 10)));
        assertFalse(shipStructure.isOnGrid(new CellPosition(10, 0)));
        assertFalse(shipStructure.isOnGrid(new CellPosition(10, 10)));

        assertFalse(shipStructure.isOnGrid(new CellPosition(-1, 0)));
        assertFalse(shipStructure.isOnGrid(new CellPosition(0, -1)));
        assertFalse(shipStructure.isOnGrid(new CellPosition(-1, -1)));

        assertFalse(shipStructure.isOnGrid(new CellPosition(15, 0)));
        assertFalse(shipStructure.isOnGrid(new CellPosition(0, 15)));
        assertFalse(shipStructure.isOnGrid(new CellPosition(15, 15)));
    }

    @Test
    void resourceValueTest() {
        assertEquals(0, new ShipStructure(1, 1).getResourceValue());
        assertEquals(2 * Fuselage.RESOURCE_VALUE + Shield.RESOURCE_BASE_VALUE, shipStructure.getResourceValue());
    }
}