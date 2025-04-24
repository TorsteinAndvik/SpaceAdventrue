package model.Score;

public interface ScoreFormula {

    /**
     * Calculates a score based on GameStats
     *
     * @param stats about the game
     * @return the calculated score
     */
    int calculateScore(GameStats stats);


}


