package model.ShipComponents;

import grid.CellPosition;
import grid.IGrid;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.ShipUpgrade;

public class UpgradeHandler {

    private final ShipStructure structure;

    public UpgradeHandler(ShipStructure shipStructure) {
        structure = shipStructure;
    }

    public void expand() {
        int gridExp = 2;
        structure.expandGrid(gridExp, gridExp, true);
    }

    public boolean canPlaceItem(CellPosition cp, UpgradeType type) {
        if (type == UpgradeType.FUSELAGE) {
            return structure.canBuildAt(cp);
        }
        return !structure.hasUpgrade(cp);
    }

    public boolean placeItem(CellPosition pos, UpgradeType type) {

        boolean placedItem = structure.addUpgrade(pos, ShipUpgrade.getShipUpgrade(type));

        if (placedItem) {
            structure.normalize();
            expand();
        }

        return placedItem;
    }

    /**
     * Checks if the specified position contains a <code>Fuselage</code>.
     * 
     * @param cp the <code>CellPosition</code> to check
     * @return <code>true</code> if the position contains a <code>Fuselage</code>,
     *         <code>false</code> otherwise.
     */
    public boolean hasFuselage(CellPosition cp) {
        return structure.hasFuselage(cp);
    }

    /**
     * Gets the <code>Fuselage</code> at the specified <code>CellPosition</code>.
     * This is simply a pass-through call to
     * <code>ShipStructure.getFuselage()</code> and may return <code>null</code>.
     * 
     * @param cp the <code>CellPosition</code> to get the <code>Fuselage</code> from
     * @return the <code>Fuselage</code> at the specified <code>CellPosition</code>
     */
    public Fuselage getFuselage(CellPosition cp) {
        return structure.getFuselage(cp);
    }

    public IGrid<Fuselage> getGrid() {
        return structure.getGridCopy();
    }

    public void exit() {
        structure.normalize();
    }

    /**
     * Upgrade the stage of the upgrade at the specified position.
     * If <code>upgradeFuselage</code> is <code>true</code>, this upgrades the
     * the fuselage, otherwise it upgrades the held upgrade.
     * <p>
     * The held upgrade will only be upgraded if the upgrade stage of the fuselage
     * is greater than the upgrade stage of the held upgrade.
     * 
     * @param cpGrid          the <code>CellPosition</code> of the upgrade.
     * @param upgradeFuselage whether it's the <code>Fuselage</code> at
     *                        <code>cpGrid</code> or its held
     *                        upgrade that should be upgraded.
     * @return <code>true</code> if the component was upgraded,
     *         <code>false</code> otherwise.
     */
    public boolean upgradeStage(CellPosition cpGrid, boolean upgradeFuselage) {
        Fuselage fuselage = structure.getFuselage(cpGrid);
        if (upgradeFuselage) {
            return fuselage.upgrade();
        } else {
            if (upgradeStageIncreaseIsAllowed(fuselage)) {
                return fuselage.getUpgrade().upgrade();
            }
            return false;
        }
    }

    /**
     * Checks if the upgrade stage of the <code>fuselage</code>'s
     * held upgrade can be increased.
     * <p>
     * The held upgrade may only be upgraded if the upgrade stage of the
     * fuselage is greater than the upgrade stage of the held upgrade.
     * <p>
     * Note that this does not consider whether the upgrade stage of
     * <code>fuselage</code> or the held upgrade are themselves increasable.
     * This only compares the fuselage's upgrade stage to the held upgrade's
     * upgrade stage.
     * 
     * @param fuselage the <code>Fuselage</code> to check eligibility for
     * @return <code>true</code> if the upgrade stage of the held upgrade is
     *         allowed to be increased according to the rules specified,
     *         <code>false</code> otherwise.
     */
    public boolean upgradeStageIncreaseIsAllowed(Fuselage fuselage) {
        if (!fuselage.hasUpgrade()) {
            return false;
        }

        return fuselage.getStage().ordinal() > fuselage.getUpgrade().getStage().ordinal();
    }
}
