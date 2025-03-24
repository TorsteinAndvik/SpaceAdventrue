package model.ShipComponents.Components;

import grid.CellPosition;
import grid.GridCell;
import grid.IGrid;
import model.ShipComponents.UpgradeType;
import model.utils.FloatPair;

public interface ViewableShipStructure extends Iterable<GridCell<Fuselage>> {

    int getWidth();

    int getHeight();

    /**
     * @return the total mass of the ship.
     */
    float getMass();

    boolean hasFuselage(CellPosition cp);

    boolean hasUpgrade(CellPosition cp);

    UpgradeType getUpgradeType(CellPosition cp);

    /**
     * @return the ship's center of mass as a <code>FloatPair</code>
     */
    FloatPair getCenterOfMass();

    Iterable<GridCell<Fuselage>> iterable();

    IGrid<Fuselage> getGrid();
}
