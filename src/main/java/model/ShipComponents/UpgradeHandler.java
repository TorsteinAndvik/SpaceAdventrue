package model.ShipComponents;

import grid.CellPosition;
import grid.IGrid;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.ShipUpgrade;

public class UpgradeHandler {

    private final int gridExp = 2;
    private final ShipStructure structure;


    public UpgradeHandler(ShipStructure shipStructure) {
        structure = shipStructure;
        structure.expandGrid(gridExp, gridExp, true);
    }

    public boolean canPlaceItem(CellPosition cp, UpgradeType type) {
        if (type == UpgradeType.FUSELAGE) {
            return structure.canBuildAt(cp);
        }
        return !structure.hasUpgrade(cp);
    }

    public boolean placeItem(CellPosition cp, UpgradeType type) {
        if (!canPlaceItem(cp, type)) {
            return false;
        }
        boolean placedItem;
        if (type == UpgradeType.FUSELAGE) {
            placedItem = set(cp, new Fuselage());
        } else {
            placedItem = set(cp, ShipUpgrade.getShipUpgrade(type));
        }

        if (placedItem) {
            structure.normalize();
            structure.expandGrid(2, 2, true);
        }

        return placedItem;
    }

    /**
     * Sets the fuselage at the given position if it is empty and the position is on
     * the grid. Updates ship's mass and center of mass accordingly.
     *
     * @param pos     the <code>CellPosition</code> of the fuselage to be added
     * @param upgrade the <code>ShipUpgrade</code> to be added
     * @return true if the ship upgrade was successfully added, false otherwise
     */
    private boolean set(CellPosition pos, ShipUpgrade upgrade) {
        if (structure.isValidBuildPosition(pos, upgrade)) {
            return structure.addUpgrade(pos, upgrade);
        }
        return false;
    }


    public IGrid<Fuselage> getGrid() {
        return structure.getGridCopy();
    }

    public void exit() {
        structure.normalize();
    }
}
