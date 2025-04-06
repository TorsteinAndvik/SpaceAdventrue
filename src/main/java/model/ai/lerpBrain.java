package model.ai;

import model.SpaceCharacters.EnemyShip;
import model.SpaceCharacters.Player;
import model.utils.SpaceCalculator;

public class lerpBrain extends Brain {

    public lerpBrain(EnemyShip ship, Player player) {
        super(ship, player);
    }

    @Override
    public void update(float delta) {
        float angle = SpaceCalculator.angleBetweenPoints(ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass());
        ship.setRotation(delta);
    }

    private float hoverDistance() {
        return 2f * (ship.getRadius() + player.getRadius());
    }

}
