package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import grid.CellPosition;
import grid.IGrid;
import java.util.HashSet;
import java.util.Set;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.ShipStructure;
import model.ShipComponents.UpgradeHandler;
import model.ShipComponents.UpgradeType;
import model.World.StoreItem;
import model.constants.GameState;
import model.utils.SpaceCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpgradeScreenModelTest {

    private UpgradeScreenModel model;

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
        GameStateModel gsm = new GameStateModel();
        gsm.startNewGame();
        gsm.changeState(GameState.UPGRADE);
        model = gsm.getUpgradeScreenModel();
    }

    @Test
    void testInitialization() {
        assertNotNull(model.getExpandedGrid());
    }

    @Test
    void testStoreShelf() {
        Set<StoreItem<UpgradeType>> storeItemSet = new HashSet<>();
        StoreItem<UpgradeType> fuselage = new StoreItem<>(UpgradeType.FUSELAGE, 100, "A fuselage");
        StoreItem<UpgradeType> turret = new StoreItem<>(UpgradeType.TURRET, 130, "A turret");
        storeItemSet.add(fuselage);
        storeItemSet.add(turret);

        model.addNewStoreStock(storeItemSet);
        assertEquals(2, model.getStoreShelf().size());
        assertTrue(model.getStoreShelf().contains(fuselage));
        assertTrue(model.getStoreShelf().contains(turret));

    }

    @Test
    void testInvalidFuselagePlacement() {
        UpgradeHandler uh = model.getUpgradeHandler();
        uh.expand();
        ShipStructure structure = model.getPlayer().getShipStructure();

        CellPosition cp = new CellPosition(0, 0);

        // Check that no neighbouring cells has fuselage
        for (CellPosition pos : SpaceCalculator.getOrthogonalNeighbours(cp)) {
            assertFalse(structure.hasFuselage(pos));
        }
        assertFalse(uh.canPlaceItem(new CellPosition(0, 0), UpgradeType.FUSELAGE));
    }

    @Test
    void testSimpleGettersAndSetters() {
        assertFalse(model.isUpgradeGrabbed());
        model.setUpgradeGrabbed(true);
        assertTrue(model.isUpgradeGrabbed());

        assertEquals(-1, model.getGrabbedUpgradeIndex());
        model.setGrabbedUpgradeIndex(1);
        assertEquals(1, model.getGrabbedUpgradeIndex());

        assertEquals(-1, model.getInspectedUpgradeIndex());
        model.setInspectedUpgradeIndex(2);
        assertEquals(2, model.getInspectedUpgradeIndex());

        assertFalse(model.isReleaseGrabbedUpgrade());
        model.setReleaseGrabbedUpgrade(true);
        assertTrue(model.isReleaseGrabbedUpgrade());

        assertFalse(model.upgradeInspectionModeIsActive());
        model.setUpgradeInspectionModeIsActive(true);
        assertTrue(model.upgradeInspectionModeIsActive());

        assertEquals(new Vector2(0, 0), model.getCameraPosition());

        assertTrue(model.getCameraZoomTextFadeCutoffTime() > 0);

        assertEquals(new Vector2(0, 0), model.getMousePosition());

    }

    @Test
    void testResetAfterUpdate() {
        model.setUpgradeGrabbed(true);
        model.setReleaseGrabbedUpgrade(true);
        model.setGrabbedUpgradeIndex(0);

        assertTrue(model.isReleaseGrabbedUpgrade());
        assertTrue(model.isUpgradeGrabbed());

        model.update(0);

        assertFalse(model.isReleaseGrabbedUpgrade());
        assertFalse(model.isUpgradeGrabbed());
    }

    @Test
    void testZoomInOut() {
        float zoomStep = 0.1f;
        float currentZoom = model.getCurrentZoom();
        float zoomChange = 5;
        model.updateCameraZoom(-zoomChange);
        assertEquals(currentZoom - zoomChange * zoomStep, model.getCurrentZoom());

        currentZoom = model.getCurrentZoom();
        float steps = 2;
        model.updateCameraZoom(zoomChange * steps);
        assertEquals(currentZoom + zoomChange * zoomStep * steps, model.getCurrentZoom());
    }

    @Test
    void testGetPlayerResources() {
        assertEquals(model.getPlayer().getInventory().getResourceCount(), model.getPlayerResources());
    }

    @Test
    void testUpdateCameraZoomDeltaTime() {
        model.update(1);
        assertEquals(0, model.getCameraZoomDeltaTime());
        assertFalse(model.isCameraZoomRecently());

        model.updateCameraZoom(0.1f);
        assertTrue(model.isCameraZoomRecently());

        model.update(10);
        assertEquals(10, model.getCameraZoomDeltaTime());
        assertFalse(model.isCameraZoomRecently());

        model.setCameraZoomRecently(true);
        assertTrue(model.isCameraZoomRecently());
    }

    @Test
    void testUpdateDragPosition() {
        assertEquals(0, model.getDragX());
        assertEquals(0, model.getDragY());

        model.updateDragPosition(1, 2);

        assertEquals(1, model.getDragX());
        assertEquals(2, model.getDragY());
        assertEquals(new Vector2(-1, -2), model.getDragDelta());
    }

    @Test
    void testOffsets() {
        IGrid<Fuselage> grid = model.getExpandedGrid();
        float worldWidth = 42;
        float worldHeight = 24;

        float upgradeToGridDelta = 2f; // local variable in `updateOffsets`
        model.updateOffsets(worldWidth, worldHeight);

        float gridOffsetX = (worldWidth - grid.cols()) / 2f;
        float gridOffsetY = (worldHeight - grid.rows() - upgradeToGridDelta) / 2f;
        assertEquals(gridOffsetX, model.getGridOffsetX());
        assertEquals(gridOffsetY, model.getGridOffsetY());

        float upgradeOffsetX = (worldWidth - model.getStoreShelf().size()) / 2f;
        float upgradeOffsetY = gridOffsetY + grid.rows() + upgradeToGridDelta / 2f;
        assertEquals(upgradeOffsetX, model.getUpgradeOffsetX());
        assertEquals(upgradeOffsetY, model.getUpgradeOffsetY());
    }

    @Test
    void testSuccessfulShipUpgrade() {
        int shipWidth = model.getPlayer().getShipStructure().getWidth();
        int playerResources = model.getPlayerResources();
        int upgradeIndex = 0;
        int upgradePrice = model.getStoreShelf().get(upgradeIndex).price();

        model.getUpgradeHandler().expand();
        ShipStructure structure = model.getPlayer().getShipStructure();

        CellPosition pos = new CellPosition(1, 0);

        assertFalse(structure.hasFuselage(pos));
        assertTrue(structure.isValidFuselagePosition(pos));

        // Simulate releasing the upgrade
        model.setUpgradeGrabbed(true);
        model.setGrabbedUpgradeIndex(upgradeIndex);
        model.setReleaseGrabbedUpgrade(true);
        model.setReleasedCellPosition(pos);

        model.update(0);

        CellPosition posAfterFit = new CellPosition(0, 0);
        assertTrue(structure.hasFuselage(new CellPosition(0, 1).offset(1, 1)));
        assertTrue(structure.hasFuselage(new CellPosition(1, 1).offset(1, 1)));
        assertTrue(structure.hasFuselage(posAfterFit.offset(1, 1)));

        model.getUpgradeHandler().exit();
        assertEquals(shipWidth + 1, structure.getWidth());

        assertTrue(upgradePrice > 0);
        assertEquals(playerResources - upgradePrice, model.getPlayerResources());
    }

    @Test
    void testSuccessfulBuildShipFuselageWithUpgrade() {
        int shipWidth = model.getPlayer().getShipStructure().getWidth();

        model.getUpgradeHandler().expand();
        ShipStructure structure = model.getPlayer().getShipStructure();

        assertTrue(structure.hasFuselage(new CellPosition(2, 1)));
        assertTrue(structure.hasFuselage(new CellPosition(1, 1)));
        assertTrue(structure.hasUpgrade(new CellPosition(2, 1)));
        assertTrue(structure.hasUpgrade(new CellPosition(1, 1)));

        CellPosition pos = new CellPosition(1, 0);
        for (int x = 0; x < 2; x++) {

            assertFalse(structure.hasFuselage(pos.offset(x, x)));
            assertTrue(structure.isValidFuselagePosition(pos.offset(x, x)));

            // Simulate releasing the upgrade
            model.setUpgradeGrabbed(true);
            model.setGrabbedUpgradeIndex(x); // First fuselage then shield
            model.setReleaseGrabbedUpgrade(true);
            model.setReleasedCellPosition(pos.offset(0, x));

            model.update(0);

            assertTrue(structure.hasFuselage(pos.offset(0, 1)));
            if (x == 1) {
                assertTrue(structure.hasUpgrade(new CellPosition(1, 1)));
            }

        }
        model.getUpgradeHandler().exit();
        assertEquals(shipWidth + 1, structure.getWidth());

    }

    @Test
    void testFailedShipUpgrade() {
        int shipWidth = model.getPlayer().getShipStructure().getWidth();
        int playerResources = model.getPlayerResources();

        model.getUpgradeHandler().expand();
        ShipStructure structure = model.getPlayer().getShipStructure();

        CellPosition pos = new CellPosition(0, 0);

        assertFalse(structure.hasFuselage(pos));
        assertFalse(structure.isValidFuselagePosition(pos));

        // Simulate releasing the upgrade
        model.setUpgradeGrabbed(true);
        model.setGrabbedUpgradeIndex(0);
        model.setReleaseGrabbedUpgrade(true);
        model.setReleasedCellPosition(pos);

        model.update(0);
        assertFalse(structure.hasFuselage(pos));

        model.exitUpgradeHandler();
        assertEquals(shipWidth, structure.getWidth());

        assertEquals(playerResources, model.getPlayerResources());
    }

}