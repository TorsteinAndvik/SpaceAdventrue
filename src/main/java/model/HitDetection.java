package model;

import java.util.LinkedList;
import java.util.List;

import model.Globals.Collideable;
import model.SpaceCharacters.SpaceShip;

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
        // TODO implementer
        return true;
    }

    public void checkCollisions() {
        for (int i = 0; i < colliders.size(); i++) {
            Collideable collA = colliders.get(i);
            for (int j = i + 1; j < colliders.size(); j++) {
                Collideable collB = colliders.get(j);

                if (objectProximity(collA, collB)) {
                    if (checkCollision(collA, collB)) {
                        model.handleCollision(collA, collB);
                    }
                }
            }
        }
    }

    private boolean checkCollision(Collideable target1, Collideable target2) {
        float t1X, t1Y, t2X, t2Y;
        if (target1 instanceof SpaceShip) {
            t1X = ((SpaceShip) target1).getAbsoluteCenter().x();
            t1Y = ((SpaceShip) target1).getAbsoluteCenter().y();
        } else {
            t1X = target1.getX();
            t1Y = target1.getY();
        }

        if (target2 instanceof SpaceShip) {
            t2X = ((SpaceShip) target2).getAbsoluteCenter().x();
            t2Y = ((SpaceShip) target2).getAbsoluteCenter().y();
        } else {
            t2X = target2.getX();
            t2Y = target2.getY();
        }

        float dx = t1X - t2X;
        float dy = t1Y - t2Y;
        float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        return (distance < target1.getRadius() + target2.getRadius());
    }
}
