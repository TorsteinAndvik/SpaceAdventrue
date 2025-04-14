package model.ShipComponents;

import grid.CellPosition;
import grid.Grid;
import grid.GridCell;
import grid.IGrid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.ShipUpgrade;
import model.ShipComponents.ShipConfig.ShipComponent;
import model.utils.FloatPair;
import model.utils.MassProperties;
import model.utils.SpaceCalculator;

public class ShipStructure implements ViewableShipStructure {

    private IGrid<Fuselage> grid;
    private float mass;
    private float radius;
    private FloatPair centerOfMass;
    private boolean gridExpanded;

    public ShipStructure(int width, int height) {
        this(new Grid<>(height, width));
    }

    public ShipStructure(IGrid<Fuselage> grid) {
        // TODO: since building has to be valid, we should also validate the grid
        this.grid = grid;
        updateFields();
    }

    public ShipStructure(ShipConfig shipConfig) {
        this(shipConfig.width, shipConfig.height);

        for (ShipComponent component : shipConfig.components) {

            Fuselage fuselage = new Fuselage();

            if (component.upgrade != null) {
                ShipUpgrade upgradeType = ShipUpgrade.getShipUpgrade(component.upgrade.type);
                fuselage.setUpgrade(upgradeType);
            }

            setFuselage(component.getPosition(), fuselage);
        }
        updateFields();
    }

    private void updateFields() {
        updateMassAndCenterOfMass();
        updateRadius();
    }

    /**
     * Updates the ship structure by adding a fuselage at the specified position.
     * Expands the grid temporarily to ensure sufficient space for placement.
     *
     * @param pos The position where the fuselage should be placed.
     * @return {@code true} if the fuselage was successfully placed, {@code false}
     *         if placement was
     *         not possible.
     */
    public boolean updateWithFuselage(CellPosition pos) {

        // Local grid must match the grid from the updateScreen, which is expanded by
        // 2x2
        grid = getExpandedGrid(grid, 2, 2, true);

        if (!canBuildAt(pos, grid)) {
            grid = Grid.shrinkGridToFit(grid);
            return false;
        }

        grid.set(pos, new Fuselage());
        grid = Grid.shrinkGridToFit(grid);

        updateFields();

        return true;
    }

    /**
     * Computes the total mass and center of mass of a given {@link ShipStructure}.
     * Computes the total mass and center of mass of the {@link ShipStructure}.
     * <p>
     * The method iterates over all {@link Fuselage} components in the ship
     * structure,
     * accumulating their mass and computing a weighted average to determine the
     * center of mass.
     * </p>
     *
     * @return A {@link MassProperties} object containing the total mass and center
     *         of mass.
     */
    public MassProperties getMassProperties() {
        return getMassProperties(grid);
    }

    /**
     * Computes the total mass and center of mass of a given
     * {@link IGrid<Fuselage>}.
     * <p>
     * The method iterates over all {@link Fuselage} components in the ship grid,
     * accumulating their mass and computing a weighted average to determine the
     * center of mass.
     * </p>
     *
     * @param shipGrid The {@link IGrid<Fuselage>} whose mass properties are to be
     *                 calculated.
     * @return A {@link MassProperties} object containing the total mass and center
     *         of mass.
     */
    public MassProperties getMassProperties(IGrid<Fuselage> shipGrid) {
        float prevMass;
        float newMass = 0;
        FloatPair centerOfMass = new FloatPair(0f, 0f);

        for (GridCell<Fuselage> cell : shipGrid) {
            prevMass = newMass;
            if (cell.value() == null) {
                continue;
            }
            CellPosition pos = cell.pos();

            float currentMass = cell.value().getMass();
            newMass = prevMass + currentMass;

            float cmX = (prevMass * centerOfMass.x() + currentMass * pos.col()) / newMass;
            float cmY = (prevMass * centerOfMass.y() + currentMass * pos.row()) / newMass;

            centerOfMass = new FloatPair(cmX, cmY);

        }
        return new MassProperties(newMass, centerOfMass);
    }

    /**
     * Adds a <code>ShipUpgrade</code> to the <code>Fuselage</code> at the given
     * <code>CellPosition </code>, if the position is valid and holds an empty
     * <code>Fuselage</code>. Updates ship's mass and center of mass accordingly.
     *
     * @param pos     the <code>CellPosition</code> where the upgrade is be to added
     *                to
     * @param upgrade the <code>ShipUpgrade</code> to be added
     * @return true if the upgrade was successfully added (i.e. <code>pos</code> is
     *         valid and the position holds an empty <code>Fuselage</code>), false
     *         otherwise
     */
    public boolean addUpgrade(CellPosition pos, ShipUpgrade upgrade) {
        if (upgrade == null || !grid.positionIsOnGrid(pos)) {
            return false;
        }

        if (upgrade.getType() == UpgradeType.FUSELAGE) {
            return setFuselage(pos, (Fuselage) upgrade);
        }

        if (grid.isEmptyAt(pos)) {
            return false;
        }

        Fuselage base = grid.get(pos);
        if (base.setUpgrade(upgrade)) {
            updateMassAndCenterOfMass();
            return true;
        }
        return false;
    }

    public boolean setFuselage(CellPosition pos, Fuselage fuselage) {
        if (canBuildAt(pos)) {
            grid.set(pos, fuselage);
            updateMassAndCenterOfMass();
            return true;
        }
        return false;
    }

    public boolean setFuselage(CellPosition pos) {
        return setFuselage(pos, new Fuselage());
    }

    /**
     * Expands the given grid by adding rows and columns, either centering the
     * existing content or shifting it to the bottom-right.
     *
     * <p>
     * If {@code addedRows} or {@code addedCols} are negative, or both are zero, the
     * original grid is returned unchanged.
     * </p>
     *
     * @param addedRows The number of rows to add.
     * @param addedCols The number of columns to add.
     * @param center    If {@code true}, shifts the old grid content to keep it
     *                  centered in the
     *                  expanded grid. If {@code false}, shifts the content towards
     *                  the
     *                  bottom-right.
     */
    public void expandGrid(int addedRows, int addedCols, boolean center) {
        grid = getExpandedGrid(grid, addedRows, addedCols, center);
    }

    /**
     * Expands the given grid by adding rows and columns, either centering the
     * existing content or shifting it to the bottom-right.
     *
     * <p>
     * If {@code addedRows} or {@code addedCols} are negative, or both are zero, the
     * original grid is returned unchanged.
     * </p>
     *
     * @param grid      The original grid to expand.
     * @param addedRows The number of rows to add.
     * @param addedCols The number of columns to add.
     * @param center    If {@code true}, shifts the old grid content to keep it
     *                  centered in the
     *                  expanded grid. If {@code false}, shifts the content towards
     *                  the
     *                  bottom-right.
     * @return A new {@code IGrid<Fuselage>} instance with the expanded dimensions,
     *         containing the original grid’s elements repositioned accordingly.
     */
    protected IGrid<Fuselage> getExpandedGrid(IGrid<Fuselage> grid, int addedRows,
            int addedCols, boolean center) {
        if (addedRows < 0 || addedCols < 0 || (addedRows == 0 && addedCols == 0)) {
            gridExpanded = false;
            return grid;
        }

        IGrid<Fuselage> extGrid = new Grid<>(grid.rows() + addedRows, grid.cols() + addedCols);

        for (GridCell<Fuselage> cell : grid) {
            if (cell.value() == null) {
                continue;
            }

            CellPosition cp;
            if (center) {
                cp = new CellPosition(cell.pos().row() + (addedRows - addedRows / 2),
                        cell.pos().col() + (addedCols - addedCols / 2));
            } else {
                cp = new CellPosition(cell.pos().row() + addedRows, cell.pos().col() + addedCols);
            }
            extGrid.set(cp, cell.value());
        }
        gridExpanded = true;
        return extGrid;
    }

    @Override
    public Iterator<GridCell<Fuselage>> iterator() {
        return this.grid.iterator();
    }

    /**
     * @param type the <code>UpgradeType</code> we are looking for
     * @return a <code>List</code> of <code>CellPosition</code> for each of the
     *         <code>Fuselage</code> holding an upgrade of the given
     *         <code>UpgradeType</code> in <code>this</code>.
     */
    public List<CellPosition> getUpgradeTypePositions(UpgradeType type) {
        List<CellPosition> upgradeTypePositions = new ArrayList<>();
        for (GridCell<Fuselage> cell : grid) {
            if (cell.value() == null) {
                continue;
            }

            if (cell.value().hasUpgrade() && cell.value().getUpgrade().getType() == type) {
                upgradeTypePositions.add(cell.pos());
            }
        }
        return upgradeTypePositions;
    }

    @Override
    public int getWidth() {
        return grid.cols();
    }

    @Override
    public int getHeight() {
        return grid.rows();
    }

    @Override
    public float getMass() {
        return this.mass;
    }

    @Override
    public FloatPair getCenterOfMass() {
        return this.centerOfMass;
    }

    @Override
    public boolean hasFuselage(CellPosition cp) {
        return hasFuselage(cp, grid);
    }

    public static boolean hasFuselage(CellPosition cp, IGrid<Fuselage> grid) {
        try {
            return grid.get(cp) != null;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean hasUpgrade(CellPosition cp) {
        if (!hasFuselage(cp)) {
            return false;
        }
        return this.grid.get(cp).hasUpgrade();
    }

    @Override
    public UpgradeType getUpgradeType(CellPosition cp) {
        return this.grid.get(cp).getUpgrade().getType();
    }

    @Override
    public IGrid<Fuselage> getGridCopy() {
        return this.grid.copy();
    }

    @Override
    public boolean isValidFuselagePosition(CellPosition pos) {
        return isValidFuselagePosition(grid, pos);
    }

    /**
     * Checks if a fuselage can be placed at the specified position.
     * A valid position must be empty and adjacent to at least one existing
     * fuselage.
     *
     * @param structureGrid The grid containing the ship structure.
     * @param pos           The position to check for fuselage placement.
     * @return {@code true} if the position is valid for fuselage placement,
     *         {@code false} otherwise.
     */
    public static boolean isValidFuselagePosition(IGrid<Fuselage> structureGrid, CellPosition pos) {
        if (!structureGrid.positionIsOnGrid(pos)) {
            return false;
        }

        if (structureGrid.isEmpty()) {
            return true;
        }

        if (hasFuselage(pos, structureGrid)) {
            return false;
        }

        for (CellPosition cp : SpaceCalculator.getOrthogonalNeighbours(pos)) {

            if (!structureGrid.positionIsOnGrid(cp)) {
                continue;
            }

            if (!structureGrid.isEmptyAt(cp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an upgrade can be placed at the specified position.
     * A valid position must contain a fuselage and must not already have an
     * upgrade.
     *
     * @param grid The grid containing fuselages.
     * @param pos  The position to check for upgrade placement.
     * @return {@code true} if an upgrade can be placed, {@code false} otherwise.
     */
    public static boolean isValidUpgradePosition(IGrid<Fuselage> grid, CellPosition pos) {
        if (!grid.positionIsOnGrid(pos) || grid.isEmptyAt(pos)) {
            return false;
        }

        System.out.println("here");
        return !grid.get(pos).hasUpgrade();
    }

    /**
     * Checks if an upgrade can be placed at the specified position.
     * A valid position must contain a fuselage and must not already have an
     * upgrade.
     *
     * @param pos The position to check for upgrade placement.
     * @return {@code true} if an upgrade can be placed, {@code false} otherwise.
     */
    public boolean isValidUpgradePosition(CellPosition pos) {
        return isValidUpgradePosition(grid, pos);
    }

    @Override
    public boolean isValidBuildPosition(CellPosition pos, ShipUpgrade upgrade) {
        if (upgrade.getType() == UpgradeType.FUSELAGE) {
            return isValidFuselagePosition(pos);
        }
        return isValidUpgradePosition(grid, pos);
    }

    @Override
    public boolean isOnGrid(CellPosition cp) {
        return cp.row() >= 0 && cp.col() >= 0 && cp.row() < getHeight()
                && cp.col() < getWidth();
    }

    @Override
    public boolean canBuildAt(CellPosition pos) {
        return canBuildAt(pos, grid);
    }

    /**
     * Checks if a fuselage can be placed at the given position within the specified
     * grid.
     * A fuselage cannot be placed if there is already one at the position.
     * The position must also be adjacent to at least one existing fuselage.
     *
     * @param pos  The position to check.
     * @param grid The grid in which to check the position.
     * @return {@code true} if a fuselage can be built at the position,
     *         {@code false} otherwise.
     */
    public static boolean canBuildAt(CellPosition pos, IGrid<Fuselage> grid) {

        if (!grid.positionIsOnGrid(pos)) {
            return false;
        }

        if (hasFuselage(pos, grid)) {
            return false;
        }

        return grid.isEmpty() || isValidFuselagePosition(grid, pos);
    }

    protected void shrinkToFit() {
        grid = Grid.shrinkGridToFit(grid);
        gridExpanded = false;
        updateFields();
    }

    public boolean isGridExpanded() {
        return gridExpanded;
    }

    /**
     * The resources to loot from this {@code ShipStructure}
     *
     * @return the amount of resources to loot.
     */
    public int getResourceValue() {
        int resourceValue = 0;
        for (GridCell<Fuselage> gridCells : grid) {
            if (gridCells.value() != null) {
                resourceValue += gridCells.value().getResourceValue();
            }
        }
        return resourceValue;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    /**
     * Normalizes the structure of the ship grid by performing two key operations:
     * If the grid is expanded beyond what is necessary, it shrinks the grid to fit
     * the placed components.
     * Recalculates the radius, total mass and the center of mass of the ship based
     * on current components.
     * <p>
     * This method ensures that the structural representation of the ship is both
     * space-efficient and physically accurate.
     */
    public void normalize() {
        // TODO: fine to just call shrinkToFit without a check, nothing bad happens.
        // TODO: remove this? shrinkToFit has to recalculate fields anyways
        if (isGridExpanded()) {
            shrinkToFit();
        }
    }

    private void updateRadius() {
        radius = SpaceCalculator.distance(getWidth() / 2f, getHeight() / 2f);
    }

    private void updateMassAndCenterOfMass() {
        MassProperties mp = getMassProperties();
        mass = mp.mass();
        centerOfMass = mp.centerOfMass();
    }
}
