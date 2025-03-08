package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;
import inf112.skeleton.model.Globals.Repairable;
//import java.util.ArrayList;
//import java.util.List;

public abstract class SpaceShip extends SpaceBody implements DamageDealer, Damageable, Repairable {
  private int maxHitPoints;
  private int hitPoints;
//  private List<Gun> gunList;


  public SpaceShip(String name, String description, CharacterType characterType, int maxHealthPoints, float x, float y, float angle, float radius) {
    this(name, description, characterType, maxHealthPoints, maxHealthPoints, x, y, angle, radius);
  }

  public SpaceShip(String name, String description, CharacterType characterType, int maxHitPoints, int hitPoints, float x, float y, float angle, float radius) {
    super(name, description, characterType, 0, 0, 0, radius);
    if (hitPoints <= 0 || maxHitPoints <= 0) {
      throw new IllegalArgumentException("Hit points must be positive on ship creation");
    }
    if (maxHitPoints < hitPoints) {
      throw new IllegalArgumentException("Hit points can't be higher than max hit points");
    }
    this.hitPoints = hitPoints;
    this.maxHitPoints = maxHitPoints;
//    this.gunList = new ArrayList<>();
  }

  @Override
  public int getMaxHitPoints() {
    return maxHitPoints;
  }
//  public void addGun(Gun gun) {
//    gunList.add(gun);
//  }

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
//  public Iterable<Gun> guns() {
//    return gunList;
//  }
//  public abstract void attack(SpaceBodyView target);


}
