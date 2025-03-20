package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.model.ShipComponents.UpgradeType;

public class Fuselage {

    private ShipUpgrade heldUpgrade;

    private float mass = 1f;

    public Fuselage() {
    }

    public Fuselage(ShipUpgrade upgrade) {
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
        if (heldUpgrade != null) {
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
     * @return mass of a piece of <code>Fuselage</code>.
     */
    public float getMass() {
        return mass;
    }
}
