package model.SpaceCharacters.Ships;

import model.ShipComponents.ShipStructure;
import model.ViewableSpaceShip;

public interface ViewablePlayer extends ViewableSpaceShip {

    /**
     * Returns the structure of the player's ship.
     *
     * @return the {@link ShipStructure} representing the current state of the ship's structure
     */
    ShipStructure getShipStructure();

    /**
     * Returns the current inventory of the player’s ship. This includes items
     * like cargo, upgrades, and collectibles.
     *
     * @return the {@link ViewableInventory} associated with the player
     */
    ViewableInventory getInventory();

}
