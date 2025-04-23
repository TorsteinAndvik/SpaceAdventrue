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
        // lerp the rotation angle towards the player
        float angle = SpaceCalculator.angleBetweenPoints(
                ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass());

        float newAngle;
        if (ship.getRotationAngle() <= angle + 180f) {
            newAngle = SpaceCalculator.lerp1D(ship.getRotationAngle(), angle + 90f, 1.75f * delta);
        } else {
            newAngle = SpaceCalculator.lerp1D(ship.getRotationAngle() - 360f, angle + 90f, 1.75f * delta);
        }

        ship.setRotation(newAngle);

        // lerp the position towards the player, at a "safe" hover-distance
        FloatPair target = SpaceCalculator.getPointAtDistance(ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass(), hoverDistance());

        FloatPair newPos = SpaceCalculator.lerp2D(ship.getAbsoluteCenterOfMass(), target, 3f * delta);

        FloatPair deltaPos = new FloatPair(newPos.x() - ship.getAbsoluteCenterOfMass().x(),
                newPos.y() - ship.getAbsoluteCenterOfMass().y());

        ship.translate(deltaPos);

        // shooting logic
        shoot(inFiringRange());
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
        return distance < 1.2f * hoverDistance();
    }
}
