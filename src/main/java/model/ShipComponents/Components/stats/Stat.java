package model.ShipComponents.Components.stats;

public enum Stat {

    MASS(false),
    RESOURCE_VALUE(true),
    HEALTH_VALUE(true),
    HEALTH_REGENERATION_RATE(false),
    MAX_SPEED(false),
    ACCELERATION_FORCE(false),
    FIRE_RATE(false);

    public final boolean intBased;

    private Stat(boolean intBased) {
        this.intBased = intBased;
    }
}
