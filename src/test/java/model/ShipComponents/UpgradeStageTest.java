package model.ShipComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UpgradeStageTest {

    @Test
    public void nextStageTest() {
        UpgradeStage stage = UpgradeStage.ZERO;
        assertEquals(UpgradeStage.ONE, stage.nextStage());
        assertEquals(UpgradeStage.TWO, stage.nextStage().nextStage());
        assertEquals(UpgradeStage.THREE, stage.nextStage().nextStage().nextStage());
        assertEquals(UpgradeStage.MAX, stage.nextStage().nextStage().nextStage().nextStage(),
                "Error: Did you add more stages to UpgradeStage?");
        assertEquals(UpgradeStage.MAX, stage.nextStage().nextStage().nextStage().nextStage().nextStage(),
                "Error: Did you add more stages to UpgradeStage?");
    }

    @Test
    public void upgradeableTest() {
        for (UpgradeStage stage : UpgradeStage.values()) {
            assertEquals(stage.isUpgradeable(), stage != UpgradeStage.MAX);
        }
    }
}
