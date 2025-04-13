package model.ShipComponents;

public enum UpgradeType {
    FUSELAGE,
    THRUSTER,
    TURRET,
    SHIELD;

    public static UpgradeType getUpgradeType(String name) {
        try {
            return UpgradeType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid upgrade type: " + name);
        }
    }
}
