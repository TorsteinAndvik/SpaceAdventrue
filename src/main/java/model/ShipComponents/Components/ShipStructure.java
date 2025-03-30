package model.ShipComponents.Components;

import grid.CellPosition;
import grid.Grid;
import grid.GridCell;
import grid.IGrid;
import java.util.Iterator;
import model.ShipComponents.ShipConfig;
import model.ShipComponents.ShipConfig.ShipComponent;
import model.ShipComponents.UpgradeType;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;

public class ShipStructure implements ViewableShipStructure {

    private IGrid<Fuselage> grid;
    private float mass;
    private FloatPair centerOfMass;

    public ShipStructure(int width, int height) {
        this(new Grid<>(height, width), 0f, new FloatPair(0f, 0f));


    }

    public ShipStructure(IGrid<Fuselage> grid, float mass, FloatPair centerOfMass) {
        this.grid = grid;
        this.mass = mass;
        this.centerOfMass = centerOfMass;

    }

    public ShipStructure(IGrid<Fuselage> grid) {
        this(grid.cols(), grid.rows());
        MassProperties massProperties = getMassProperties(grid);
        this.mass = massProperties.mass();
        this.centerOfMass = massProperties.centerOfMass();
    }

    public ShipStructure(ShipConfig shipConfig) {
        this(shipConfig.width, shipConfig.height);

        for (ShipComponent component : shipConfig.components) {

            Fuselage fuselage = new Fuselage();

            if (component.upgrade != null) {
                ShipUpgrade upgradeType = switch (component.upgrade.type) {
                    case TURRET -> new Turret();
                    case SHIELD -> new Shield();
                    case THRUSTER -> new Thruster();
                };
                fuselage.setUpgrade(upgradeType);
            }

            set(component.getPosition(), fuselage);
        }
    }

    /**
     * Sets the fuselage at the given position if it is empty and the position is on
     * the grid. Updates ship's mass and center of mass accordingly.
     *
     * @param pos      the <code>CellPosition</code> of the fuselage to be added
     * @param fuselage the <code>Fuselage</code> to be added
     * @return true if the fuselage was successfully added (i.e. <code>pos</code> is
     * valid and the position was empty), false otherwise
     */
    public boolean set(CellPosition pos, Fuselage fuselage) {
        try {
            if (grid.get(pos) != null) {
                return false;
            }

            grid.set(pos, fuselage);

            updateMassAndCenterOfMass(pos, fuselage.getMass());

            return true;

        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Updates the ship structure by adding a fuselage at the specified position.
     * Expands the grid temporarily to ensure sufficient space for placement.
     *
     * @param pos The position where the fuselage should be placed.
     * @return {@code true} if the fuselage was successfully placed, {@code false} if placement was
     * not possible.
     */
    public boolean updateWithFuselage(CellPosition pos) {

        // Local grid must match the grid from the updateScreen, which is expanded by 2x2
        grid = getExpandedGrid(grid, 2, 2, true);

        if (!canBuildAt(pos, grid)) {
            grid = Grid.shrinkGridToFit(grid);
            return false;
        }

        grid.set(pos, new Fuselage());
        grid = Grid.shrinkGridToFit(grid);

        MassProperties massProp = ShipStructure.getMassProperties(grid);
        mass = massProp.mass();
        centerOfMass = massProp.centerOfMass();

        return true;
    }

    private void updateMassAndCenterOfMass(CellPosition pos, float mass) {
        float oldMass = this.mass;
        float newMass = oldMass + mass;

        float cmX = (oldMass * this.centerOfMass.x() + mass * pos.col()) / newMass;
        float cmY = (oldMass * this.centerOfMass.y() + mass * pos.row()) / newMass;

        FloatPair newCM = new FloatPair(cmX, cmY);

        this.mass = newMass;
        this.centerOfMass = newCM;
    }

    /**
     * Computes the total mass and center of mass of a given {@link ShipStructure}.
     * <p>
     * The method iterates over all {@link Fuselage} components in the ship structure,
     * accumulating their mass and computing a weighted average to determine the center of mass.
     * </p>
     *
     * @param shipStructure The {@link ShipStructure} whose mass properties are to be calculated.
     * @return A {@link MassProperties} object containing the total mass and center of mass.
     */
    public static MassProperties getMassProperties(ShipStructure shipStructure) {
        return getMassProperties(shipStructure.grid);
    }

    /**
     * Computes the total mass and center of mass of a given {@link IGrid<Fuselage>}.
     * <p>
     * The method iterates over all {@link Fuselage} components in the ship grid,
     * accumulating their mass and computing a weighted average to determine the center of mass.
     * </p>
     *
     * @param shipGrid The {@link IGrid<Fuselage>} whose mass properties are to be calculated.
     * @return A {@link MassProperties} object containing the total mass and center of mass.
     */
    private static MassProperties getMassProperties(IGrid<Fuselage> shipGrid) {
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
     * Sets an empty <code>Fuselage</code> at the given position if it is empty and
     * the position is on the grid.
     * Updates ship's mass and center of mass accordingly.
     *
     * @param pos the <code>CellPosition</code> to add an empty
     *            <code>Fuselage</code> to
     * @return true if the fuselage was successfully added (i.e. <code>pos</code> is
     * valid and the
     * position was empty), false otherwise
     */
    public boolean set(CellPosition pos) {
        return set(pos, new Fuselage());
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
     * valid and the
     * position holds an empty <code>Fuselage</code>), false otherwise
     */
    public boolean addUpgrade(CellPosition pos, ShipUpgrade upgrade) {
        if (upgrade == null || !grid.positionIsOnGrid(pos)) { return false; }

        Fuselage fuselage = grid.get(pos);
        if (fuselage == null) { return false; }

        if (fuselage.setUpgrade(upgrade)) {
            updateMassAndCenterOfMass(pos, upgrade.getMass());
            return true;
        }
        return false;
    }

    /**
     * Expands the grid by the given number of rows and columns.
     * <p>
     * If the inputs are negative, nothing happens.
     * <p>
     * TODO: needs a more detailed javadoc
     *
     * @param addedCols
     * @param addedRows
     * @param center
     */
    public ShipStructure expandGrid(int addedRows, int addedCols, boolean center) {
        grid = getExpandedGrid(grid.copy(), addedRows, addedCols, center);

        MassProperties massProperties = getMassProperties(this);
        this.mass = massProperties.mass();
        this.centerOfMass = massProperties.centerOfMass();

        return this;
    }

    /**
     * Expands the given grid by adding rows and columns, either centering the existing content or
     * shifting it to the bottom-right.
     *
     * <p>If {@code addedRows} or {@code addedCols} are negative, or both are zero, the original
     * grid is returned unchanged.</p>
     *
     * @param grid      The original grid to expand.
     * @param addedRows The number of rows to add.
     * @param addedCols The number of columns to add.
     * @param center    If {@code true}, shifts the old grid content to keep it centered in the
     *                  expanded grid. If {@code false}, shifts the content towards the
     *                  bottom-right.
     * @return A new {@code IGrid<Fuselage>} instance with the expanded dimensions, containing the
     * original grid’s elements repositioned accordingly.
     */
    public static IGrid<Fuselage> getExpandedGrid(IGrid<Fuselage> grid, int addedRows,
        int addedCols, boolean center) {
        if (addedRows < 0 || addedCols < 0 || (addedRows == 0 && addedCols == 0)) {
            return grid;
        }

        IGrid<Fuselage> extGrid = new Grid<>(grid.rows() + addedRows,
            grid.cols() + addedCols);

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
        return extGrid;
    }

    @Override
    public Iterator<GridCell<Fuselage>> iterator() {
        return this.grid.iterator();
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
    public IGrid<Fuselage> getGrid() {
        return this.grid.copy();
    }

    @Override
    public boolean isValidFuselagePosition(CellPosition pos) {
        return isValidFuselagePosition(grid, pos);
    }

    /**
     * Checks if a fuselage can be placed at the specified position.
     * A valid position must be empty and adjacent to at least one existing fuselage.
     *
     * @param structureGrid The grid containing the ship structure.
     * @param pos           The position to check for fuselage placement.
     * @return {@code true} if the position is valid for fuselage placement, {@code false} otherwise.
     */
    public static boolean isValidFuselagePosition(IGrid<Fuselage> structureGrid, CellPosition pos) {
        if (structureGrid.positionIsOnGrid(pos) && !structureGrid.isEmptyAt(pos)) {
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
     * A valid position must contain a fuselage and must not already have an upgrade.
     *
     * @param grid The grid containing fuselages.
     * @param pos  The position to check for upgrade placement.
     * @return {@code true} if an upgrade can be placed, {@code false} otherwise.
     */
    public static boolean isValidUpgradePosition(IGrid<Fuselage> grid, CellPosition pos) {
        if (!grid.positionIsOnGrid(pos) || grid.isEmptyAt(pos)) {
            return false;
        }
        return !grid.get(pos).hasUpgrade();
    }

    @Override
    public boolean isValidUpgradePosition(CellPosition pos) {
        return isValidFuselagePosition(grid, pos);
    }

    @Override
    public boolean isOnGrid(CellPosition cp) {
        return cp.row() >= 0 && cp.col() >= 0 && cp.row() < getHeight()
            && cp.col() < getWidth();
    }

    @Override
    public boolean canBuildAt(CellPosition pos) {
        return canBuildAt(pos, getExpandedGrid(grid, 2, 2, true));
    }

    /**
     * Checks if a fuselage can be placed at the given position within the specified grid.
     * A fuselage cannot be placed if there is already one at the position.
     * The position must also be adjacent to at least one existing fuselage.
     *
     * @param pos  The position to check.
     * @param grid The grid in which to check the position.
     * @return {@code true} if a fuselage can be built at the position, {@code false} otherwise.
     */
    public static boolean canBuildAt(CellPosition pos, IGrid<Fuselage> grid) {

        if (hasFuselage(pos, grid)) {
            return false;
        }
        return isValidFuselagePosition(grid, pos);

    }

}
