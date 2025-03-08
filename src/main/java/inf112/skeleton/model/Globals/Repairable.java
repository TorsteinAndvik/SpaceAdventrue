package inf112.skeleton.model.Globals;

public interface Repairable {
    /**
     * Repairs the damageable object an amount of hit points
     *
     * @param hitPoints the amount of hit points to repair
     */
    void repair(int hitPoints);
}
