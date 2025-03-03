package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;

public class SpaceShip extends SpaceBody implements DamageDealer, Damageable {
  private int healthPoints;

  public SpaceShip(String name, String description, int healthPoints, int x, int y, int mass, int speed, float angle) {
    super(name, description, x, y, mass, speed, angle);
    this.healthPoints = healthPoints;
  }

  @Override
  public void dealDamage(Damageable target) {
  }

  @Override
  public void takeDamage(int damagePoints) {
    if (damagePoints < 0) {
      throw new IllegalArgumentException("Damage can't be negative");
    }
    healthPoints -= damagePoints;
  }

  @Override
  public int getHealthPoints() {
    return healthPoints;
  }

  @Override
  public boolean isDestroyed() {
    return false;
  }
}
