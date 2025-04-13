package model.ShipComponents.Components;

import grid.CellPosition;
import grid.GridCell;
import model.ShipComponents.ShipConfig;
import model.ShipComponents.ShipStructure;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.Ships.Player;
import model.SpaceCharacters.Ships.SpaceShip;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;

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
        component2.x = 1;
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
        assertTrue(structure.addUpgrade(cellPosition, new Fuselage()));
    }


    @Test
    void testAddUpgrade() {
        CellPosition cellPosition = new CellPosition(1, 1);
        assertTrue(shipStructure.addUpgrade(cellPosition, new Shield()));
        assertFalse(shipStructure.addUpgrade(new CellPosition(5, 5),
                new Turret())); // this is an invalid position.

        CellPosition otherCellPosition = new CellPosition(2, 2);
        assertFalse(shipStructure.addUpgrade(otherCellPosition, new Shield()));
    }

    @Test
    void testInitializationsFromShipConfig() {
        for (GridCell<Fuselage> cell : shipStructure) {
            CellPosition pos = cell.pos();
            Fuselage fuselage = cell.value();
            if (pos.equals(new CellPosition(1, 1))) {
                assertNull(fuselage.getUpgrade());
            } else if (pos.equals(new CellPosition(2, 1))) {
                assertNotNull(fuselage.getUpgrade());
                assertEquals(UpgradeType.SHIELD, fuselage.getUpgrade().getType());
            }
        }
    }

    @Test
    void testBoundaryConditions() {
        CellPosition outOfBounds = new CellPosition(11, 11);
        assertFalse(shipStructure.addUpgrade(outOfBounds, new Fuselage()));
        assertFalse(shipStructure.addUpgrade(outOfBounds, new Shield()));
    }

    @Test
    void testDifferentUpgrades() {
        CellPosition cellPosition = new CellPosition(3, 1);
        shipStructure.addUpgrade(cellPosition, new Fuselage());
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
        assertTrue(shipStructure.addUpgrade(new CellPosition(1, 1), new Thruster()));
        assertEquals(upgradeAddedMass, shipStructure.getMass());

        float emptyFuselageAddedMass = upgradeAddedMass + fuselageMass;
        assertTrue(shipStructure.addUpgrade(new CellPosition(0, 1), new Fuselage()));
        assertEquals(emptyFuselageAddedMass, shipStructure.getMass());

        float fuselageWithUpgradeAddedMass = emptyFuselageAddedMass + fuselageMass + upgradeMass;
        assertTrue(shipStructure.addUpgrade(new CellPosition(3, 1), new Fuselage(new Shield())));
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
        pos = new CellPosition(2, 1);

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
        pos = new CellPosition(1, 2);
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
    void ExpandStructureGridCenteredTest() {

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
    void ExpandStructureGridTest() {

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
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
        ShipStructure structure = ship.getShipStructure();
        structure.addUpgrade(new CellPosition(0, 0), new Fuselage());
        structure.addUpgrade(new CellPosition(1, 0), new Fuselage());
        structure.expandGrid(2, 2, true);

        CellPosition posAbove = new CellPosition(3, 1);

        assertTrue(structure.isOnGrid(posAbove));
        assertTrue(structure.canBuildAt(posAbove));
        assertTrue(structure.addUpgrade(posAbove, new Fuselage()));

        structure.normalize();
        assertEquals(1, structure.getWidth());
        assertEquals(3, structure.getHeight());

    }

    @Test
    void expandShipBottomTest() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
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
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
        ShipStructure structure = ship.getShipStructure();

        structure.addUpgrade(new CellPosition(0, 0), new Fuselage());
        structure.addUpgrade(new CellPosition(1, 0), new Fuselage());
        structure.expandGrid(2, 2, true);
//        IGrid<Fuselage> buildGrid = ShipStructure.getExpandedGrid(structure.getGridCopy(), 2, 2, true);

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
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
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
    void testShrinkToFit() {
        SpaceShip ship = new Player(new ShipStructure(1, 2), "name", "description", 100, 0, 0);
        shipStructure = ship.getShipStructure();
        assertTrue(shipStructure.addUpgrade(new CellPosition(0, 0), new Fuselage()));
        assertTrue(shipStructure.addUpgrade(new CellPosition(1, 0), new Fuselage()));

        int cols = shipStructure.getWidth();
        int rows = shipStructure.getHeight();
        int expNum = 2;

        shipStructure.expandGrid(expNum, expNum, true);
        assertEquals(cols + expNum, shipStructure.getWidth());
        assertEquals(rows + expNum, shipStructure.getHeight());

        shipStructure.normalize();
        assertEquals(cols, shipStructure.getWidth());
        assertEquals(rows, shipStructure.getHeight());

    }


}