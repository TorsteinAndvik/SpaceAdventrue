package controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import model.GameStateModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.Screen;
import view.SpaceGame;
import controller.audio.MusicManager;
import controller.audio.SoundManager;

public class GenericControllerTest {

    private TestController controller;

    @BeforeEach
    void setUp() {
        GameStateModel model = mock(GameStateModel.class);
        Screen screen = mock(Screen.class);
        SpaceGame mockGame = mock(SpaceGame.class);

        MusicManager musicManager = mock(MusicManager.class);
        SoundManager soundManager = mock(SoundManager.class);

        when(mockGame.getMusicManager()).thenReturn(musicManager);
        when(mockGame.getSoundManager()).thenReturn(soundManager);

        controller = new TestController(screen, model, mockGame);
    }

    @Test
    void testTouchDown_LeftClick() {
        assertTrue(controller.touchDown(10, 10, 0, 0));
        assertTrue(controller.leftClicked);
    }

    @Test
    void testTouchDown_RightClick() {
        assertTrue(controller.touchDown(20, 20, 0, 1));
        assertTrue(controller.rightClicked);
    }

    @Test
    void testTouchDown_MiddleClick() {
        assertTrue(controller.touchDown(30, 30, 0, 2));
        assertTrue(controller.middleClicked);
    }

    @Test
    void testTouchDown_InvalidButton() {
        assertFalse(controller.touchDown(40, 40, 0, 3));
    }


    @Test
    void testTouchUp_LeftClickRelease() {
        assertTrue(controller.touchUp(0, 0, 0, 0));
        assertTrue(controller.leftReleased);
    }

    @Test
    void testTouchUp_RightClickRelease() {
        assertTrue(controller.touchUp(0, 0, 0, 1));
        assertTrue(controller.rightReleased);
    }

    @Test
    void testTouchUp_InvalidButton() {
        assertFalse(controller.touchUp(0, 0, 0, 2));
    }

    @Test
    void testScroll_Handled() {
        assertTrue(controller.scrolled(0, 1));
        assertTrue(controller.scrolledHandled);
    }

    @Test
    void testClickLocks() {
        controller.setLeftClickLocked(true);
        controller.setRightClickLocked(true);
        assertTrue(controller.isLeftClickLocked());
        assertTrue(controller.isRightClickLocked());
    }

    @Test
    void testTouchDragged_LeftClickLocked() {
        controller.setLeftClickLocked(true);
        assertTrue(controller.touchDragged(5, 5, 0));
    }

    @Test
    void testTouchDragged_RightClickLocked() {
        controller.setLeftClickLocked(false);
        controller.setRightClickLocked(true);
        assertTrue(controller.touchDragged(5, 5, 0));
    }

    @Test
    void testTouchDragged_NoLock() {
        controller.setLeftClickLocked(false);
        controller.setRightClickLocked(false);
        assertFalse(controller.touchDragged(5, 5, 0));
    }


    @Test
    void testKeyDown() {
        assertFalse(controller.keyDown(13));
    }

    @Test
    void testKeyUp() {
        assertFalse(controller.keyUp(11));
    }

    public static class TestController extends GenericController {

        public boolean leftClicked = false;
        public boolean rightClicked = false;
        public boolean middleClicked = false;
        public boolean leftReleased = false;
        public boolean rightReleased = false;
        public boolean scrolledHandled = false;

        public TestController(Screen view, GameStateModel model, SpaceGame game) {
            super(view, model, game);
        }

        @Override
        public void update(float delta) { }

        @Override
        protected boolean leftClick(int screenX, int screenY) {
            leftClicked = true;
            return true;
        }

        @Override
        protected boolean rightClick(int screenX, int screenY) {
            rightClicked = true;
            return true;
        }

        @Override
        protected boolean middleClick() {
            middleClicked = true;
            return true;
        }

        @Override
        protected boolean leftClickDragged(int screenX, int screenY) {
            return true;
        }

        @Override
        protected boolean rightClickDragged(int screenX, int screenY) {
            return true;
        }

        @Override
        protected boolean leftClickRelease() {
            leftReleased = true;
            return true;
        }

        @Override
        protected boolean rightClickRelease() {
            rightReleased = true;
            return true;
        }

        @Override
        protected void handleScroll(float amountY) {
            scrolledHandled = true;
        }

    }

}


