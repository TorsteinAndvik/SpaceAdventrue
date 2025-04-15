package model.ShipComponents.Components;

import java.util.HashMap;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.stats.Stat;
import model.ShipComponents.Components.stats.StatModifier;
import model.constants.PhysicsParameters;

public class Fuselage extends ShipUpgrade {

    private ShipUpgrade heldUpgrade;

    public Fuselage() {
        this(null);
    }

    public Fuselage(ShipUpgrade upgrade) {
        super("Fuselage", "The base building block of a ship", UpgradeType.FUSELAGE, UpgradeStage.ZERO);
        if (upgrade == null || upgrade.getType() == UpgradeType.FUSELAGE) {
            return;
        }
        this.heldUpgrade = upgrade;
    }

    /**
     * Sets the upgrade held by the fuselage. If there is already an upgrade held,
     * nothing happens.
     *
     * @param upgrade The ShipUpgrade to be held by <code>this</code>
     * @return true if <code>upgrade</code> is set (i.e. no upgrade was previously
     *         set), false
     *         otherwise.
     */
    public boolean setUpgrade(ShipUpgrade upgrade) {
        if (upgrade == null || heldUpgrade != null || upgrade.getType() == getType()) {
            return false;
        }
        heldUpgrade = upgrade;
        return true;
    }

    /**
     * Removes the upgrade held by the fuselage. If there is no upgrade set, nothing
     * happens.
     *
     * @return The type of the upgrade held by <code>this</code>.
     */
    public UpgradeType removeUpgrade() {
        UpgradeType type = heldUpgrade.getType();
        heldUpgrade = null;
        return type;
    }

    /**
     * @return The <code>ShipUpgrade</code> held by <code>this</code>.
     */
    public ShipUpgrade getUpgrade() {
        return heldUpgrade;
    }

    /**
     * @return <code>true</code> if <code>this</code> currently holds a
     *         <code>ShipUpgrade</code>, otherwise <code>false</code>.
     */
    public boolean hasUpgrade() {
        return heldUpgrade != null;
    }

    @Override
    public String toString() {
        if (heldUpgrade == null) {
            return "fuselage: empty";
        } else {
            return "fuselage: " + heldUpgrade.getName();
        }
    }

    /**
     * @return combined mass of <code>this</code> and an eventual held
     *         <code>ShipUpgrade</code>.
     */
    public float getMass() {
        return getModifiers().get(Stat.MASS).floatValue();
    }

    @Override
    public int getResourceValue() {
        return getModifiers().get(Stat.RESOURCE_VALUE).intValue();
    }

    @Override
    public StatModifier getStatModifier() {
        if (hasUpgrade()) {
            return statModifier.copy().addModifier(heldUpgrade.getStatModifier());
        }
        return statModifier;
    }

    @Override
    public HashMap<Stat, Number> getModifiers() {
        if (hasUpgrade()) {
            return statModifier.copy().addModifier(heldUpgrade.getStatModifier()).getModifiers();
        }
        return statModifier.getModifiers();
    }

    @Override
    public UpgradeType getType() {
        return UpgradeType.FUSELAGE;
    }

    @Override
    protected void setupStatModifier() {
        statModifier.setModifier(Stat.MASS, PhysicsParameters.fuselageMass);
        statModifier.setModifier(Stat.HEALTH_VALUE, 5);
        statModifier.setModifier(Stat.RESOURCE_VALUE, 10);
    }
}
