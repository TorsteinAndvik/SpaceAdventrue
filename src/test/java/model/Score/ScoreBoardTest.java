package model.Score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ScoreBoardTest {

    private ScoreBoard scoreBoard;
    private FileHandle fileHandle;
    private final int maxEntries = 5;

    private final BasicScoreFormula basicScoreFormula = new BasicScoreFormula();


    @BeforeEach
    void setup() {

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

        scoreBoard = new ScoreBoard(maxEntries, fileHandle, basicScoreFormula);
    }

    @Test
    void testReadEmptyScoreBoard() {
        assertEquals("[]", scoreBoard.getHighScores().toString());
    }


    @Test
    void testReadScoreBoard() {
        GameStats gs = new GameStats(1, 0, 0, 0);
        int score = basicScoreFormula.calculateScore(gs);

        assertNotEquals("[ScoreEntry[name=PlayerName, score=" + score + "]]", scoreBoard.getHighScores().toString());
        scoreBoard.submitScore(new DummyNameProvider(), gs);
        assertEquals("[ScoreEntry[name=PlayerName, score=" + score + "]]", scoreBoard.getHighScores().toString());

    }

    @Test
    void testAddMoreScoresThanCanHold() {
        String name = "PlayerName";
        int firstScore = basicScoreFormula.calculateScore(new GameStats(3, 0, 0, 0));

        for (int x = 0; x <= maxEntries; x++) {

            scoreBoard.submitScore(new DummyNameProvider(), new GameStats(3, x, 0, 0));

            if (x == maxEntries) {
                assertFalse(scoreBoard.getHighScores().contains(new ScoreEntry(name, firstScore)));
            } else {
                assertTrue(scoreBoard.getHighScores().contains(new ScoreEntry(name, firstScore)));
            }
        }
    }

    @Test
    void testEnsureFileExists_Exists() {
        FileHandle parentHandle = mock(FileHandle.class);

        when(fileHandle.parent()).thenReturn(parentHandle);
        when(parentHandle.exists()).thenReturn(true);
        when(fileHandle.exists()).thenReturn(true);

        scoreBoard = new ScoreBoard(maxEntries, fileHandle, basicScoreFormula);

        verify(parentHandle, never()).mkdirs();
        verify(fileHandle, never()).writeString("[]", false);
    }

    @Test
    void testEnsureFileExist_NotExists() {
        FileHandle parentHandle = mock(FileHandle.class);

        when(fileHandle.parent()).thenReturn(parentHandle);
        when(parentHandle.exists()).thenReturn(false);
        when(fileHandle.exists()).thenReturn(false);

        scoreBoard = new ScoreBoard(maxEntries, fileHandle, basicScoreFormula);

        verify(parentHandle, times(1)).mkdirs();
        verify(fileHandle, times(1)).writeString("[]", false);

    }

    @Test
    void testGetScore() {
        GameStats gs = new GameStats(1, 2, 3, 4);
        int score = basicScoreFormula.calculateScore(gs);
        assertEquals(score, scoreBoard.getScore(gs));
    }

    @Test
    void testGetScoreFormula() {
        assertEquals(basicScoreFormula, scoreBoard.getScoreFormula());
    }

    public static class DummyNameProvider implements NameProvider {

        @Override
        public String getPlayerName() {
            return "PlayerName";
        }
    }

    @Test
    public void testConstructorMaxEntries_UsesDefaultFile() {
        ScoreBoard board = new ScoreBoard(4, basicScoreFormula);
        assertEquals(basicScoreFormula, board.getScoreFormula());
        assertTrue(board.getHighScores().isEmpty());
    }

    @Test
    public void testConstructorNoParams() {
        ScoreBoard board = new ScoreBoard();
        assertNull(board.getScoreFormula());
        assertTrue(board.getHighScores().isEmpty());
    }
}
