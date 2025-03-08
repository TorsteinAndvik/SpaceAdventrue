package inf112.skeleton.model.ShipComponents;

public enum UpgradeStage {
    // TODO: Add more stages (ONE, TWO, ...) as more are implemented in the game
    // TODO: Consider if there should be a unique UpgradeStage-esque enum for each
    // type of upgrade, in case different upgrade types have a different number of
    // total stages

    ZERO, // base stage
    MAX; // final upgrade stage

    public UpgradeStage nextStage() {
        return nextStage(this);
    }

    public UpgradeStage nextStage(UpgradeStage stage) {
        return switch (stage) {
            case ZERO -> UpgradeStage.MAX;
            case MAX -> UpgradeStage.MAX;
        };
    }

    public boolean isUpgradeable() {
        return this != UpgradeStage.MAX;
    }

    // Being able to display the number of upgrade stages available could be a nice
    // detail for the view
    public int numberOfUpgradeStages() {
        return UpgradeStage.values().length;
    }
}
