package model.ai;

import model.SpaceCharacters.EnemyShip;
import model.SpaceCharacters.Player;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;

public class LerpBrain extends Brain implements ShooterBrain {

    public LerpBrain(EnemyShip ship, Player player) {
        super(ship, player);
    }

    @Override
    public void update(float delta) {
        float angle = SpaceCalculator.angleBetweenPoints(
                ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass());
        ship.setRotation(angle + 90f);

        FloatPair target = SpaceCalculator.getPointAtDistance(ship.getAbsoluteCenterOfMass(),
                player.getAbsoluteCenterOfMass(), hoverDistance());

        FloatPair newPos = SpaceCalculator.lerp2D(ship.getAbsoluteCenterOfMass(), target, 3f * delta);

        FloatPair deltaPos = new FloatPair(newPos.x() - ship.getAbsoluteCenterOfMass().x(),
                newPos.y() - ship.getAbsoluteCenterOfMass().y());

        ship.translate(deltaPos);

        if (inFiringRange()) {
            shoot();
        }
    }

    private float hoverDistance() {
        return 1.25f * (ship.getRadius() + player.getRadius());
    }

    @Override
    public void shoot() {
        ship.setIsShooting(true);
    }

    @Override
    public boolean inFiringRange() {
        float distance = SpaceCalculator.distance(ship.getAbsoluteCenterOfMass(), player.getAbsoluteCenterOfMass());
        return distance < 1.5f * hoverDistance();
    }
}
