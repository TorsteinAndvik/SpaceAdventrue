package controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import controller.audio.MusicManager;
import controller.audio.SoundManager;
import java.util.ArrayList;
import java.util.List;
import model.GameStateModel;
import model.ShipComponents.UpgradeHandler;
import model.ShipComponents.UpgradeType;
import model.UpgradeScreenModel;
import model.World.StoreItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.SpaceGame;
import view.bars.UpgradeStageDisplay;
import view.screens.UpgradeScreen;
import grid.CellPosition;

class UpgradeScreenControllerTest {

    private UpgradeScreenController controller;
    private UpgradeScreen mockView;
    private UpgradeScreenModel mockModel;
    private SpaceGame mockGame;

    @BeforeEach
    void setUp() {
        mockView = mock(UpgradeScreen.class);
        mockModel = mock(UpgradeScreenModel.class);
        mockGame = mock(SpaceGame.class);
        GameStateModel mockGameState = mock(GameStateModel.class);

        SoundManager mockSoundManager = mock(SoundManager.class);
        MusicManager mockMusicManager = mock(MusicManager.class);
        when(mockGame.getSoundManager()).thenReturn(mockSoundManager);
        when(mockGame.getMusicManager()).thenReturn(mockMusicManager);
        when(mockGameState.getUpgradeScreenModel()).thenReturn(mockModel);

        controller = new UpgradeScreenController(mockView, mockGameState, mockGame);
    }

    @Test
    void testConstructor_AssignsDependencies() {
        assertNotNull(controller);
    }

    @Test
    void testHandleScroll() {
        when(mockModel.getCurrentZoom()).thenReturn(1.5f);

        controller.handleScroll(2.0f);

        verify(mockModel).updateCameraZoom(2.0f);
        verify(mockView).updateCameraZoom(1.5f);
    }

    @Test
    void testHandleKeyDown_T_TogglesInspection_ON() {

        Gdx.input = mock(Input.class);
        when(Gdx.input.getX()).thenReturn(100);
        when(Gdx.input.getY()).thenReturn(200);

        // Inspection mode initially OFF
        when(mockModel.upgradeInspectionModeIsActive()).thenReturn(false);

        boolean result = controller.handleKeyDown(Input.Keys.T);

        assertTrue(result);
        verify(mockModel).setUpgradeInspectionModeIsActive(anyBoolean());

    }

    @Test
    void testHandleKeyDown_T_TogglesInspection_OFF() {

        Gdx.input = mock(Input.class);
        when(Gdx.input.getX()).thenReturn(100);
        when(Gdx.input.getY()).thenReturn(200);

        // Inspection mode is ON
        when(mockModel.upgradeInspectionModeIsActive()).thenReturn(true);

        boolean result = controller.handleKeyDown(Input.Keys.T);

        assertTrue(result);
        verify(mockModel, times(2)).setUpgradeInspectionModeIsActive(anyBoolean());

    }

    @Test
    void testHandleKeyDown_ESCAPE_ExitsUpgrade() {
        boolean result = controller.handleKeyDown(Input.Keys.ESCAPE);

        assertTrue(result);
        verify(mockModel).exitUpgradeHandler();
        verify(mockGame).setSpaceScreen();
    }

    @Test
    void testHandleKeyDown_U_ExitsUpgrade() {
        boolean result = controller.handleKeyDown(Input.Keys.U);

        assertTrue(result);
        verify(mockModel).exitUpgradeHandler();
        verify(mockGame).setSpaceScreen();
    }

    @Test
    void testHandleKeyDown_OtherKey_ReturnsFalse() {
        boolean result = controller.handleKeyDown(Input.Keys.A);
        assertFalse(result);
    }

    @Test
    void testMouseMoved_UpdatesInspection() {
        boolean result = controller.mouseMoved(100, 200);
        assertTrue(result);

    }

    @Test
    void testConvertMouseToGrid() {
        when(mockModel.getGridOffsetX()).thenReturn(2f);
        when(mockModel.getGridOffsetY()).thenReturn(3f);

        CellPosition result = controller.convertMouseToGrid(5f, 8f);

        assertEquals(5, result.row()); // 8 - 3 = 5
        assertEquals(3, result.col()); // 5 - 2 = 3
    }

    @Test
    void testConvertMouseToUpgradeBar() {
        when(mockModel.getUpgradeOffsetX()).thenReturn(1f);
        when(mockModel.getUpgradeOffsetY()).thenReturn(1f);

        CellPosition result = controller.convertMouseToUpgradeBar(4f, 5f);

        assertEquals(4, result.row()); // 5 - 1 = 4
        assertEquals(3, result.col()); // 4 - 1 = 3
    }

    @Test
    void testCellPositionOnGrid_InsideBounds() {
        when(mockModel.getGridWidth()).thenReturn(10);
        when(mockModel.getGridHeight()).thenReturn(10);

        CellPosition cp = new CellPosition(5, 5);

        assertTrue(controller.cellPositionOnGrid(cp));
    }

    @Test
    void testCellPositionOnGrid_OutsideBounds() {
        when(mockModel.getGridWidth()).thenReturn(5);
        when(mockModel.getGridHeight()).thenReturn(5);

        CellPosition cp = new CellPosition(6, 2);
        assertFalse(controller.cellPositionOnGrid(cp));
    }

    @Test
    void testCellPositionOnUpgradeBar_InsideBounds() {
        when(mockModel.getNumUpgradeOptions()).thenReturn(5);

        CellPosition cp = new CellPosition(0, 3);

        assertTrue(controller.cellPositionOnUpgradeBar(cp));
    }

    @Test
    void testCellPositionOnUpgradeBar_OutsideBounds_RowNotZero() {
        when(mockModel.getNumUpgradeOptions()).thenReturn(5);

        CellPosition cp = new CellPosition(1, 3);

        assertFalse(controller.cellPositionOnUpgradeBar(cp));
    }

    @Test
    void testCellPositionOnUpgradeBar_OutsideBounds_ColNegative() {
        when(mockModel.getNumUpgradeOptions()).thenReturn(5);

        CellPosition cp = new CellPosition(0, -1);

        assertFalse(controller.cellPositionOnUpgradeBar(cp));
    }

    @Test
    void testCellPositionOnUpgradeBar_OutsideBounds_ColTooLarge() {
        when(mockModel.getNumUpgradeOptions()).thenReturn(5);

        CellPosition cp = new CellPosition(0, 6);

        assertFalse(controller.cellPositionOnUpgradeBar(cp));
    }

    @Test
    void testLeftClick_Locked_ReturnsTrueImmediately() {
        controller.setLeftClickLocked(true);

        boolean result = controller.leftClick(100, 200);

        assertTrue(result);
    }

    @Test
    void testLeftClick_ClicksOnUpgradeDisplay() {

        controller.setLeftClickLocked(false);
        when(mockView.getUpgradeStageDisplay()).thenReturn(mock(UpgradeStageDisplay.class));
        when(mockView.getUpgradeStageDisplay().clickedOnUpgradeStageDisplay(anyFloat(), anyFloat()))
                .thenReturn(true);

        boolean result = controller.leftClick(100, 200);

        assertTrue(result);
    }

    @Test
    void testLeftClick_ClickOnFuselageHighlight() {

        controller.setLeftClickLocked(false);

        when(mockView.getUpgradeStageDisplay()).thenReturn(mock(UpgradeStageDisplay.class));
        when(mockView.getUpgradeStageDisplay().clickedOnUpgradeStageDisplay(anyFloat(), anyFloat()))
                .thenReturn(false);
        when(mockModel.getUpgradeHandler()).thenReturn(mock(UpgradeHandler.class));
        when(mockModel.getUpgradeHandler().hasFuselage(any())).thenReturn(true);

        controller.leftClick(100, 200);

        verify(mockModel).disableCellHighlight();

    }

    @Test
    void testLeftClick_ClickOnUpgradeBar() {
        controller.setLeftClickLocked(false);

        when(mockView.getUpgradeStageDisplay()).thenReturn(mock(UpgradeStageDisplay.class));
        when(mockModel.getUpgradeHandler()).thenReturn(mock(UpgradeHandler.class));

        when(mockView.getUpgradeStageDisplay().clickedOnUpgradeStageDisplay(anyFloat(), anyFloat()))
                .thenReturn(false);
        when(mockModel.getUpgradeHandler().hasFuselage(any())).thenReturn(false);

        List<StoreItem<UpgradeType>> shelf = new ArrayList<>(List.of(
                new StoreItem<>(UpgradeType.TURRET, 50, "Turret"),
                new StoreItem<>(UpgradeType.SHIELD, 10, "Shield")));

        when(mockModel.getStoreShelf()).thenReturn(shelf);
        when(mockModel.getPlayerResources()).thenReturn(1000);
        when(mockModel.getNumUpgradeOptions()).thenReturn(2);

        boolean result = controller.leftClick(1, 0);

        assertTrue(result);

    }

    @Test
    void testRightClick_Locked_ReturnsTrueImmediately() {
        controller.setRightClickLocked(true);

        boolean result = controller.rightClick(100, 200);

        assertTrue(result);
    }

    @Test
    void testRightClick_UpdatesDragAndLocks() {
        controller.setRightClickLocked(false);

        boolean result = controller.rightClick(100, 200);

        assertTrue(result);
        verify(mockModel).updateDragPosition(100, 200);
    }

    @Test
    void testMiddleClick_ResetsZoom() {
        boolean result = controller.middleClick();

        assertTrue(result);
        verify(mockModel).updateCameraZoom(0f);
    }

    @Test
    void testLeftClickDragged_UpdatesDrag() {
        boolean result = controller.leftClickDragged(150, 250);

        assertTrue(result);
        verify(mockModel).updateDragPosition(150, 250);
    }

    @Test
    void testRightClickDragged_UpdatesDragAndCamera() {
        Vector2 mockDelta = new Vector2(5f, -3f);
        when(mockModel.getDragDelta()).thenReturn(mockDelta);

        boolean result = controller.rightClickDragged(200, 300);

        assertTrue(result);
        verify(mockModel).updateDragPosition(200, 300);
        verify(mockView).updateCameraPosition(5, -3);
    }

    @Test
    void testLeftClickRelease_WhenLocked() {
        controller.setLeftClickLocked(true);

        boolean result = controller.leftClickRelease();

        assertTrue(result);
    }

    @Test
    void testLeftClickRelease_Normal() {
        controller.setLeftClickLocked(false);

        boolean result = controller.leftClickRelease();

        assertTrue(result);
        verify(mockModel).setReleaseGrabbedUpgrade(true);
    }

    @Test
    void testRightClickRelease_WhenLocked() {
        controller.setRightClickLocked(true);

        boolean result = controller.rightClickRelease();

        assertTrue(result);
    }

    @Test
    void testRightClickRelease_Normal() {
        controller.setRightClickLocked(false);

        boolean result = controller.rightClickRelease();

        assertTrue(result);
    }

    @Test
    void testUpdate() {
        controller.update(1);
        verify(mockModel).update(1);
    }

    @Test
    void testReset() {

        UpgradeHandler mockUpgradeHandler = mock(UpgradeHandler.class);
        when(mockModel.getUpgradeHandler()).thenReturn(mockUpgradeHandler);
        controller.reset();
        verify(mockUpgradeHandler).expand();
    }

    @Test
    void testUpdateInspectionMode() {
        Gdx.input = mock(Input.class);
        when(Gdx.input.getX()).thenReturn(1);
        when(Gdx.input.getY()).thenReturn(0);
        when(mockModel.getNumUpgradeOptions()).thenReturn(2);
        when(mockModel.upgradeInspectionModeIsActive()).thenReturn(true);

        boolean result = controller.handleKeyDown(Input.Keys.T);

        assertTrue(result);
        verify(mockModel).setInspectedUpgradeIndex(1);
        verify(mockModel).setUpgradeInspectionModeIsActive(true);
    }
}
