package model;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import model.constants.GameState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class GameStateModelTest {

    private GameStateModel gameStateModel;

    private static MockedStatic<Gdx> gdxStaticMock;
    private FileHandle fileHandle;

    @BeforeAll
    static void setupGdx() {
        gdxStaticMock = mockStatic(Gdx.class);
        Gdx.files = mock(Files.class);
    }

    @AfterAll
    static void tearDownGdx() {
        gdxStaticMock.close();
    }

    @BeforeEach
    void setUp() {
        gameStateModel = new GameStateModel();

        fileHandle = mock(FileHandle.class);
        FileHandle parentHandle = mock(FileHandle.class);
        when(fileHandle.parent()).thenReturn(parentHandle);
        when(parentHandle.exists()).thenReturn(true);
        when(fileHandle.exists()).thenReturn(true);

        when(fileHandle.parent()).thenReturn(parentHandle);
        when(parentHandle.exists()).thenReturn(true);
        when(fileHandle.exists()).thenReturn(true);

        Files mockFiles = mock(Files.class);
        when(mockFiles.local(anyString())).thenReturn(fileHandle);
        Gdx.files = mockFiles;
    }

    @Test
    void testInitialStateIsLoading() {
        assertEquals(GameState.LOADING, gameStateModel.getCurrentState(), "Initial state should be LOADING");
    }

    @Test
    void testChangeStateUpdatesStateAndPreviousState() {
        gameStateModel.changeState(GameState.START_GAME);
        assertEquals(GameState.START_GAME, gameStateModel.getCurrentState());
        assertEquals(GameState.LOADING, gameStateModel.getPreviousState());
    }

    @Test
    void testChangeToSameStateDoesNothing() {
        gameStateModel.changeState(GameState.LOADING);
        assertNull(gameStateModel.getPreviousState(), "Previous state should remain null if no actual change occurs");
    }

    @Test
    void testOnAssetsLoadedChangesToStartGame() {
        gameStateModel.onAssetsLoaded();
        assertEquals(GameState.START_GAME, gameStateModel.getCurrentState());
        assertEquals(GameState.LOADING, gameStateModel.getPreviousState());
    }

    @Test
    void testStartNewGameInitializesSpaceGameModelAndSetsState() {
        assertNull(gameStateModel.getSpaceGameModel(), "Should initially be null");

        gameStateModel.startNewGame();

        assertNotNull(gameStateModel.getSpaceGameModel(), "Should initialize SpaceGameModel");
        assertNull(gameStateModel.getUpgradeScreenModel(), "UpgradeScreenModel should be reset");
        assertEquals(GameState.PLAYING, gameStateModel.getCurrentState());
    }

    @Test
    void testShowUpgradeScreenInitializesUpgradeScreenModel() {
        gameStateModel.startNewGame(); // must start game to have a spaceGameModel
        gameStateModel.showUpgradeScreen();

        assertEquals(GameState.UPGRADE, gameStateModel.getCurrentState());
        assertNotNull(gameStateModel.getUpgradeScreenModel(), "UpgradeScreenModel should be initialized");
    }

    @Test
    void testSelectedButtonIndexGetterSetter() {
        gameStateModel.setSelectedButtonIndex(2);
        assertEquals(2, gameStateModel.getSelectedButtonIndex());
    }

    @Test
    void testSelectedControlCategoryIndexGetterSetter() {
        gameStateModel.setSelectedControlCategoryIndex(3);
        assertEquals(3, gameStateModel.getSelectedControlCategoryIndex());
    }


    @Test
    void testHasActiveGameTrueWhenPlayerPresent() {
        assertFalse(gameStateModel.hasActiveGame(), "Should be false with no SpaceGameModel or player");
        gameStateModel.startNewGame();
        assertNotNull(gameStateModel.getSpaceGameModel().getPlayer(), "Player should be initialized");
        assertTrue(gameStateModel.hasActiveGame(), "Should be true with player present");
    }

    @Test
    void testNewSpaceGame_PlayingAndSpaceGameModelNull() {
        assertNull(gameStateModel.getSpaceGameModel());

        gameStateModel.changeState(GameState.PLAYING);
        gameStateModel.startNewGame();

        assertNotNull(gameStateModel.getSpaceGameModel());
        assertSame(GameState.PLAYING, gameStateModel.getCurrentState());

    }
}
