package inf112.skeleton.model;

import java.util.ArrayList;
import java.util.List;

import inf112.skeleton.model.Globals.Collideable;
import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;

public class HitDetection {

    private List<Collideable> colliders = new ArrayList<>();

    public void addCollider(Collideable c){
        colliders.add(c);
    }

    public boolean objectProximity(Collideable c1, Collideable c2){
        // TODO implementer
        return true;
    }


    public void checkCollisions() {
        for (int i = 0; i < colliders.size(); i++) {
            for (int j = i + 1; j < colliders.size(); j++) {
                if (objectProximity(colliders.get(i),colliders.get(j))){
                    if (colliders.get(i).checkCollision(colliders.get(j))) {
                        if (colliders.get(i) instanceof DamageDealer && colliders.get(j) instanceof Damageable){
                            ((DamageDealer)colliders.get(i)).dealDamage((Damageable)(colliders.get(j)));
                        }
                    }
                }
            }
        }
    }




}
