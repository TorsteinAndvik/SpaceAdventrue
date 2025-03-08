package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.ShipComponents.Components.ShipStructure;

public class Player extends SpaceShip {

    public Player(ShipStructure shipStructure, String name, String description, int hitPoints, int x, int y, int mass,
            int speed, float angle, int radius) {
        super(shipStructure, name, description, hitPoints, x, y, mass, speed, angle, radius);
    }

    @Override
    public boolean isPlayerShip() {
        return true;
    }
}
