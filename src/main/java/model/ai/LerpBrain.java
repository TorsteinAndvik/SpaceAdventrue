package model.ai;

import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;

public class LerpBrain extends Brain implements ShooterBrain {

    public LerpBrain(EnemyShip ship, Player player) {
        super(ship, player);
    }

    @Override
    public void update(float delta) {
        ship.setRotation(newAngle(delta));
        ship.translate(newPositionDelta(delta));
        shoot(inFiringRange());
    }

    protected float newAngle(float delta) {
        // lerp the rotation angle towards the player
        float angle = SpaceCalculator.angleBetweenPoints(
                ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass());

        float newAngle;
        float lerpAlpha = 1.75f * delta;
        if (ship.getRotationAngle() <= angle + 180f) {
            newAngle = SpaceCalculator.lerp1D(ship.getRotationAngle(), angle + 90f, lerpAlpha);
        } else {
            newAngle = SpaceCalculator.lerp1D(ship.getRotationAngle() - 360f, angle + 90f, lerpAlpha);
        }
        return newAngle;
    }

    protected FloatPair newPositionDelta(float delta) {
        // lerp the position towards the player, at a "safe" hover-distance
        FloatPair target = SpaceCalculator.getPointAtDistance(ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass(), hoverDistance());

        FloatPair newPos = SpaceCalculator.lerp2D(ship.getAbsoluteCenterOfMass(), target, 3f * delta);

        FloatPair deltaPos = new FloatPair(newPos.x() - ship.getAbsoluteCenterOfMass().x(),
                newPos.y() - ship.getAbsoluteCenterOfMass().y());

        return deltaPos;
    }

    protected float hoverDistance() {
        return ship.getProximityRadius() + ship.getProximityRadius();
    }

    @Override
    public void shoot(boolean shoot) {
        ship.setToShoot(shoot);
    }

    @Override
    public boolean inFiringRange() {
        float distance = SpaceCalculator.distance(ship.getAbsoluteCenterOfMass(), player.getAbsoluteCenterOfMass());
        return distance < 1.35f * hoverDistance();
    }
}
