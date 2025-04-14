package model.SpaceCharacters.Ships;

import java.util.HashMap;
import model.ShipComponents.ShipStructure;
import model.SpaceCharacters.CharacterType;

public class Player extends SpaceShip implements ViewablePlayer {

    private final PlayerInventory inventory;

    public Player(ShipStructure shipStructure, String name, String description, int hitPoints,
            float x, float y) {
        super(shipStructure, name, description, CharacterType.PLAYER, x, y, hitPoints, 0);
        inventory = new PlayerInventory(new HashMap<>(), 300);
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