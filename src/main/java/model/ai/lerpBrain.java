package model.ai;

import model.SpaceCharacters.EnemyShip;
import model.SpaceCharacters.Player;
import model.utils.SpaceCalculator;

public class LerpBrain extends Brain {

    public LerpBrain(EnemyShip ship, Player player) {
        super(ship, player);
    }

    @Override
    public void update(float delta) {
        float angle = SpaceCalculator.angleBetweenPoints(ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass());
        float newAngle = SpaceCalculator.lerp(0f, angle, delta);
        ship.setRotation(newAngle);
    }

    private float hoverDistance() {
        return 2f * (ship.getRadius() + player.getRadius());
    }

}
