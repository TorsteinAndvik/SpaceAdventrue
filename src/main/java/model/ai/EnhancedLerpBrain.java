package model.ai;

import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;
import model.SpaceCharacters.Ships.SpaceShip;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;

public class EnhancedLerpBrain extends LerpBrain {

    private boolean nearCollision = false;
    private FloatPair nearCollisionPos = null;
    private float nearCollisionProximity = 0f;
    private final float nearCollisionScalar = 4f;

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
        FloatPair deltaPos = avoidNearCollision(delta);
        float maxDistance = ship.maxSpeed() * delta;
        float distance = SpaceCalculator.distance(deltaPos.x(), deltaPos.y());

        if (distance <= maxDistance) {
            return deltaPos;
        }

        // need to limit distance according to max speed
        float distanceFactor = maxDistance / distance;
        return new FloatPair(deltaPos.x() * distanceFactor, deltaPos.y() * distanceFactor);
    }

    private FloatPair avoidNearCollision(float delta) {
        FloatPair deltaPos = super.newPositionDelta(delta);
        if (!nearCollision) {
            return deltaPos;
        }
        nearCollision = false;

        // direction from brain's ship to a nearby ship
        FloatPair direction = new FloatPair(ship.getAbsoluteCenterOfMass().x() - nearCollisionPos.x(),
                ship.getAbsoluteCenterOfMass().y() - nearCollisionPos.y());

        float norm = SpaceCalculator.distance(direction.x(), direction.y());
        float distanceToOtherShip = SpaceCalculator.distance(ship.getAbsoluteCenterOfMass(), nearCollisionPos);

        // amount of repulsion from other ship
        float scaleFactor = nearCollisionScalar * delta
                / ((float) Math.pow(distanceToOtherShip - nearCollisionProximity, 2f) * norm);

        // wanted escape delta
        FloatPair target = new FloatPair(scaleFactor * direction.x(),
                scaleFactor * direction.y());

        // lerp the deltaPos towards the target escape delta
        FloatPair collisionAvoidance = SpaceCalculator.lerp2D(deltaPos,
                new FloatPair(deltaPos.x() + target.x(), deltaPos.y() + target.y()), delta);

        return collisionAvoidance;
    }

    // if too close to a ship, try to avoid collision
    public void nearCollision(SpaceShip otherShip) {
        float newProximity = 0.75f * (ship.getProximityRadius() + otherShip.getProximityRadius());
        if (nearCollision) {// already near collision, set to avoid the closest threat
            if (newProximity > nearCollisionProximity) {
                return;
            }
        }

        nearCollision = true;
        nearCollisionPos = otherShip.getAbsoluteCenterOfMass();
        nearCollisionProximity = newProximity;
    }
}
