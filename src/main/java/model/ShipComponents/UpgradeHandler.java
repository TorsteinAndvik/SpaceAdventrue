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
    }

    public void expand() {
        structure.expandGrid(gridExp, gridExp, true);
    }

    public boolean canPlaceItem(CellPosition cp, UpgradeType type) {
        if (type == UpgradeType.FUSELAGE) {
            return structure.canBuildAt(cp);
        }
        return !structure.hasUpgrade(cp);
    }

    public boolean placeItem(CellPosition pos, UpgradeType type) {
        if (!canPlaceItem(pos, type)) {
            return false;
        }

        boolean placedItem = structure.addUpgrade(pos, ShipUpgrade.getShipUpgrade(type));

        if (placedItem) {
            structure.normalize();
            structure.expandGrid(2, 2, true);
        }

        return placedItem;
    }

    public IGrid<Fuselage> getGrid() {
        return structure.getGridCopy();
    }

    public void exit() {
        structure.normalize();
    }
}
