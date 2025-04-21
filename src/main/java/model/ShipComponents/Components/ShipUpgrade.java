package model.ShipComponents.Components;

import java.util.HashMap;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.stats.Stat;
import model.ShipComponents.Components.stats.StatModifier;

public abstract class ShipUpgrade {

    protected final String name;
    protected final String description;
    protected final UpgradeType type;
    protected UpgradeStage stage;
    protected final StatModifier statModifier;
    protected final StatModifier upgradeModifier;

    public ShipUpgrade(String name, String description, UpgradeType type, UpgradeStage stage) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.stage = stage;
        this.statModifier = new StatModifier();
        this.upgradeModifier = new StatModifier();
        setupStatModifiers();
    }

    protected abstract void setupStatModifiers();

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
        return getModifiers().get(Stat.MASS).floatValue();
    }

    public StatModifier getStatModifier() {
        return statModifier;
    }

    public HashMap<Stat, Number> getModifiers() {
        return statModifier.getModifiers();
    }

    public StatModifier getUpgradeStatModifier() {
        return upgradeModifier;
    }

    public HashMap<Stat, Number> getUpgradeModifiers() {
        return upgradeModifier.getModifiers();
    }

    /**
     * Upgrade <code>this</code> to the next stage if possible.
     *
     * @return true if <code>this</code> was upgraded, false otherwise.
     */
    public boolean upgrade() {
        if (stage.isUpgradeable()) {
            stage = stage.nextStage(stage);
            statModifier.addModifier(upgradeModifier);
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
            case FUSELAGE -> new Fuselage();
        };
    }

    /**
     * The resources to loot from this {@code ShipUpgrade}
     *
     * @return the amount of resources to loot.
     */
    public int getResourceValue() {
        int value = getModifiers().get(Stat.RESOURCE_VALUE).intValue();

        return value * (stage.ordinal() + 1);
    }
}
