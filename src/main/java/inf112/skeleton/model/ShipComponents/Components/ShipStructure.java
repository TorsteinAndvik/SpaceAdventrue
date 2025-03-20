package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.grid.Grid;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.grid.IGrid;
import inf112.skeleton.model.ShipComponents.ShipConfig;
import inf112.skeleton.model.ShipComponents.ShipConfig.ShipComponent;
import inf112.skeleton.model.utils.FloatPair;

public class ShipStructure {

    private final IGrid<Fuselage> grid;
    private float mass;
    private FloatPair centerOfMass;

    public ShipStructure(int width, int height) {
        this.grid = new Grid<>(height, width);
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
     *         valid and the
     *         position was empty), false otherwise
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
     *         valid and the
     *         position was empty), false otherwise
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
     *         valid and the
     *         position holds an empty <code>Fuselage</code>), false otherwise
     */
    public boolean addUpgrade(CellPosition pos, ShipUpgrade upgrade) {
        try {
            Fuselage fuselage = grid.get(pos);
            if (fuselage == null) {
                return false;
            }
            return fuselage.setUpgrade(upgrade);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public Iterable<GridCell<Fuselage>> iterable() {
        return grid;
    }

    public int getWidth() {
        return grid.cols();
    }

    public int getHeight() {
        return grid.rows();
    }

    /**
     * @return the total mass of the ship.
     */
    public float getMass() {
        return this.mass;
    }

    /**
     * @return the ship's center of mass as a <code>FloatPair</code>
     */
    public FloatPair getCenterOfMass() {
        return this.centerOfMass;
    }
}
