package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;

public abstract class Projectile extends SpaceBody implements Damageable, DamageDealer {

  private int hitPoints;

  private final int multiplier = 1;

  public Projectile(String name, String description, int x, int y, int healthPoints, int mass, int speed, float angle) {
    super(name, description, x, y, mass, speed, angle);
    this.hitPoints = healthPoints;
  }

  /**
   * Get the health points of a Projectile object.
   */
  @Override
  public int getHitPoints() {
    return this.hitPoints;
  }

  @Override
  public boolean isDestroyed() {
    return hitPoints <= 0;
  }

  @Override
  public void takeDamage(int hitPoints) {
    this.hitPoints = Math.max(this.hitPoints - hitPoints, 0);
  }

  @Override
  public void dealDamage(Damageable target) {
    target.takeDamage(this.multiplier);
    this.takeDamage(this.getHitPoints());
  }
  //TODO Damage kan ikke være basert på HP (hvis asteroider kolliderer skal begge ødelegges)


}
