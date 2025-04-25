package model.Score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ScoreFormulaTest {

    @Test
    void testFormulaStart() {
        ScoreFormula formula = new BasicScoreFormula();
        int score = formula.calculateScore(new GameStats(0, 0, 0, 0));
        assertEquals(0, score);
    }

    @Test
    void testFormulaScoreIncrease() {
        ScoreFormula formula = new BasicScoreFormula();
        int prevScore = 0;
        for (int x = 1; x < 10; x++) {
            int newScore = formula.calculateScore(new GameStats(x, x, x, x));
            assertTrue(prevScore < newScore);
            prevScore = newScore;
        }

    }


}
