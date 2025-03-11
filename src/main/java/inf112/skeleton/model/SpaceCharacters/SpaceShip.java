package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;
import inf112.skeleton.model.Globals.Repairable;
import inf112.skeleton.model.ShipComponents.Components.ShipStructure;

public abstract class SpaceShip extends SpaceBody implements DamageDealer, Damageable, Repairable {
  private int maxHitPoints;
  private int hitPoints;
  private ShipStructure shipStructure;
//  private List<Gun> gunList;


  public SpaceShip(ShipStructure shipStructure, String name, String description, CharacterType characterType, float x, float y, int maxHealthPoints, float angle, float radius) {
    this(shipStructure, name, description, characterType, x, y, maxHealthPoints, maxHealthPoints, angle, radius);
  }

  public SpaceShip(ShipStructure shipStructure, String name, String description, CharacterType characterType, float x, float y, int maxHitPoints, int hitPoints, float angle, float radius) {
    super(name, description, characterType, x, y, angle, radius);
    if (hitPoints <= 0 || maxHitPoints <= 0) {
      throw new IllegalArgumentException("Hit points must be positive on ship creation");
    }
    if (maxHitPoints < hitPoints) {
      throw new IllegalArgumentException("Hit points can't be higher than max hit points");
    }
    this.hitPoints = hitPoints;
    this.maxHitPoints = maxHitPoints;
    this.shipStructure = shipStructure;
//    this.gunList = new ArrayList<>();
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
