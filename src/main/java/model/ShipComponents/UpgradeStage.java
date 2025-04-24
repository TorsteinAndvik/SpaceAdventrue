package model.ShipComponents;

public enum UpgradeStage {

    ZERO,
    ONE,
    TWO,
    THREE,
    MAX;

    public UpgradeStage nextStage() {
        return nextStage(this);
    }

    public UpgradeStage nextStage(UpgradeStage stage) {
        return switch (stage) {
            case ZERO -> UpgradeStage.ONE;
            case ONE -> UpgradeStage.TWO;
            case TWO -> UpgradeStage.THREE;
            case THREE -> UpgradeStage.MAX;
            case MAX -> UpgradeStage.MAX;
        };
    }

    public boolean isUpgradeable() {
        return this != UpgradeStage.MAX;
    }
}
