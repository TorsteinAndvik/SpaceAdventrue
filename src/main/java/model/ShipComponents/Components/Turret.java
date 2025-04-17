package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.stats.Stat;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;

public class Turret extends UpdateableShipUpgrade {

    private float timeSinceLastShot; // able to fire immediately
    private boolean isSetToShoot;

    public Turret() {
        this(UpgradeStage.ZERO);
    }

    public Turret(UpgradeStage stage) {
        super("Turret", "Fires lasers at enemies and asteroids", UpgradeType.TURRET, stage);

        // simple workaround to able to fire immediately
        timeSinceLastShot = statModifier.getModifiers().get(Stat.FIRE_RATE).floatValue();
    }

    /**
     * @return a <code>FloatPair</code> giving the x / y offset between the
     *         centerpoint of <code>this</code> and the tip of the turret's barrel.
     */
    public static FloatPair turretBarrelLocation() {
        return new FloatPair(0f, 0.375f);
    }

    @Override
    protected void setupStatModifiers() {
        statModifier.setModifier(Stat.MASS, PhysicsParameters.shipUpgradeMass);
        statModifier.setModifier(Stat.FIRE_RATE, 1f);
        statModifier.setModifier(Stat.RESOURCE_VALUE, 5);

        // Upgrades:
        upgradeModifier.setModifier(Stat.FIRE_RATE, 0.2f);
        upgradeModifier.setModifier(Stat.RESOURCE_VALUE, 4);
    }

    @Override
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canShoot() { // 1/x means it fires 1 shot every x seconds
        return timeSinceLastShot >= 1f / statModifier.getModifiers().get(Stat.FIRE_RATE).floatValue();
    }

    public boolean shoot() {
        if (isShooting()) {
            hasShot();
            return true;
        }
        return false;
    }

    public boolean isShooting() {
        return isSetToShoot && canShoot();
    }

    public void setToShoot(boolean setToShoot) {
        isSetToShoot = setToShoot;
    }

    public void hasShot() {
        timeSinceLastShot = 0f;
        setToShoot(false);
    }
}
