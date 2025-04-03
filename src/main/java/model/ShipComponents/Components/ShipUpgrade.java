package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.constants.PhysicsParameters;

public abstract class ShipUpgrade {

    private final String name;
    private final String description;

    private final UpgradeType type;
    private UpgradeStage stage;

    private float mass = PhysicsParameters.shipUpgradeMass;

    public ShipUpgrade(String name, String description, UpgradeType type, UpgradeStage stage) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.stage = stage;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public UpgradeType getType() {
        return type;
    }

    public UpgradeStage getStage() {
        return stage;
    }

    public float getMass() {
        return mass;
    }

    /**
     * Upgrade <code>this</code> to the next stage if possible
     *
     * @return true if <code>this</code> was upgraded, false otherwise
     */
    public boolean upgrade() {
        if (stage.isUpgradeable()) {
            stage = stage.nextStage(stage);
            return true;
        } else {
            return false;
        }
    }

    public static ShipUpgrade getShipUpgrade(UpgradeType upgradeType) {
        return switch (upgradeType) {
            case THRUSTER -> new Thruster();
            case TURRET -> new Turret();
            case SHIELD -> new Shield();
        };
    }

    /**
     * The resources to loot from this {@code ShipUpgrade}
     *
     * @return the amount of resources to loot.
     */
    public int getResourceValue() {
        int value = switch (type) {
            case THRUSTER -> Thruster.RESOURCE_BASE_VALUE;
            case TURRET -> Turret.RESOURCE_BASE_VALUE;
            case SHIELD -> Shield.RESOURCE_BASE_VALUE;
        };

        return value * (stage.ordinal() + 1);
    }
}
