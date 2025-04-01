package model.SpaceCharacters;

import model.ShipComponents.Components.ShipStructure;

public class Player extends SpaceShip {

    private final PlayerInventory inventory;

    public Player(ShipStructure shipStructure, String name, String description, int hitPoints,
            float x, float y) {
        super(shipStructure, name, description, CharacterType.PLAYER, x, y, hitPoints, 0);
        inventory = new PlayerInventory();
    }

    @Override
    public boolean isPlayerShip() {
        return true;
    }

    /**
     * @return The player inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}