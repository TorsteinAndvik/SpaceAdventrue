package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.stats.Stat;
import model.constants.PhysicsParameters;
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

    @Override
    protected void setupStatModifiers() {
        statModifier.setModifier(Stat.MASS, PhysicsParameters.shipUpgradeMass);
        statModifier.setModifier(Stat.ACCELERATION_FORCE, 12f);
        statModifier.setModifier(Stat.MAX_SPEED, 3f);
        statModifier.setModifier(Stat.RESOURCE_VALUE, 3);

        // Upgrades:
        upgradeModifier.setModifier(Stat.ACCELERATION_FORCE, 4f);
        upgradeModifier.setModifier(Stat.MAX_SPEED, 1f);
        upgradeModifier.setModifier(Stat.RESOURCE_VALUE, 1);
    }
}
