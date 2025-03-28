package model.SpaceCharacters;

import model.ShipComponents.Components.ShipStructure;

public class Player extends SpaceShip {

    public Player(ShipStructure shipStructure, String name, String description, int hitPoints,
            float x, float y) {
        super(shipStructure, name, description, CharacterType.PLAYER, x, y, hitPoints, 0);
    }

    @Override
    public boolean isPlayerShip() {
        return true;
    }
}