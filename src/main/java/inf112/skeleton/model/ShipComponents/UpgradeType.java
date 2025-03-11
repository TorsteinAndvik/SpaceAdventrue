package inf112.skeleton.model.ShipComponents;

public enum UpgradeType {
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
