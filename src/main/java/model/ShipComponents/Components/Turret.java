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

    /**
     * @return a <code>FloatPair</code> giving the x / y offset between the
     *         centerpoint of <code>this</code> and the tip of the turret's barrel.
     */
    public static FloatPair turretBarrelLocation() {
        return new FloatPair(0f, 0.375f);
    }
}
