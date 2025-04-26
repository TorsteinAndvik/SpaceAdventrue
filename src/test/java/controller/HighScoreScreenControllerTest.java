package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import controller.audio.MusicManager;
import controller.audio.SoundEffect;
import controller.audio.SoundManager;
import model.GameStateModel;
import model.utils.MenuButton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.SpaceGame;
import view.screens.HighScoreScreen;


class HighScoreScreenControllerTest {

    private HighScoreScreen mockView;
    private GameStateModel mockModel;
    private SoundManager mockSoundManager;
    private HighScoreScreenController controller;
    private MenuButton mockBackButton;

    @BeforeEach
    void setUp() {

        mockView = mock(HighScoreScreen.class);
        mockModel = mock(GameStateModel.class);
        SpaceGame mockGame = mock(SpaceGame.class);
        mockSoundManager = mock(SoundManager.class);
        mockBackButton = mock(MenuButton.class);
        MusicManager mockMusicManager = mock(MusicManager.class);

        when(mockGame.getSoundManager()).thenReturn(mockSoundManager);
        when(mockGame.getMusicManager()).thenReturn(mockMusicManager);
        when(mockView.getBackButton()).thenReturn(mockBackButton);

        controller = new HighScoreScreenController(mockView, mockModel, mockGame);
    }


    @Test
    void testHandleKeyDown_EnterSpaceAndEscape() {
        when(mockModel.getSelectedButtonIndex()).thenReturn(0);
        for (int key : new int[]{ Keys.ENTER, Keys.SPACE, Keys.ESCAPE }) {
            boolean handled = controller.handleKeyDown(key);
            assertTrue(handled);
        }
        verify(mockSoundManager, times(3)).play(SoundEffect.MENU_SELECT, 0.2f);

    }

    @Test
    void testTouchDown() {

        Vector3 mockWorldCoords = new Vector3(100, 200, 0);
        Rectangle mockBounds = new Rectangle(90, 190, 20, 20);

        when(mockView.unprojectScreenCoords(anyInt(), anyInt())).thenReturn(mockWorldCoords);
        when(mockBackButton.getBounds()).thenReturn(mockBounds);

        boolean result = controller.touchDown(100, 200, 0, 0);

        assertTrue(result);

        verify(mockModel).setSelectedButtonIndex(0);
    }

    @Test
    void testTouchDown_OutsideButton() {
        Vector3 mockWorldCoords = new Vector3(100, 200, 0);
        Rectangle mockBounds = new Rectangle(30, 120, 20, 20);

        when(mockView.unprojectScreenCoords(anyInt(), anyInt())).thenReturn(mockWorldCoords);
        when(mockBackButton.getBounds()).thenReturn(mockBounds);

        boolean result = controller.touchDown(100, 200, 0, 0);
        assertFalse(result);
    }

    @Test
    void testUpdate() {
        controller.update(0);
        verifyNoInteractions(mockModel);
    }

    @Test
    void testClickLocks() {
        controller.setLeftClickLocked(true);
        controller.setRightClickLocked(true);
        assertTrue(controller.isLeftClickLocked());
        assertTrue(controller.isRightClickLocked());
    }

    @Test
    void testKeyDown_DelegatesToHandleKeyDown() {
        assertFalse(controller.keyDown(42));
        assertTrue(controller.keyDown(Keys.ESCAPE));
    }

    @Test
    void testMouseMovedOverBackButton() {
        Vector3 mockWorldCoords = new Vector3(100, 200, 0);
        Rectangle mockBounds = new Rectangle(90, 190, 20, 20);

        when(mockView.unprojectScreenCoords(anyInt(), anyInt())).thenReturn(mockWorldCoords);
        when(mockBackButton.getBounds()).thenReturn(mockBounds);
        when(mockModel.getSelectedButtonIndex()).thenReturn(-1);

        boolean result = controller.mouseMoved(100, 200);

        assertTrue(result);

        verify(mockModel).getSelectedButtonIndex();
        verify(mockSoundManager).play(SoundEffect.MENU_SELECT, 0.2f);
    }

    @Test
    void testMouseMovedOverNoButtons() {
        Vector3 mockWorldCoords = new Vector3(100, 200, 0);
        Rectangle mockBounds = new Rectangle(90, 190, 20, 20);

        when(mockView.unprojectScreenCoords(anyInt(), anyInt())).thenReturn(mockWorldCoords);
        when(mockBackButton.getBounds()).thenReturn(mockBounds);

        boolean result = controller.mouseMoved(20, 20);
        assertTrue(result);
    }

    @Test
    void testMouseMovedButtonOutsideScreen() {
        Vector3 mockWorldCoords = new Vector3(100, 200, 0);
        Rectangle mockBounds = new Rectangle(900, 990, 20, 20);

        when(mockView.unprojectScreenCoords(anyInt(), anyInt())).thenReturn(mockWorldCoords);
        when(mockBackButton.getBounds()).thenReturn(mockBounds);

        boolean result = controller.mouseMoved(50, 68);
        assertFalse(result);
    }

    @Test
    void testClickDragRelease() {
        Vector3 mockWorldCoords = new Vector3(100, 200, 0);
        Rectangle mockBounds = new Rectangle(90, 190, 20, 20);

        when(mockView.unprojectScreenCoords(anyInt(), anyInt())).thenReturn(mockWorldCoords);
        when(mockBackButton.getBounds()).thenReturn(mockBounds);

        assertTrue(controller.leftClick(95, 200));
        assertFalse(controller.leftClickDragged(0, 0));
        assertFalse(controller.leftClickRelease());

        assertFalse(controller.rightClick(1, 1));
        assertFalse(controller.rightClickDragged(0, 0));
        assertFalse(controller.rightClickRelease());

        assertFalse(controller.middleClick());
    }

    @Test
    void testHandleScroll() {
        controller.handleScroll(10);
        verifyNoInteractions(mockModel);
    }
}
