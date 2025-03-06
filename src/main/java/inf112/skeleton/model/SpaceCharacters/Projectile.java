package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.DamageDealer;
import inf112.skeleton.model.Globals.Damageable;

public abstract class Projectile extends SpaceBody implements Damageable, DamageDealer {

  private int hitPoints;
  private int maxHitPoints;

  private final int multiplier = 1;

  public Projectile(String name, String description, float x, float y, int hitPoints, int mass, float angle, int radius) {
    super(name, description, x, y, mass, angle, radius);
    this.hitPoints = hitPoints;
    this.maxHitPoints = hitPoints;
  }

  /**
   * Get the health points of a Projectile object.
   */
  @Override
  public int getHitPoints() {
    return this.hitPoints;
  }

  @Override
  public int getMaxHitPoints() {
    return this.maxHitPoints;
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
    int targetHP = target.getHitPoints();
    target.takeDamage(this.multiplier * this.hitPoints);
    this.takeDamage(targetHP);
    //Forslag: Lage en Crash metode i stedet for å "take damage" i "deal damage"-metoden.
  }
  //TODO Damage kan ikke være basert på HP (hvis asteroider kolliderer skal begge ødelegges)
  //Mitt forslag er hvis to asteroider kolloderer, vil den som har mest HP "vinne".

}
