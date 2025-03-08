package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.model.ShipComponents.UpgradeStage;
import inf112.skeleton.model.ShipComponents.UpgradeType;

public class Shield extends ShipUpgrade {
    public Shield() {
        this(UpgradeStage.ZERO);
    }

    public Shield(UpgradeStage stage) {
        super("Shield", "Reduces damage taken from enemies and collisions", UpgradeType.SHIELD, stage);
    }
}
