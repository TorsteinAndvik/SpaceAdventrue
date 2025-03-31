package model;

import java.util.LinkedList;
import java.util.List;

import grid.CellPosition;
import grid.GridCell;
import model.Globals.Collideable;
import model.ShipComponents.Components.Fuselage;
import model.SpaceCharacters.SpaceShip;
import model.constants.PhysicsParameters;
import model.utils.SpaceCalculator;

public class HitDetection {

    private LinkedList<Collideable> colliders = new LinkedList<>();
    private final SpaceGameModel model;

    public HitDetection(SpaceGameModel model) {
        this.model = model;
    }

    public void addCollider(Collideable c) {
        colliders.addFirst(c);
    }

    public void addColliders(List<? extends Collideable> colliders) {
        this.colliders.addAll(colliders);
    }

    public void removeCollider(Collideable c) {
        colliders.remove(c);
    }

    public boolean objectProximity(Collideable c1, Collideable c2) {
        if (c1 instanceof SpaceShip) {
            SpaceShip ship1 = (SpaceShip) c1;

            if (c2 instanceof SpaceShip) {
                SpaceShip ship2 = (SpaceShip) c2;
                float dx = ship1.getAbsoluteCenter().x() - ship2.getAbsoluteCenter().x();
                float dy = ship1.getAbsoluteCenter().y() - ship2.getAbsoluteCenter().y();
                float distance = SpaceCalculator.distance(dx, dy);

                return distance < ship1.getRadius() + ship2.getRadius();

            } else {
                float dx = ship1.getAbsoluteCenter().x() - c2.getX();
                float dy = ship1.getAbsoluteCenter().y() - c2.getY();
                float distance = SpaceCalculator.distance(dx, dy);

                return distance < ship1.getRadius() + c2.getRadius();
            }
        }

        if (c2 instanceof SpaceShip) {
            SpaceShip ship2 = (SpaceShip) c2;
            float dx = c1.getX() - ship2.getAbsoluteCenter().x();
            float dy = c1.getY() - ship2.getAbsoluteCenter().y();
            float distance = SpaceCalculator.distance(dx, dy);

            return distance < c1.getRadius() + ship2.getRadius();
        }

        return SpaceCalculator.distance(c1.getX() - c2.getX(), c1.getY() - c2.getY())
            < c1.getRadius() + c2.getRadius();
    }

    public void checkCollisions() {
        for (int i = 0; i < colliders.size(); i++) {
            Collideable collA = colliders.get(i);
            for (int j = i + 1; j < colliders.size(); j++) {
                Collideable collB = colliders.get(j);
                if (objectProximity(collA, collB)) {
                    checkCollision(collA, collB);
                }
            }
        }
    }

    private boolean checkCollision(Collideable target1, Collideable target2) {
        if (target1 instanceof SpaceShip) {
            if (target2 instanceof SpaceShip) {
                return doubleShipCollision((SpaceShip) target1, (SpaceShip) target2);
            }
            return shipCollision((SpaceShip) target1, target2);
        } else if (target2 instanceof SpaceShip) {
            return shipCollision((SpaceShip) target2, target1);
        }

        float dx = target1.getX() - target2.getX();
        float dy = target1.getY() - target2.getY();
        float distance = SpaceCalculator.distance(dx, dy);

        if (distance < target1.getRadius() + target2.getRadius()) {
            model.handleCollision(target1, target2);
            return true;
        }
        return false;
    }

    private boolean shipCollision(SpaceShip ship, Collideable c) {
        for (GridCell<Fuselage> gridCell : ship.getShipStructure().getGrid()) {
            if (gridCell.value() == null) {
                continue;
            }

            CellPosition cell = gridCell.pos();

            float x0 = (float) cell.col() - ship.getRelativeCenterOfMass().x();
            float y0 = (float) cell.row() - ship.getRelativeCenterOfMass().y();
            float r = SpaceCalculator.distance(x0, y0);

            float offsetAngle = (float) Math.toDegrees(Math.atan2(y0, x0));

            float x1 = r * (float) Math.cos(Math.toRadians(ship.getRotationAngle() + offsetAngle));
            float y1 = r * (float) Math.sin(Math.toRadians(ship.getRotationAngle() + offsetAngle));

            float x2 = ship.getAbsoluteCenterOfMass().x() + x1;
            float y2 = ship.getAbsoluteCenterOfMass().y() + y1;

            float d = SpaceCalculator.distance(x2 - c.getX(), y2 - c.getY());
            if (d < PhysicsParameters.fuselageRadius + c.getRadius()) {
                model.handleCollision(ship, c);
                return true;
            }
        }
        return false;
    }

    private boolean doubleShipCollision(SpaceShip shipA, SpaceShip shipB) {
        for (GridCell<Fuselage> gridCellA : shipA.getShipStructure().getGrid()) {
            if (gridCellA.value() == null) {
                continue;
            }

            CellPosition cellA = gridCellA.pos();

            float x0A = (float) cellA.col() - shipA.getRelativeCenterOfMass().x();
            float y0A = (float) cellA.row() - shipA.getRelativeCenterOfMass().y();
            float rA = SpaceCalculator.distance(x0A, y0A);

            float offsetAngleA = (float) Math.toDegrees(Math.atan2(y0A, x0A));

            float x1A =
                rA * (float) Math.cos(Math.toRadians(shipA.getRotationAngle() + offsetAngleA));
            float y1A =
                rA * (float) Math.sin(Math.toRadians(shipA.getRotationAngle() + offsetAngleA));

            float x2A = shipA.getAbsoluteCenterOfMass().x() + x1A;
            float y2A = shipA.getAbsoluteCenterOfMass().y() + y1A;

            for (GridCell<Fuselage> gridCellB : shipB.getShipStructure().getGrid()) {
                if (gridCellB.value() == null) {
                    continue;
                }

                CellPosition cellB = gridCellB.pos();

                float x0B = (float) cellB.col() - shipB.getRelativeCenterOfMass().x();
                float y0B = (float) cellB.row() - shipB.getRelativeCenterOfMass().y();
                float rB = SpaceCalculator.distance(x0B, y0B);

                float offsetAngleB = (float) Math.toDegrees(Math.atan2(y0B, x0B));

                float x1B =
                    rB * (float) Math.cos(Math.toRadians(shipB.getRotationAngle() + offsetAngleB));
                float y1B =
                    rB * (float) Math.sin(Math.toRadians(shipB.getRotationAngle() + offsetAngleB));

                float x2B = shipB.getAbsoluteCenterOfMass().x() + x1B;
                float y2B = shipB.getAbsoluteCenterOfMass().y() + y1B;

                float d = SpaceCalculator.distance(x2A - x2B, y2A - y2B);
                if (d < 2 * PhysicsParameters.fuselageRadius) {
                    model.handleCollision(shipA, shipB);
                    return true;
                }
            }
        }
        return false;
    }
}
