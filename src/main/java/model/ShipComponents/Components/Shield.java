package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;

public class Shield extends ShipUpgrade {

    public static int RESOURCE_BASE_VALUE = 7;

    public Shield() {
        this(UpgradeStage.ZERO);
    }

    public Shield(UpgradeStage stage) {
        super("Shield", "Reduces damage taken from enemies and collisions", UpgradeType.SHIELD,
            stage);
    }
}
