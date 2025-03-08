package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.grid.Grid;
import inf112.skeleton.grid.GridCell;
import inf112.skeleton.grid.IGrid;

public class ShipStructure {

    private IGrid<Fuselage> grid;
    public final boolean isPlayerShip;
    
    public ShipStructure(int width, int height) {this(width, height, false);}

    public ShipStructure(int width, int height, boolean isPlayerShip) {
        this.grid = new Grid<Fuselage>(height, width);
        this.isPlayerShip = isPlayerShip;
    }

    /**
     * Sets the fuselage at the given position if it is empty and the position is on the grid
     * 
     * @param pos the <code>CellPosition</code> of the fuselage to be added
     * @param fuselage the <code>Fuselage</code> to be added
     * @return true if the fuselage was successfully added (i.e. <code>pos</code> is valid and the position was empty), false otherwise
     */
    public boolean set(CellPosition pos, Fuselage fuselage) {
        try {
            if (grid.get(pos) != null) {return false;}
            grid.set(pos, fuselage);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Sets an empty <code>Fuselage</code> at the given position if it is empty and the position is on the grid
     * 
     * @param pos the <code>CellPosition</code> to add an empty <code>Fuselage</code> to
     * @return true if the fuselage was successfully added (i.e. <code>pos</code> is valid and the position was empty), false otherwise
     */
    public boolean set(CellPosition pos) {
        return set(pos, new Fuselage());
    }

    /**
     * Adds a <code>ShipUpgrade</code> to the <code>Fuselage</code> at the given <code>CellPosition</code>,
     * if the position is valid and holds an empty <code>Fuselage</code>.
     * 
     * @param pos the <code>CellPosition</code> where the upgrade is be to added to
     * @param upgrade the <code>ShipUpgrade</code> to be added
     * @return true if the upgrade was successfully added (i.e. <code>pos</code> is valid and the position holds an empty <code>Fuselage</code>), false otherwise
     */
    public boolean addUpgrade(CellPosition pos, ShipUpgrade upgrade) {
        try {
            Fuselage fuselage = grid.get(pos);
            if (fuselage == null) {return false;}
            return fuselage.setUpgrade(upgrade);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public Iterable<GridCell<Fuselage>> iterable() {
        return grid;
    }
}
