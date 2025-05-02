package model;

import java.util.LinkedList;
import java.util.List;

import grid.CellPosition;
import grid.GridCell;
import model.Globals.Collidable;
import model.ShipComponents.Components.Fuselage;
import model.SpaceCharacters.Bullet;
import model.SpaceCharacters.Ships.SpaceShip;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;

public class HitDetection {

    protected final LinkedList<Collidable> colliders = new LinkedList<>();
    private final SpaceGameModel model;

    public HitDetection(SpaceGameModel model) {
        this.model = model;
    }

    /**
     * Adds a {@link Collidable} object to the front of the collider list.
     * 
     * @param collideable The {@code Collidable} object to be added.
     */
    public void addCollider(Collidable collideable) {
        colliders.addFirst(collideable);
    }

    /**
     * Adds a list of {@link Collidable} objects to the collider list.
     *
     * @param colliders A list of objects that implement the {@code Collidable}
     *                  interface to be added.
     */
    public void addColliders(List<? extends Collidable> colliders) {
        this.colliders.addAll(colliders);
    }

    /**
     * Removes the specified {@link Collidable} object from the collider list.
     *
     * @param c The {@code Collidable} object to be removed.
     */
    public void removeCollider(Collidable c) {
        colliders.remove(c);
    }

    private boolean objectProximity(Collidable c1, Collidable c2) {
        if (c1 instanceof SpaceShip ship1) {
            if (c2 instanceof SpaceShip ship2) {
                float dx = ship1.getAbsoluteCenterOfMass().x() - ship2.getAbsoluteCenterOfMass().x();
                float dy = ship1.getAbsoluteCenterOfMass().y() - ship2.getAbsoluteCenterOfMass().y();
                float distance = SpaceCalculator.distance(dx, dy);

                return distance < ship1.getProximityRadius() + ship2.getProximityRadius();

            } else {
                float dx = ship1.getAbsoluteCenterOfMass().x() - c2.getX();
                float dy = ship1.getAbsoluteCenterOfMass().y() - c2.getY();
                float distance = SpaceCalculator.distance(dx, dy);

                return distance < ship1.getProximityRadius() + c2.getRadius();
            }
        }

        if (c2 instanceof SpaceShip ship2) {
            float dx = c1.getX() - ship2.getAbsoluteCenterOfMass().x();
            float dy = c1.getY() - ship2.getAbsoluteCenterOfMass().y();
            float distance = SpaceCalculator.distance(dx, dy);

            return distance < c1.getRadius() + ship2.getProximityRadius();
        }

        return SpaceCalculator.distance(c1.getX() - c2.getX(), c1.getY() - c2.getY()) < c1.getRadius() + c2.getRadius();
    }

    /**
     * Iterates through all pairs of registered {@link Collidable} objects and
     * checks
     * for potential collisions based on their proximity.
     * <p>
     * If two objects are within proximity of each other (as determined by
     * {@link #objectProximity}),
     * the {@link #checkCollision} method is called to handle the collision logic.
     */
    public void checkCollisions() {
        for (int i = 0; i < colliders.size(); i++) {
            Collidable collA = colliders.get(i);
            for (int j = i + 1; j < colliders.size(); j++) {
                Collidable collB = colliders.get(j);
                if (objectProximity(collA, collB)) {
                    checkCollision(collA, collB);
                }
            }
        }
    }

    private boolean checkCollision(Collidable target1, Collidable target2) {
        if (target1 instanceof SpaceShip ship1 && target2 instanceof SpaceShip ship2) {
            return doubleShipCollision(ship1, ship2);
        }
        if (target1 instanceof SpaceShip ship1) {
            return shipCollision(ship1, target2);
        }
        if (target2 instanceof SpaceShip ship2) {
            return shipCollision(ship2, target1);
        }

        if (SpaceCalculator.collisionCalculator(target1, target2)) {
            model.handleCollision(target1, target2);
            return true;
        }
        return false;
    }

    private boolean shipCollision(SpaceShip ship, Collidable c) {
        for (GridCell<Fuselage> gridCell : ship.getShipStructure().getGridCopy()) {
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
        model.handleShipProximity(shipA, shipB); // ships are close
        for (GridCell<Fuselage> gridCellA : shipA.getShipStructure().getGridCopy()) {
            if (gridCellA.value() == null) {
                continue;
            }

            CellPosition cellA = gridCellA.pos();

            FloatPair pointA = SpaceCalculator.rotatePoint(cellA.col(), cellA.row(), shipA.getRelativeCenterOfMass(),
                    shipA.getAbsoluteCenterOfMass(), shipA.getRotationAngle());

            for (GridCell<Fuselage> gridCellB : shipB.getShipStructure().getGridCopy()) {
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

    /**
     * Holds the rules for what counts as friendly fire.
     * <p>
     * Checks if two collidables has the same source, or if one emerged from the
     * other.
     *
     * @param A The first collidable
     * @param B The second collidable
     * @return false if the interaction is friendly fire, true otherwise.
     */
    public static boolean isFriendlyFire(Collidable A, Collidable B) {
        if ((A instanceof SpaceShip ship) && (B instanceof Bullet bullet)) {
            return bullet.getSourceID().equals(ship.getID());

        } else if ((B instanceof SpaceShip ship) && (A instanceof Bullet bullet)) {
            return bullet.getSourceID().equals(ship.getID());

        } else if ((A instanceof Bullet bulletA) && (B instanceof Bullet bulletB)) {
            return bulletA.getSourceID().equals(bulletB.getSourceID());
        }
        return false;
    }
}
