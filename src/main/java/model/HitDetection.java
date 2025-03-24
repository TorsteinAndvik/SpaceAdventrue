package model;

import java.util.ArrayList;
import java.util.List;

import model.Globals.Collideable;
import model.Globals.DamageDealer;
import model.Globals.Damageable;

public class HitDetection {

    private List<Collideable> colliders = new ArrayList<>();

    public void addCollider(Collideable c) {
        colliders.add(c);
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
                        if (collA instanceof DamageDealer && collB instanceof Damageable) {
                            ((DamageDealer) collA).dealDamage(
                                    (Damageable) (collB));
                        }

                        if (collB instanceof DamageDealer && collA instanceof Damageable) {
                            ((DamageDealer) collB).dealDamage(
                                    (Damageable) (collA));
                        }
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
