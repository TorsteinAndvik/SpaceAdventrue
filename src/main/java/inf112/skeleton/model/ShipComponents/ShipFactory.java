package inf112.skeleton.model.ShipComponents;

import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.model.ShipComponents.Components.Fuselage;
import inf112.skeleton.model.ShipComponents.Components.ShipStructure;
import inf112.skeleton.model.ShipComponents.Components.Thruster;
import inf112.skeleton.model.ShipComponents.Components.Turret;

public class ShipFactory {

    public ShipFactory() {}

    /**
     * @return A 1x2 <code>ShipStructure</code> with a turret at the front and a thruster at the back.
     */
    public ShipStructure simpleShip() {
        ShipStructure ship = new ShipStructure(1, 2);
        ship.set(new CellPosition(1, 0), new Fuselage(new Turret()));
        ship.set(new CellPosition(0, 0), new Fuselage(new Thruster()));
        return ship;
    }

    /**
     * @return A 1x2 <code>ShipStructure</code> with a turret at the front and a thruster at the back, set to be the player's ship.
     */
    public ShipStructure simplePlayerShip() {
        ShipStructure ship = new ShipStructure(1, 2, true);
        ship.set(new CellPosition(1, 0), new Fuselage(new Turret()));
        ship.set(new CellPosition(0, 0), new Fuselage(new Thruster()));
        return ship;
    }
}
