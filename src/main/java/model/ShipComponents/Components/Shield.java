package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.stats.Stat;
import model.constants.PhysicsParameters;

public class Shield extends ShipUpgrade {

    public Shield() {
        this(UpgradeStage.ZERO);
    }

    public Shield(UpgradeStage stage) {
        super("Shield", "Reduces damage taken from enemies and collisions", UpgradeType.SHIELD,
                stage);
    }

    @Override
    protected void setupStatModifiers() {
        statModifier.setModifier(Stat.MASS, PhysicsParameters.shipUpgradeMass);
        statModifier.setModifier(Stat.HEALTH_VALUE, 5);
        statModifier.setModifier(Stat.RESOURCE_VALUE, 7);

        // 1/x -> 1 hitpoint restored every x seconds.
        statModifier.setModifier(Stat.HEALTH_REGENERATION_RATE, 1f / 8f);

        // Upgrades:
        upgradeModifier.setModifier(Stat.HEALTH_VALUE, 1);
        upgradeModifier.setModifier(Stat.RESOURCE_VALUE, 3);
        upgradeModifier.setModifier(Stat.HEALTH_REGENERATION_RATE, 1f / 12f);
    }
}
