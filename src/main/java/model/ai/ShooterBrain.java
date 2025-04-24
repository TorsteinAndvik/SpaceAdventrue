package model.ai;

public interface ShooterBrain {

    /**
     * Sets the ship's turret to shoot or not.
     */
    void shoot(boolean setToShoot);

    /**
     * @return whether the ship is in firing range or not.
     */
    boolean inFiringRange();
}
