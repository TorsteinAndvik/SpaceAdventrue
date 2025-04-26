package model.ai;

import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;

public class EnhancedLerpBrain extends LerpBrain {

    public EnhancedLerpBrain(EnemyShip ship, Player player) {
        super(ship, player);
    }

    @Override
    protected float newAngle(float deltaTime) {
        float lerpAngle = super.newAngle(deltaTime);
        float deltaAngle = lerpAngle - ship.getRotationAngle();

        float angleScalar = angleScalar(deltaTime, deltaAngle);

        return angleScalar * lerpAngle;
    }

    private float angleScalar(float deltaTime, float deltaAngle) {

        return 1f;
    }
}
