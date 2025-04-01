package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.utils.FloatPair;

public class Thruster extends ShipUpgrade {

    public Thruster() {
        this(UpgradeStage.ZERO);
    }

    public Thruster(UpgradeStage stage) {
        super("Thruster", "Increases the speed of the ship", UpgradeType.THRUSTER, stage);
    }

    /**
     * @return a <code>FloatPair</code> giving the x / y offset between the
     *         centerpoint of <code>this</code> and the exhaust (location of flame).
     */
    public static FloatPair thrusterFlameLocation() {
        return new FloatPair(0f, -0.15f);
    }
}
