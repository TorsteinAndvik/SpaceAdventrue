package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;

public class Projectile extends SpaceBody implements Damageable, DamageDealer {
  
  private int healthPoints;

  private int multiplier = 1;

  public Projectile(String name, String description, int x, int y, int healthPoints, int mass, int speed, float angle) {
    super(name, description, x, y, mass, speed, angle);
    this.healthPoints = healthPoints;
  }

  /**
   * Get the health points of an Projectile object.
   */
  @Override
  public int getHealthPoints() {
    return this.healthPoints;
  }

  @Override
  public void takeDamage(int damagePoints) {
    this.healthPoints = Math.max(this.healthPoints-damagePoints, 0);
  }

  @Override
  public void dealDamage(Damageable target) {
    target.takeDamage(this.multiplier);
    this.takeDamage(this.getHealthPoints());
  }
  //TODO Damage kan ikke være basert på HP (hvis asteroider kolliderer skal begge ødelegges)


}
