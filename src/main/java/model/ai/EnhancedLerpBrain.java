package model.ai;

import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;

public class EnhancedLerpBrain extends LerpBrain {

    public EnhancedLerpBrain(EnemyShip ship, Player player) {
        super(ship, player);
    }

    @Override
    protected float newAngle(float deltaTime) {
        // lerp the rotation angle towards the player,
        // and apply rotational velocity limit if necessary
        float angle = SpaceCalculator.angleBetweenPoints(
                ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass());

        float maxDeltaAngle = ship.maxRotationalVelocity() * deltaTime;

        float newAngle;
        float deltaAngle;
        float diff;
        float lerpAlpha = 1.75f * deltaTime;
        if (ship.getRotationAngle() <= angle + 270f) {
            newAngle = SpaceCalculator.lerp1D(ship.getRotationAngle(), angle + 90f, lerpAlpha);
            deltaAngle = newAngle - ship.getRotationAngle();
        } else {
            newAngle = SpaceCalculator.lerp1D(ship.getRotationAngle() - 360f, angle + 90f, lerpAlpha);
            deltaAngle = newAngle - ship.getRotationAngle() + 360f;
        }

        diff = Math.abs(deltaAngle) - maxDeltaAngle;
        if (diff > 0) {
            newAngle -= Math.signum(deltaAngle) * diff;
        }

        return newAngle;
    }

    @Override
    protected FloatPair newPositionDelta(float delta) {
        FloatPair deltaPos = super.newPositionDelta(delta);
        float maxDistance = ship.maxSpeed() * delta;
        float distance = SpaceCalculator.distance(deltaPos.x(), deltaPos.y());

        if (distance <= maxDistance) {
            return deltaPos;
        }

        // need to limit distance according to max speed
        float distanceFactor = maxDistance / distance;
        return new FloatPair(deltaPos.x() * distanceFactor, deltaPos.y() * distanceFactor);
    }
}
