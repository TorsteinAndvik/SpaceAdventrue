package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.ShipComponents.Components.ShipStructure;

public class EnemyShip extends SpaceShip {
    public EnemyShip(ShipStructure shipStructure, String name, String description, float x, float y, int hitPoints,
            float angle, int radius) {
        super(shipStructure, name, description, CharacterType.ENEMY_SHIP, x, y, hitPoints, angle, radius);
    }
}
