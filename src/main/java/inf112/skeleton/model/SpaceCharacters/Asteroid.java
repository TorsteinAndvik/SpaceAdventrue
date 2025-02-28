package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;

public class Asteroid extends SpaceBody implements Damageable, DamageDealer {
  private int healthPoints;

  public Asteroid(String name, String description, int x, int y, int healthPoints, int mass, int speed, float angle) {
    super(name, description, x, y, mass, speed, angle);
    this.healthPoints = healthPoints;
  }

  /**
   * Get the health points of an Asteroid object.
   */
  @Override
  public int getHealthPoints() {
    return healthPoints;
  }

  @Override
  public void takeDamage(int damagePoints) {
    healthPoints -= damagePoints;
  }

  @Override
  public void dealDamage(Damageable target) {
    target.takeDamage(getHealthPoints());
  }


}
