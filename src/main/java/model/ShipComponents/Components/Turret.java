package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.stats.Stat;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;

public class Turret extends UpdateableShipUpgrade {

    private float fireRate = 1f;
    private float timeSinceLastShot = fireRate; // able to fire immediately
    private boolean isSetToShoot;

    public Turret() {
        this(UpgradeStage.ZERO);
    }

    public Turret(UpgradeStage stage) {
        super("Turret", "Fires lasers at enemies and asteroids", UpgradeType.TURRET, stage);
    }

    /**
     * @return a <code>FloatPair</code> giving the x / y offset between the
     *         centerpoint of <code>this</code> and the tip of the turret's barrel.
     */
    public static FloatPair turretBarrelLocation() {
        return new FloatPair(0f, 0.375f);
    }

    @Override
    protected void setupStatModifier() {
        statModifier.setModifier(Stat.MASS, PhysicsParameters.shipUpgradeMass);
        statModifier.setModifier(Stat.RESOURCE_VALUE, 5);
    }

    @Override
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canShoot() {
        return timeSinceLastShot >= fireRate;
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
