package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;

public class Thruster extends ShipUpgrade {

    public Thruster() {
        this(UpgradeStage.ZERO);
    }

    public Thruster(UpgradeStage stage) {
        super("Thruster", "Increases the speed of the ship", UpgradeType.THRUSTER, stage);
    }
}
