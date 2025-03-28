package model.constants;

public class PhysicsParameters {

    public static final float fuselageMass = 1f;

    public static final float shipUpgradeMass = 0.5f;

    // TODO: space ships should calculate a max velocity and acceleration according
    // to ShipStructure
    public static final float maxVelocityLongitudonal = 5f;

    public static final float maxVelocityRotational = 180f;

    // if equal to maxVelocityLongitudonal, max speed is reached in 1s from 0m/s
    public static final float accelerationLimitLongitudonal = 10f;

    // if equal to maxVelocityRotational, max speed is reached in 1s from 0rpm
    public static final float accelerationLimitRotational = 360f;
}
