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

public class ShipStructure implements ViewableShipStructure {

    private IGrid<Fuselage> grid;
    private float mass;
    private FloatPair centerOfMass;

    public ShipStructure(int width, int height) {
        this(new Grid<>(height, width));

    }

    public ShipStructure(IGrid<Fuselage> grid) {
        this.grid = grid;
        this.mass = 0f;
        this.centerOfMass = new FloatPair(0f, 0f);

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
     * valid and the
     * position was empty), false otherwise
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
        try {
            Fuselage fuselage = grid.get(pos);
            if (fuselage == null) {
                return false;
            }

            updateMassAndCenterOfMass(pos, upgrade.getMass());
            return fuselage.setUpgrade(upgrade);

        } catch (IndexOutOfBoundsException e) {
            return false;
        }
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
    public void expandGrid(int addedRows, int addedCols, boolean center) {
        grid = getExpandedGrid(grid.copy(), addedRows, addedCols, center);

        this.mass = 0f;
        this.centerOfMass = new FloatPair(0f, 0f);

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
    public Iterable<GridCell<Fuselage>> iterable() {
        return grid;
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
        try {
            return this.grid.get(cp) != null;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean hasUpgrade(CellPosition cp) {
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
}
