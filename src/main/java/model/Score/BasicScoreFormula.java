package model.Score;

public class BasicScoreFormula implements ScoreFormula {

    @Override
    public int calculateScore(GameStats stats) {
        return stats.objectsDestroyed() * 100
                + stats.shipValue() * 10
                + (int) stats.timeSurvived() * 2
                + stats.resourcesLeft();
    }
}