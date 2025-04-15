package model.constants;

public class PhysicsParameters {

    public static final float fuselageMass = 1f;

    public static final float fuselageRadius = 0.5f;

    public static final float shipUpgradeMass = 0.5f;

    public static final float maxVelocityLongitudonal = 6f;

    public static final float maxVelocityRotational = 180f;

    public static final float laserVelocity = 12f;

    // if equal to maxVelocityLongitudonal, max speed is reached in 1s from 0m/s
    // if mass is equals to 1.
    public static final float accelerationForceLimitLongitudonal = 12f;

    // if equal to maxVelocityRotational, max speed is reached in 1s from 0rpm
    // if mass is equals to 1.
    public static final float accelerationForceLimitRotational = 360f;

    public static final float dampingLongitudonal = 12f;

    public static final float dampingRotational = 360f;
}
