package model.ShipComponents.Components;

import grid.CellPosition;
import grid.GridCell;
import grid.IGrid;
import model.ShipComponents.UpgradeType;
import model.utils.FloatPair;

public interface ViewableShipStructure extends Iterable<GridCell<Fuselage>> {

    /**
     * @return the width of the ship grid.
     */
    int getWidth();

    /**
     * @return the height of the ship grid.
     */
    int getHeight();

    /**
     * @return the total mass of the ship.
     */
    float getMass();

    /**
     * Checks if the given position contains a fuselage.
     *
     * @param cp The position to check.
     * @return true if a fuselage is present, false otherwise.
     */
    boolean hasFuselage(CellPosition cp);

    /**
     * Checks if the given position contains an upgrade.
     *
     * @param cp The position to check.
     * @return true if an upgrade is present, false otherwise.
     */
    boolean hasUpgrade(CellPosition cp);

    /**
     * Retrieves the upgrade type at the specified position.
     *
     * @param cp The position of the upgrade.
     * @return The {@link UpgradeType} at the given position.
     */
    UpgradeType getUpgradeType(CellPosition cp);

    /**
     * @return the ship's center of mass as a {@link FloatPair}.
     */
    FloatPair getCenterOfMass();

    /**
     * @return the underlying grid containing the ship structure.
     */
    IGrid<Fuselage> getGrid();
}
