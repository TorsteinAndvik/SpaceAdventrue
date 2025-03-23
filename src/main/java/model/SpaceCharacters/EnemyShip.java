package model.SpaceCharacters;

import model.ShipComponents.Components.ShipStructure;

public class EnemyShip extends SpaceShip {

    public EnemyShip(ShipStructure shipStructure, String name, String description, float x, float y,
        int hitPoints,
        float angle) {
        super(shipStructure, name, description, CharacterType.ENEMY_SHIP, x, y, hitPoints, angle);
    }
}
