package model.Globals;

public interface DamageDealer {

    /**
     * Deals damage to a damageable target
     *
     * @param target the target to deal damage to.
     */
    void dealDamage(Damageable target, int damage);

    /**
     * @return The amount of damage dealt by the {@code DamageDealer}
     */
    int getDamage();

}
