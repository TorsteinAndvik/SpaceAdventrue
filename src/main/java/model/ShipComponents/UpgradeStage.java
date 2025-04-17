package model.ShipComponents;

public enum UpgradeStage {

    ZERO, // base stage
    ONE,
    TWO,
    THREE,
    FOUR,
    MAX; // final upgrade stage

    public UpgradeStage nextStage() {
        return nextStage(this);
    }

    public UpgradeStage nextStage(UpgradeStage stage) {
        return switch (stage) {
            case ZERO -> UpgradeStage.ONE;
            case ONE -> UpgradeStage.TWO;
            case TWO -> UpgradeStage.THREE;
            case THREE -> UpgradeStage.FOUR;
            case FOUR -> UpgradeStage.MAX;
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
