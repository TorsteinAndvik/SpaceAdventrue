package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;

public class Turret extends ShipUpgrade {

    public Turret() {
        this(UpgradeStage.ZERO);
    }

    public Turret(UpgradeStage stage) {
        super("Turret", "Fires lasers at enemies and asteroids", UpgradeType.TURRET, stage);
    }
}
