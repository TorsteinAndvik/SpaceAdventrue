package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;
import inf112.skeleton.model.Globals.Repairable;
//import java.util.ArrayList;
//import java.util.List;
import inf112.skeleton.model.ShipComponents.Components.ShipStructure;

public abstract class SpaceShip extends SpaceBody implements DamageDealer, Damageable, Repairable {
    private int maxHitPoints;
    private int hitPoints;
    private ShipStructure shipStructure;
    // private List<Gun> gunList;

    public SpaceShip(ShipStructure shipStructure, String name, String description, int maxHealthPoints, int x, int y,
            int mass, int speed, float angle, int radius) {
        this(shipStructure, name, description, maxHealthPoints, maxHealthPoints, x, y, mass, speed, angle, radius);
    }

    public SpaceShip(ShipStructure shipStructure, String name, String description, int maxHitPoints, int hitPoints,
            int x, int y, int mass, int speed, float angle, int radius) {
        super(name, description, x, y, mass, speed, angle, radius);
        if (hitPoints <= 0 || maxHitPoints <= 0) {
            throw new IllegalArgumentException("Hit points must be positive on ship creation");
        }
        if (maxHitPoints < hitPoints) {
            throw new IllegalArgumentException("Hit points can't be higher than max hit points");
        }
        this.hitPoints = hitPoints;
        this.maxHitPoints = maxHitPoints;
        this.shipStructure = shipStructure;
        // this.gunList = new ArrayList<>();
    }

    public ShipStructure getShipStructure() {
        return shipStructure;
    }

    public boolean isPlayerShip() {
        return false;
    }

    @Override
    public int getMaxHitPoints() {
        return maxHitPoints;
    }
    // public void addGun(Gun gun) {
    // gunList.add(gun);
    // }

    @Override
    public void dealDamage(Damageable target) {
        // Damage dealt when ship hits damageable object
        target.takeDamage(getHitPoints());

    }

    @Override
    public void takeDamage(int hitPoints) {
        if (hitPoints < 0) {
            throw new IllegalArgumentException("Damage can't be negative");
        }
        this.hitPoints = Math.max(0, this.hitPoints - hitPoints);
    }

    @Override
    public int getHitPoints() {
        return hitPoints;
    }

    @Override
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void repair(int hitPoints) {
        if (isDestroyed()) {
            return;
        }
        this.hitPoints = Math.min(maxHitPoints, this.hitPoints + hitPoints);
    }
    // public Iterable<Gun> guns() {
    // return gunList;
    // }
    // public abstract void attack(SpaceBodyView target);

}
