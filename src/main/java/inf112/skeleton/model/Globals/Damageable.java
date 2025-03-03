package inf112.skeleton.model.Globals;

public interface Damageable {
  /**
   * A damageable object takes damage equal to the amount of damagePoints
   *
   * @param hitPoints the amount of damage to take
   */
  void takeDamage(int hitPoints);

  /**
   * @return the amount of health points for a damageable object
   */
  int getHitPoints();

  /**
   * Checks if a damageable object is destroyed.
   *
   * @return true if destroyed, else false.
   */
  boolean isDestroyed();

}

