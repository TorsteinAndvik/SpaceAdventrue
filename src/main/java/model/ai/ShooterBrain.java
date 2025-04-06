package model.ai;

public interface ShooterBrain {

    /**
     * Fires the ship's turrets.
     */
    void shoot();

    /**
     * @return whether the ship is in firing range or not.
     */
    boolean inFiringRange();
}
