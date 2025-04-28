package controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Input;
import controller.audio.MusicManager;
import controller.audio.SoundEffect;
import controller.audio.SoundManager;
import model.GameStateModel;
import model.SpaceGameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.SpaceGame;

class SpaceScreenControllerTest {

    private SpaceGameModel spaceGameModel;

    private SpaceGame game;
    private SpaceScreenController controller;

    @BeforeEach
    void setUp() {
        GameStateModel gameStateModel = mock(GameStateModel.class);
        MusicManager musicManager = mock(MusicManager.class);
        SoundManager soundManager = mock(SoundManager.class);
        spaceGameModel = mock(SpaceGameModel.class);
        game = mock(SpaceGame.class);

        when(gameStateModel.getSpaceGameModel()).thenReturn(spaceGameModel);
        when(game.getMusicManager()).thenReturn(musicManager);
        when(game.getSoundManager()).thenReturn(soundManager);

        controller = new SpaceScreenController(null, gameStateModel, game);
    }

    @Test
    void testHandleKeyDown_W() {
        assertTrue(controller.handleKeyDown(Input.Keys.W));
        verify(spaceGameModel).setAccelerateForward(true);
        verify(spaceGameModel).setAccelerateBackward(false);
    }

    @Test
    void testHandleKeyDown_S() {
        assertTrue(controller.handleKeyDown(Input.Keys.S));
        verify(spaceGameModel).setAccelerateBackward(true);
        verify(spaceGameModel).setAccelerateForward(false);
    }

    @Test
    void testHandleKeyDown_A() {
        assertTrue(controller.handleKeyDown(Input.Keys.A));
        verify(spaceGameModel).setAccelerateCounterClockwise(true);
        verify(spaceGameModel).setAccelerateClockwise(false);
    }

    @Test
    void testHandleKeyDown_D() {
        assertTrue(controller.handleKeyDown(Input.Keys.D));
        verify(spaceGameModel).setAccelerateClockwise(true);
        verify(spaceGameModel).setAccelerateCounterClockwise(false);
    }

    @Test
    void testHandleKeyDown_SPACE() {
        assertTrue(controller.handleKeyDown(Input.Keys.SPACE));
        verify(spaceGameModel).playerShoot();
    }

    @Test
    void testHandleKeyDown_U() {
        assertTrue(controller.handleKeyDown(Input.Keys.U));
        verify(game).setUpgradeScreen();
    }

    @Test
    void testHandleKeyDown_ESCAPE() {
        assertTrue(controller.handleKeyDown(Input.Keys.ESCAPE));
        verify(game).setStartScreen();
    }

    @Test
    void testHandleKeyDown_P() {
        assertTrue(controller.handleKeyDown(Input.Keys.P));
        verify(game).setOptionsScreen();
    }

    @Test
    void testHandleKeyDown_unknownKey() {
        assertFalse(controller.handleKeyDown(Input.Keys.F1));
    }

    @Test
    void testHandleKeyUp_W() {
        assertTrue(controller.handleKeyUp(Input.Keys.W));
        verify(spaceGameModel).setAccelerateForward(false);
    }

    @Test
    void testHandleKeyUp_S() {
        assertTrue(controller.handleKeyUp(Input.Keys.S));
        verify(spaceGameModel).setAccelerateBackward(false);
    }

    @Test
    void testHandleKeyUp_A() {
        assertTrue(controller.handleKeyUp(Input.Keys.A));
        verify(spaceGameModel).setAccelerateCounterClockwise(false);
    }

    @Test
    void testHandleKeyUp_D() {
        assertTrue(controller.handleKeyUp(Input.Keys.D));
        verify(spaceGameModel).setAccelerateClockwise(false);
    }

    @Test
    void testHandleKeyUp_unknownKey() {
        assertFalse(controller.handleKeyUp(Input.Keys.F2));
    }


    @Test
    void testReset() {
        controller.reset();
        verify(spaceGameModel).setAccelerateForward(false);
        verify(spaceGameModel).setAccelerateBackward(false);
        verify(spaceGameModel).setAccelerateClockwise(false);
        verify(spaceGameModel).setAccelerateCounterClockwise(false);
    }

    @Test
    void testPlaySoundEffect() {
        SoundEffect effect = mock(SoundEffect.class);
        controller.play(effect);
        verify(controller.soundManager).play(effect);
    }

    @Test
    void testClickDragRelease() {

        assertFalse(controller.leftClick(1, 1));
        assertFalse(controller.leftClickDragged(1, 1));
        assertFalse(controller.leftClickRelease());

        assertFalse(controller.rightClick(1, 1));
        assertFalse(controller.rightClickDragged(0, 0));
        assertFalse(controller.rightClickRelease());

        assertFalse(controller.middleClick());
    }

    @Test
    void testHandleScroll_DoesNothing() {
        assertDoesNotThrow(() -> controller.handleScroll(5.0f));
        assertDoesNotThrow(() -> controller.handleScroll(-3.0f));
        assertDoesNotThrow(() -> controller.handleScroll(0f));
    }

    @Test
    void testUpdate() {
        controller.update(1);
        verify(spaceGameModel).update(1);
    }
}