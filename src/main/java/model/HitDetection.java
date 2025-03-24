package model;

import java.util.LinkedList;
import java.util.List;

import model.Globals.Collideable;

public class HitDetection {

    private LinkedList<Collideable> colliders = new LinkedList<>();
    private final SpaceGameModel model;

    public HitDetection(SpaceGameModel model) {
        this.model = model;
    }

    public void addCollider(Collideable c) {
        colliders.addFirst(c);
    }

    public void addColliders(List<Collideable> colliders) {
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
        float dx = target1.getX() - target2.getX();
        float dy = target1.getY() - target2.getY();
        float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        return (distance < target1.getRadius() + target2.getRadius());
    }
}
