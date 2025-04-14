package model.ShipComponents;

import grid.CellPosition;
import grid.GridCell;
import grid.IGrid;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.ShipUpgrade;
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
    IGrid<Fuselage> getGridCopy();

    boolean isOnGrid(CellPosition cp);

    /**
     * Checks if a fuselage can be placed at the specified position.
     * A valid position must be empty and adjacent to at least one existing fuselage.
     *
     * @param pos The position to check for fuselage placement.
     * @return {@code true} if the position is valid for fuselage placement, {@code false} otherwise.
     */
    boolean isValidFuselagePosition(CellPosition pos);

    /**
     * Checks if an upgrade can be placed at the specified position.
     *
     * @param pos The position to check for upgrade placement.
     * @return {@code true} if an upgrade can be placed, {@code false} otherwise.
     */
    boolean isValidBuildPosition(CellPosition pos, ShipUpgrade upgrade);

    /**
     * Determines if a fuselage can be built at the specified position.
     * This method expands the grid by a 2x2 margin before checking the validity of the position.
     *
     * @param pos The position to check for fuselage placement.
     * @return {@code true} if a fuselage can be built at the position, {@code false} otherwise.
     */
    boolean canBuildAt(CellPosition pos);

    /**
     * @return the radius of the shipStructure.
     */
    float getRadius();
}
