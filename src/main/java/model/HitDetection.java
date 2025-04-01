package model;

import java.util.LinkedList;
import java.util.List;

import grid.CellPosition;
import grid.GridCell;
import model.Globals.Collideable;
import model.ShipComponents.Components.Fuselage;
import model.SpaceCharacters.SpaceShip;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
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

        return SpaceCalculator.distance(c1.getX() - c2.getX(), c1.getY() - c2.getY()) < c1.getRadius() + c2.getRadius();
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

            float x = (float) cell.col();
            float y = (float) cell.row();
            FloatPair point = SpaceCalculator.rotatePoint(x, y, ship.getRelativeCenterOfMass(),
                    ship.getAbsoluteCenterOfMass(), ship.getRotationAngle());

            float d = SpaceCalculator.distance(point.x() - c.getX(), point.y() - c.getY());
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

            FloatPair pointA = SpaceCalculator.rotatePoint(cellA.col(), cellA.row(), shipA.getRelativeCenterOfMass(),
                    shipA.getAbsoluteCenterOfMass(), shipA.getRotationAngle());

            for (GridCell<Fuselage> gridCellB : shipB.getShipStructure().getGrid()) {
                if (gridCellB.value() == null) {
                    continue;
                }

                CellPosition cellB = gridCellB.pos();

                FloatPair pointB = SpaceCalculator.rotatePoint(cellB.col(), cellB.row(),
                        shipB.getRelativeCenterOfMass(), shipB.getAbsoluteCenterOfMass(), shipB.getRotationAngle());

                float d = SpaceCalculator.distance(pointA.x() - pointB.x(), pointA.y() - pointB.y());
                if (d < 2 * PhysicsParameters.fuselageRadius) {
                    model.handleCollision(shipA, shipB);
                    return true;
                }
            }
        }
        return false;
    }
}
