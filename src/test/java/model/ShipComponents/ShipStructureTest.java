package model.ShipComponents;

import grid.CellPosition;
import grid.Grid;
import grid.GridCell;
import grid.IGrid;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.Shield;
import model.ShipComponents.Components.Thruster;
import model.ShipComponents.Components.Turret;
import model.ShipComponents.Components.stats.Stat;
import model.SpaceCharacters.Ships.Player;
import model.SpaceCharacters.Ships.SpaceShip;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipStructureTest {

    private ShipStructure shipStructure;
    private ShipStructure shipStructureLarge;

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
        component2.y = 1;
        component2.upgrade = upgrade;

        ShipConfig shipConfig = new ShipConfig();
        shipConfig.width = 10;
        shipConfig.height = 10;
        shipConfig.components = Arrays.asList(component1, component2);

        shipStructure = new ShipStructure(shipConfig);

        // make a larger structure to test adding all types of upgrades
        ShipConfig.Upgrade upgradeTurret = new ShipConfig.Upgrade();
        upgradeTurret.type = UpgradeType.TURRET;
        upgradeTurret.level = 1;

        ShipConfig.ShipComponent component3 = new ShipConfig.ShipComponent();
        component3.x = 3;
        component3.y = 1;
        component3.upgrade = upgradeTurret;

        ShipConfig.Upgrade upgradeThruster = new ShipConfig.Upgrade();
        upgradeThruster.type = UpgradeType.THRUSTER;
        upgradeThruster.level = 1;

        ShipConfig.ShipComponent component4 = new ShipConfig.ShipComponent();
        component4.x = 4;
        component4.y = 1;
        component4.upgrade = upgradeThruster;

        ShipConfig.Upgrade upgradeFuselage = new ShipConfig.Upgrade();
        upgradeFuselage.type = UpgradeType.FUSELAGE;
        upgradeFuselage.level = 1;

        ShipConfig.ShipComponent component5 = new ShipConfig.ShipComponent();
        component5.x = 5;
        component5.y = 1;
        component5.upgrade = upgradeFuselage;

        ShipConfig shipConfigLarge = new ShipConfig();
        shipConfigLarge.width = 10;
        shipConfigLarge.height = 10;
        shipConfigLarge.components = Arrays.asList(component1, component2, component3, component4, component5);

        shipStructureLarge = new ShipStructure(shipConfigLarge);
    }

    @Test
    void testInitializationWithDimensions() {
        ShipStructure structure = new ShipStructure(10, 10);
        assertTrue(structure.setFuselage(new CellPosition(0, 0)));
        assertFalse(structure.setFuselage(new CellPosition(9, 9)));

        structure = new ShipStructure(10, 10);
        assertTrue(structure.setFuselage(new CellPosition(9, 9)));
        assertFalse(structure.setFuselage(new CellPosition(10, 9)));
        assertFalse(structure.setFuselage(new CellPosition(9, 10)));
    }

    @Test
    void testInitializationsFromGridTest() {
        IGrid<Fuselage> grid = new Grid<>(10, 10);
        grid.set(new CellPosition(0, 0), new Fuselage());
        ShipStructure structure = new ShipStructure(grid);
        assertNotNull(structure);
        assertTrue(structure.hasFuselage(new CellPosition(0, 0)));
    }

    @Test
    void testSetFuselage() {
        CellPosition cellPosition = new CellPosition(1, 0);
        assertTrue(shipStructure.setFuselage(cellPosition));
        assertFalse(shipStructure.setFuselage(cellPosition)); // we shouldn't be allowed to set twice

        assertFalse(shipStructure.setFuselage(new CellPosition(1, 1)));

        // test that we can only build if connected to fuselage
        assertFalse(shipStructure.setFuselage(new CellPosition(3, 1)));
        assertTrue(shipStructure.setFuselage(new CellPosition(2, 1)));
        assertTrue(shipStructure.setFuselage(new CellPosition(3, 1)));
    }

    @Test
    void testSetFuselageWithUpgrade() {
        CellPosition cellPosition = new CellPosition(0, 1);
        assertTrue(shipStructure.setFuselage(cellPosition, new Fuselage(new Shield())));
        assertFalse(shipStructure.setFuselage(cellPosition, new Fuselage(new Turret())));
        assertTrue(shipStructure.hasUpgrade(cellPosition));
    }

    @Test
    void testAddUpgrade() {
        CellPosition cellPosition = new CellPosition(1, 1);
        assertFalse(shipStructure.addUpgrade(cellPosition, null));
        // upgrade can't be null.
        assertTrue(shipStructure.addUpgrade(cellPosition, new Shield()));
        assertFalse(shipStructure.addUpgrade(new CellPosition(5, 5),
                new Turret())); // this is an invalid position.
        assertFalse(shipStructure.addUpgrade(new CellPosition(11, -2),
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

            } else if (pos.equals(new CellPosition(1, 2))) {
                assertNotNull(fuselage.getUpgrade());
                assertEquals(UpgradeType.SHIELD, fuselage.getUpgrade().getType());

            } else if (pos.equals(new CellPosition(1, 3))) {
                assertNotNull(fuselage.getUpgrade());
                assertEquals(UpgradeType.TURRET, fuselage.getUpgrade().getType());

            } else if (pos.equals(new CellPosition(1, 4))) {
                assertNotNull(fuselage.getUpgrade());
                assertEquals(UpgradeType.THRUSTER, fuselage.getUpgrade().getType());

            } else if (pos.equals(new CellPosition(1, 5))) {
                assertNull(fuselage.getUpgrade());
            }
        }
    }

    @Test
    void testBoundaryConditions() {
        CellPosition outOfBounds = new CellPosition(10, 10);
        assertFalse(shipStructure.addUpgrade(outOfBounds, new Fuselage()));
        assertFalse(shipStructure.addUpgrade(outOfBounds, new Shield()));
    }

    @Test
    void testDifferentUpgrades() {
        CellPosition cellPosition = new CellPosition(2, 1);
        assertTrue(shipStructure.addUpgrade(cellPosition, new Fuselage()));
        assertFalse(shipStructure.addUpgrade(cellPosition, new Fuselage()));
        assertTrue(shipStructure.addUpgrade(cellPosition, new Thruster()));
    }

    @Test
    void testOccupiedCells() {
        CellPosition cellPosition = new CellPosition(1, 2);
        assertFalse(shipStructure.addUpgrade(cellPosition, new Turret()));
    }

    @Test
    void correctMassTest() {
        float fuselageMass = PhysicsParameters.fuselageMass;
        float upgradeMass = PhysicsParameters.shipUpgradeMass;

        float startMass = 2f * fuselageMass + upgradeMass;
        assertEquals(startMass, shipStructure.getMass());

        float upgradeAddedMass = startMass + upgradeMass;
        assertTrue(shipStructure.addUpgrade(new CellPosition(1, 1), new Thruster()));
        assertEquals(upgradeAddedMass, shipStructure.getMass());

        float emptyFuselageAddedMass = upgradeAddedMass + fuselageMass;
        assertTrue(shipStructure.addUpgrade(new CellPosition(0, 1), new Fuselage()));
        assertEquals(emptyFuselageAddedMass, shipStructure.getMass());

        float fuselageWithUpgradeAddedMass = emptyFuselageAddedMass + fuselageMass + upgradeMass;
        assertTrue(shipStructure.addUpgrade(new CellPosition(2, 1), new Fuselage(new Shield())));
        assertEquals(fuselageWithUpgradeAddedMass, shipStructure.getMass());
    }

    @Test
    void correctCenterOfMassTest() {
        float fuselageMass = PhysicsParameters.fuselageMass;
        float upgradeMass = PhysicsParameters.shipUpgradeMass;

        // Simulate the two first fuselage placements
        float oldMass = 0;
        float currentMass = fuselageMass;
        float newMass = oldMass + currentMass;
        CellPosition pos = new CellPosition(1, 1);

        FloatPair startCM = new FloatPair(0, 0);
        startCM = new FloatPair(
                ((oldMass * startCM.x()) + currentMass * pos.col()) / newMass,
                ((oldMass * startCM.y()) + currentMass * pos.row()) / newMass);

        oldMass = newMass;
        currentMass = fuselageMass + upgradeMass;
        newMass = oldMass + currentMass;
        pos = new CellPosition(1, 2);

        startCM = new FloatPair(
                ((oldMass * startCM.x()) + currentMass * pos.col()) / newMass,
                ((oldMass * startCM.y()) + currentMass * pos.row()) / newMass);

        assertEquals(startCM, shipStructure.getCenterOfMass());
        assertEquals(startCM,
                shipStructure.getMassProperties().centerOfMass());

        pos = new CellPosition(1, 1);
        assertTrue(shipStructure.addUpgrade(pos, new Thruster()));

        oldMass = newMass;
        currentMass = upgradeMass;
        newMass = oldMass + currentMass;
        FloatPair upgradeAddedCM = new FloatPair(
                ((oldMass * startCM.x()) + currentMass * pos.col()) / newMass,
                ((oldMass * startCM.y()) + currentMass * pos.row()) / newMass);
        assertEquals(upgradeAddedCM, shipStructure.getCenterOfMass());
        assertEquals(upgradeAddedCM, shipStructure.getMassProperties().centerOfMass());

        oldMass = newMass;
        currentMass = fuselageMass;
        newMass = oldMass + currentMass;
        pos = new CellPosition(2, 1);
        assertTrue(shipStructure.addUpgrade(pos, new Fuselage()));

        FloatPair emptyFuselageAddedCM = new FloatPair(
                ((oldMass * upgradeAddedCM.x()) + currentMass * pos.col()) / newMass,
                ((oldMass * upgradeAddedCM.y()) + currentMass * pos.row()) / newMass);

        assertEquals(emptyFuselageAddedCM, shipStructure.getCenterOfMass());
        assertEquals(emptyFuselageAddedCM, shipStructure.getMassProperties().centerOfMass());

        oldMass = newMass;
        currentMass = fuselageMass + upgradeMass;
        newMass = oldMass + currentMass;
        pos = new CellPosition(3, 1);
        assertTrue(shipStructure.addUpgrade(pos, new Fuselage(new Shield())));
        FloatPair fuselageWithUpgradeAddedCM = new FloatPair(
                ((oldMass * emptyFuselageAddedCM.x()) + currentMass * pos.col()) / newMass,
                ((oldMass * emptyFuselageAddedCM.y()) + currentMass * pos.row()) / newMass);

        assertEquals(fuselageWithUpgradeAddedCM, shipStructure.getCenterOfMass());
        assertEquals(fuselageWithUpgradeAddedCM, shipStructure.getMassProperties().centerOfMass());
    }

    @Test
    void expandStructureGridCenteredTest() {

        ShipStructure ship = new ShipStructure(1, 2);
        ship.addUpgrade(new CellPosition(0, 0), new Fuselage(new Thruster()));
        ship.addUpgrade(new CellPosition(1, 0), new Fuselage(new Turret()));

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
        ship.addUpgrade(new CellPosition(0, 0), new Fuselage(new Thruster()));
        ship.addUpgrade(new CellPosition(1, 0), new Fuselage(new Turret()));

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
        structure.addUpgrade(new CellPosition(0, 0), new Fuselage(new Thruster()));
        structure.addUpgrade(new CellPosition(1, 0), new Fuselage(new Turret()));
        structure.expandGrid(2, 2, true);

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
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 0f, 0f);
        ShipStructure structure = ship.getShipStructure();
        structure.addUpgrade(new CellPosition(0, 0), new Fuselage());
        structure.addUpgrade(new CellPosition(1, 0), new Fuselage());

        assertEquals(1, structure.getWidth());
        assertEquals(2, structure.getHeight());
        structure.expandGrid(2, 2, true);
        assertEquals(3, structure.getWidth());
        assertEquals(4, structure.getHeight());

        assertNotNull(structure.getExpandedGrid(shipStructure.getGridCopy(), 1, 2, true));

        IGrid<Fuselage> shipGrid = structure.getGridCopy();

        // check if invalid arguments return the original grid
        assertEquals(shipGrid, structure.getExpandedGrid(shipGrid, 0, 0, true));
        assertEquals(shipGrid, structure.getExpandedGrid(shipGrid, -3, 0, true));
        assertEquals(shipGrid, structure.getExpandedGrid(shipGrid, 0, -1, true));
        assertEquals(shipGrid, structure.getExpandedGrid(shipGrid, -2, -1, true));
        assertNotEquals(shipGrid, structure.getExpandedGrid(shipGrid, 1, 1, true));
        assertNotEquals(shipGrid, structure.getExpandedGrid(shipGrid, 0, 1, true));
        assertNotEquals(shipGrid, structure.getExpandedGrid(shipGrid, 1, 0, true));

        // check if valid arguments return the expected grid
        IGrid<Fuselage> buildGrid = structure.getExpandedGrid(structure.getGridCopy(), 2, 2, true);

        // Offset position due to expanded grid in updateScreen
        CellPosition posAbove = new CellPosition(2, 0).offset(1, 1);

        assertTrue(buildGrid.positionIsOnGrid(posAbove));
        assertTrue(buildGrid.isEmptyAt(posAbove));
        assertTrue(structure.updateWithFuselage(posAbove));
        assertFalse(
                structure.updateWithFuselage(new CellPosition(structure.getHeight(), structure.getWidth())));

        structure.normalize();
        assertEquals(2, structure.getWidth());
        assertEquals(2, structure.getHeight());
    }

    @Test
    void expandShipBottomTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 0f, 0f);
        ShipStructure structure = ship.getShipStructure();
        structure.addUpgrade(new CellPosition(0, 0), new Fuselage());
        structure.addUpgrade(new CellPosition(1, 0), new Fuselage());

        structure.expandGrid(2, 2, true);
        CellPosition posBelow = new CellPosition(0, 1);

        assertTrue(structure.isOnGrid(posBelow));
        assertTrue(structure.canBuildAt(posBelow));
        assertTrue(structure.addUpgrade(posBelow, new Fuselage()));

        structure.normalize();
        assertEquals(1, structure.getWidth());
        assertEquals(3, structure.getHeight());
    }

    @Test
    void expandShipLeftTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 0f, 0f);
        ShipStructure structure = ship.getShipStructure();

        assertTrue(structure.addUpgrade(new CellPosition(0, 0), new Fuselage()));
        assertTrue(structure.addUpgrade(new CellPosition(1, 0), new Fuselage()));
        structure.expandGrid(2, 2, true);

        // Offset position due to expanded grid in updateScreen
        CellPosition posLeft = new CellPosition(1, 0).offset(1, 0);

        assertEquals(3, structure.getWidth());
        assertEquals(4, structure.getHeight());

        assertTrue(structure.getGridCopy().positionIsOnGrid(posLeft));
        assertTrue(structure.getGridCopy().isEmptyAt(posLeft));
        assertTrue(structure.addUpgrade(posLeft, new Fuselage()));

        structure.normalize();

        assertEquals(2, structure.getWidth());
        assertEquals(2, structure.getHeight());
    }

    @Test
    void expandShipRightTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 0f, 0f);
        ShipStructure structure = ship.getShipStructure();
        structure.addUpgrade(new CellPosition(0, 0), new Fuselage());
        structure.addUpgrade(new CellPosition(1, 0), new Fuselage());

        structure.expandGrid(2, 2, true);

        // Offset position due to expanded grid in updateScreen
        CellPosition posRight = new CellPosition(1, 1).offset(1, 1);

        assertTrue(structure.getGridCopy().positionIsOnGrid(posRight));
        assertTrue(structure.getGridCopy().isEmptyAt(posRight));
        assertTrue(structure.addUpgrade(posRight, new Fuselage()));

        structure.normalize();

        assertEquals(2, structure.getWidth());
        assertEquals(2, structure.getHeight());
    }

    @Test
    void getUpgradeTypePositionsTest() {
        ShipStructure structure = new ShipStructure(3, 3);
        structure.setFuselage(new CellPosition(0, 0), new Fuselage(new Thruster()));
        structure.setFuselage(new CellPosition(0, 1), new Fuselage(new Thruster()));
        structure.setFuselage(new CellPosition(0, 2), new Fuselage(new Shield()));
        structure.setFuselage(new CellPosition(1, 1), new Fuselage());

        List<CellPosition> actualShieldPositions = new ArrayList<>(List.of(new CellPosition(0, 2)));
        List<CellPosition> actualThrusterPositions = new ArrayList<>(
                Arrays.asList(new CellPosition(0, 0), new CellPosition(0, 1)));

        List<CellPosition> shieldPositions = structure.getUpgradeTypePositions(UpgradeType.SHIELD);
        List<CellPosition> thrusterPositions = structure.getUpgradeTypePositions(UpgradeType.THRUSTER);
        List<CellPosition> turretPositions = structure.getUpgradeTypePositions(UpgradeType.TURRET);

        assertEquals(actualShieldPositions.size(), shieldPositions.size());
        assertEquals(actualThrusterPositions.size(), thrusterPositions.size());
        assertEquals(0, turretPositions.size());

        for (CellPosition position : actualShieldPositions) {
            assertTrue(shieldPositions.contains(position));
        }

        for (CellPosition position : actualThrusterPositions) {
            assertTrue(thrusterPositions.contains(position));
        }

    }

    @Test
    void upgradePositionTest() {
        assertFalse(shipStructure.hasUpgrade(new CellPosition(1, 1)));
        assertFalse(shipStructure.hasUpgrade(new CellPosition(5, 5)));
        assertTrue(shipStructure.hasUpgrade(new CellPosition(1, 2)));

        assertEquals(UpgradeType.SHIELD, shipStructure.getUpgradeType(new CellPosition(1, 2)));
    }

    @Test
    void isValidFuselagePositionTest() {
        assertFalse(shipStructure.isValidFuselagePosition(new CellPosition(1, 1)));
        assertTrue(shipStructure.isValidFuselagePosition(new CellPosition(2, 2)));
        assertFalse(shipStructure.isValidFuselagePosition(new CellPosition(1, 2)));
        assertFalse(shipStructure.isValidFuselagePosition(new CellPosition(-1, 12)));

        assertTrue(shipStructure.setFuselage(new CellPosition(0, 1)));
        assertFalse(shipStructure.hasFuselage(new CellPosition(-1, 1)));

        ShipStructure empty = new ShipStructure(3, 3);
        for (GridCell<Fuselage> cell : empty) {
            assertTrue(empty.isValidFuselagePosition(cell.pos()));
        }
    }

    @Test
    void isValidUpgradePositionTest() {
        assertTrue(shipStructure.isValidUpgradePosition(new CellPosition(1, 1)));
        assertFalse(shipStructure.isValidUpgradePosition(new CellPosition(2, 2)));
        assertFalse(shipStructure.isValidUpgradePosition(new CellPosition(1, 2)));

        assertFalse(shipStructure.isValidUpgradePosition(new CellPosition(-1, 2)));
        assertFalse(shipStructure.isValidUpgradePosition(new CellPosition(2, 2)));

        assertTrue(shipStructure.setFuselage(new CellPosition(2, 2)));

        assertTrue(shipStructure.isValidUpgradePosition(new CellPosition(2, 2)));
    }

    @Test
    void isValidBuildPositionTest() {
        assertFalse(shipStructure.isValidBuildPosition(new CellPosition(1, 1), new Fuselage()));
        assertTrue(shipStructure.isValidBuildPosition(new CellPosition(1, 1), new Shield()));

        assertTrue(shipStructure.isValidBuildPosition(new CellPosition(2, 2), new Fuselage()));
        assertFalse(shipStructure.isValidBuildPosition(new CellPosition(2, 2), new Turret()));
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
        assertEquals(2 * (new Fuselage().getResourceValue()) + (new Shield().getResourceValue()),
                shipStructure.getResourceValue());
    }

    @Test
    void testShrinkToFit() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 0f, 0f);
        shipStructure = ship.getShipStructure();
        assertTrue(shipStructure.addUpgrade(new CellPosition(0, 0), new Fuselage()));
        assertTrue(shipStructure.addUpgrade(new CellPosition(1, 0), new Fuselage()));

        int cols = shipStructure.getWidth();
        int rows = shipStructure.getHeight();
        assertEquals(1, cols);
        assertEquals(2, rows);

        int expNum = 2;

        shipStructure.expandGrid(expNum, expNum, true);
        assertEquals(cols + expNum, shipStructure.getWidth());
        assertEquals(rows + expNum, shipStructure.getHeight());

        shipStructure.shrinkToFit();
        assertEquals(cols, shipStructure.getWidth());
        assertEquals(rows, shipStructure.getHeight());
    }

    @Test
    void testShrinkToFitWhenAlreadyShrunkTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 0f, 0f);
        shipStructure = ship.getShipStructure();
        assertTrue(shipStructure.addUpgrade(new CellPosition(0, 0), new Fuselage()));
        assertTrue(shipStructure.addUpgrade(new CellPosition(1, 0), new Fuselage()));

        shipStructure.shrinkToFit();
        assertEquals(1, shipStructure.getWidth());
        assertEquals(2, shipStructure.getHeight());
    }

    @Test
    void testInvalidGrid() {
        IGrid<Fuselage> grid = new Grid<>(5, 5);
        grid.set(new CellPosition(0, 0), new Fuselage());
        grid.set(new CellPosition(4, 4), new Fuselage());

        assertThrows(IllegalArgumentException.class, () -> new ShipStructure(grid));
    }

    @Test
    void getCombinedStatsTest() {
        ShipStructure shipStructure = new ShipStructure(1, 2);
        shipStructure.setFuselage(new CellPosition(0, 0), new Fuselage(new Thruster()));
        shipStructure.setFuselage(new CellPosition(1, 0), new Fuselage(new Turret()));

        Fuselage fuselage = new Fuselage();
        Turret turret = new Turret();
        Thruster thruster = new Thruster();
        for (Stat stat : Stat.values()) {
            if (stat.intBased) {
                int value = 2 * fuselage.getModifiers().get(stat).intValue();
                value += turret.getModifiers().get(stat).intValue();
                value += thruster.getModifiers().get(stat).intValue();
                assertEquals(value, shipStructure.getCombinedStats().get(stat).intValue());
            } else {
                float value = 2f * fuselage.getModifiers().get(stat).floatValue();
                value += turret.getModifiers().get(stat).floatValue();
                value += thruster.getModifiers().get(stat).floatValue();
                assertEquals(value, shipStructure.getCombinedStats().get(stat).floatValue(), "issue with stat " + stat);
            }
        }
    }
}