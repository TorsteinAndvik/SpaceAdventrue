package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.grid.IGrid;
import inf112.skeleton.model.ShipComponents.UpgradeType;
import inf112.skeleton.model.utils.FloatPair;

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
