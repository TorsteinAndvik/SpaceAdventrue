package model.ShipComponents.Components.stats;

public enum Stat {

    MASS(false, "Mass", false),
    RESOURCE_VALUE(true, "Value", true),
    HEALTH_VALUE(true, "Health", true),
    HEALTH_REGENERATION_RATE(false, "Health regeneration", true),
    MAX_SPEED(false, "Max speed", true),
    ACCELERATION_FORCE(false, "Acceleration force", true),
    FIRE_RATE(false, "Shots per second", true);

    public final boolean intBased;
    public final String name;
    public final boolean positiveIsBeneficial;

    private Stat(boolean intBased, String name, boolean positiveIsBeneficial) {
        this.intBased = intBased;
        this.name = name;
        this.positiveIsBeneficial = positiveIsBeneficial;
    }

    public String toString() {
        return this.name;
    }
}
