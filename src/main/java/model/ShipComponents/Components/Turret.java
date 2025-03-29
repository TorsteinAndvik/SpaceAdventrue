package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.utils.FloatPair;

public class Turret extends ShipUpgrade {

    public Turret() {
        this(UpgradeStage.ZERO);
    }

    public Turret(UpgradeStage stage) {
        super("Turret", "Fires lasers at enemies and asteroids", UpgradeType.TURRET, stage);
    }

    public static FloatPair turretBarrelLocation() {
        return new FloatPair(0.5f, 0.875f);
    }
}
