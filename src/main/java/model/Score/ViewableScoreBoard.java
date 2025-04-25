package model.Score;

import java.util.List;

public interface ViewableScoreBoard {

    /**
     * Returns the list of top high scores recorded.
     *
     * @return a list of {@link ScoreEntry} objects representing the high scores
     */
    List<ScoreEntry> getHighScores();

    /**
     * Computes the score for the given game statistics.
     *
     * @param gameStats the {@link GameStats} used to calculate the score
     * @return the computed score as an integer
     */
    int getScore(GameStats gameStats);

    /**
     * Returns the scoring formula used by this scoreboard.
     *
     * @return the {@link ScoreFormula} currently in use
     */
    ScoreFormula getScoreFormula();
}
