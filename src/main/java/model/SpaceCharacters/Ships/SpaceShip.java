package model.SpaceCharacters.Ships;

import java.util.List;

import grid.CellPosition;
import model.Globals.DamageDealer;
import model.Globals.Damageable;
import model.Globals.Repairable;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.stats.Stat;
import model.ShipComponents.ShipStructure;
import model.SpaceCharacters.CharacterType;
import model.SpaceCharacters.SpaceBody;
import model.ViewableSpaceShip;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;
import view.Palette;
import view.bars.ShipHealthBar;

import java.util.UUID;

public abstract class SpaceShip extends SpaceBody implements DamageDealer, Damageable, Repairable, ViewableSpaceShip {

    private final String id;
    private int maxHitPoints;
    private int hitPoints;

    private boolean accelerateForward;
    private boolean accelerateBackward;
    private boolean accelerateClockwise;
    private boolean accelerateCounterClockwise;

    private ShipHealthBar healthBar;

    private final ShipStructure shipStructure;

    // shooting logic
    private boolean isShooting = false;
    protected float fireRate = 1f;
    protected float timeSinceLastShot = 0f;

    public SpaceShip(ShipStructure shipStructure, String name, String description,
            CharacterType characterType, float x, float y, float angle) {

        super(name, description, characterType, x, y, angle, 0f);

        this.shipStructure = shipStructure;
        this.maxHitPoints = shipStructure.getCombinedStatModifier().getModifiers().get(Stat.HEALTH_VALUE).intValue();
        this.hitPoints = maxHitPoints;

        this.shipStructure.normalize();
        radius = this.shipStructure.getRadius();

        id = UUID.randomUUID().toString();

        makeHealthBar();
    }

    private void makeHealthBar() {
        healthBar = new ShipHealthBar(this, new FloatPair(0, 0.2f));
        healthBar.setScale(0.9f, 0.13f);
        if (isPlayerShip()) {
            healthBar.setBarColor(Palette.MUTED_GREEN_LIGHT);
            healthBar.setBackgroundColor(Palette.MUTED_GREEN_DARK);
        } else {
            healthBar.setBarColor(Palette.MUTED_RED_LIGHT);
            healthBar.setBackgroundColor(Palette.MUTED_RED_DARK);
        }
        healthBar.setDrawOutline(true);
        healthBar.setNumNotches(4);
        healthBar.setHalfNotch(true);
    }

    public ShipHealthBar getHealthBar() {
        return healthBar;
    }

    public ShipStructure getShipStructure() {
        return shipStructure;
    }

    public boolean isPlayerShip() {
        return false;
    }

    public List<CellPosition> getUpgradeTypePositions(UpgradeType type) {
        return shipStructure.getUpgradeTypePositions(type);
    }

    public void setAccelerateForward(boolean accelerate) {
        this.accelerateForward = accelerate;
    }

    public void setAccelerateBackward(boolean accelerate) {
        this.accelerateBackward = accelerate;
    }

    public void setAccelerateClockwise(boolean accelerate) {
        this.accelerateClockwise = accelerate;
    }

    public void setAccelerateCounterClockwise(boolean accelerate) {
        this.accelerateCounterClockwise = accelerate;
    }

    @Override
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
        float force = force();

        if (accelerateForward) {
            addVelocityX(deltaTime * force
                    * (float) Math.cos(Math.toRadians(rotation.getAngle() + 90f)) / getMass());
            addVelocityY(deltaTime * force
                    * (float) Math.sin(Math.toRadians(rotation.getAngle() + 90f)) / getMass());
            applySpeedLimit();

        } else if (accelerateBackward) {
            addVelocityX(-deltaTime * force
                    * (float) Math.cos(Math.toRadians(rotation.getAngle() + 90f)) / getMass());
            addVelocityY(-deltaTime * force
                    * (float) Math.sin(Math.toRadians(rotation.getAngle() + 90f)) / getMass());
            applySpeedLimit();

        } else {
            dampVelocity(deltaTime);
        }

        float rotationForce = force * PhysicsParameters.accelerationForceLimitRotational
                / PhysicsParameters.accelerationForceLimitLongitudonal;

        if (accelerateClockwise) {
            addRotationSpeed(-deltaTime * rotationForce / getMass());
            applyRotationalSpeedLimit();
        } else if (accelerateCounterClockwise) {
            addRotationSpeed(deltaTime * rotationForce / getMass());
            applyRotationalSpeedLimit();
        } else {
            dampRotation(deltaTime);
        }

        rotate(deltaTime * getRotationSpeed());
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
    }

    private float force() {
        return Math.min(
                shipStructure.getCombinedStatModifier().getModifiers().get(Stat.ACCELERATION_FORCE).floatValue(),
                PhysicsParameters.accelerationForceLimitLongitudonal);
    }

    private void applySpeedLimit() {
        float maxSpeed = Math.min(
                shipStructure.getCombinedStatModifier().getModifiers().get(Stat.MAX_SPEED).floatValue(),
                PhysicsParameters.maxVelocityLongitudonal);
        if (getSpeed() > maxSpeed) {
            velocity.scl(maxSpeed / getSpeed());
        }
    }

    private void applyRotationalSpeedLimit() {
        float maxSpeed = Math.min(
                shipStructure.getCombinedStatModifier().getModifiers().get(Stat.MAX_SPEED).floatValue(),
                PhysicsParameters.maxVelocityRotational);
        float maxRotSpeed = maxSpeed * PhysicsParameters.maxVelocityRotational
                / PhysicsParameters.maxVelocityLongitudonal;

        if (getRotationSpeed() < -maxRotSpeed) {
            rotation.setRotationSpeed(-maxRotSpeed);
        } else if (getRotationSpeed() > maxRotSpeed) {
            rotation.setRotationSpeed(maxRotSpeed);
        }
    }

    private void dampVelocity(float deltaTime) {
        float deltaVelocity = deltaTime * PhysicsParameters.dampingLongitudonal / getMass();
        if (getSpeed() < deltaVelocity) {
            setVelocityX(0f);
            setVelocityY(0f);
        } else {
            scaleVelocity(1f - deltaVelocity / getSpeed());
        }
    }

    private void dampRotation(float deltaTime) {
        float deltaRotVel = deltaTime * PhysicsParameters.dampingRotational / getMass();
        if (Math.abs(getRotationSpeed()) < deltaRotVel) {
            setRotationSpeed(0f);
        } else {
            scaleRotationSpeed(1f - deltaRotVel / Math.abs(getRotationSpeed()));
        }
    }

    @Override
    public FloatPair getAbsoluteCenter() {
        return new FloatPair(getX() + shipStructure.getWidth() / 2f - PhysicsParameters.fuselageRadius,
                getY() + shipStructure.getHeight() / 2f - PhysicsParameters.fuselageRadius);
    }

    @Override
    public FloatPair getRelativeCenter() {
        return new FloatPair(shipStructure.getWidth() / 2f, shipStructure.getHeight() / 2f);
    }

    @Override
    public FloatPair getAbsoluteCenterOfMass() {
        return new FloatPair(getX() + shipStructure.getCenterOfMass().x(),
                getY() + shipStructure.getCenterOfMass().y());
    }

    @Override
    public FloatPair getRelativeCenterOfMass() {
        return shipStructure.getCenterOfMass();
    }

    @Override
    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    @Override
    public void dealDamage(Damageable target, int damage) {
        target.takeDamage(damage);
    }

    @Override
    public void takeDamage(int hitPoints) {
        if (hitPoints < 0) {
            throw new IllegalArgumentException("Damage can't be negative");
        }
        this.hitPoints = Math.max(0, this.hitPoints - hitPoints);
    }

    @Override
    public int getHitPoints() {
        return hitPoints;
    }

    @Override
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void repair(int hitPoints) {
        if (isDestroyed()) {
            return;
        }
        this.hitPoints = Math.min(maxHitPoints, this.hitPoints + hitPoints);
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public int getResourceValue() {
        return getShipStructure().getResourceValue();
    }

    @Override
    public int getDamage() {
        return hitPoints;
    }

    public boolean isAccelerating() {
        return accelerateBackward || accelerateForward || accelerateClockwise || accelerateCounterClockwise;
    }

    @Override
    public float getMass() {
        return shipStructure.getMass();
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public boolean isShooting() {
        return isShooting && timeSinceLastShot >= fireRate;
    }

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public void hasShot() {
        timeSinceLastShot = 0f;
        isShooting = false;
    }

    @Override
    public float getRadius() {
        return shipStructure.getRadius();
    }

    /**
     * Get the radius of the ship for proximity detection.
     * This radius takes into consideration the fact that
     * the ship rotates about its center of mass, not the
     * center of its <code>ShipStructure</code>.
     * 
     * <p>
     * This provides a radius for which it is safe to define
     * the minimal proximity / hit-detection radius of a ship.
     * It defines the radius of the circle centered at the ship's
     * center of mass which is guaranteed to contain the ship
     * no matter what its angle of rotation about the center of mass is.
     * 
     * @return a proximity radius of the circle centred at the ship's center of mass
     */
    public float getProximityRadius() {
        return SpaceCalculator.distance(getRelativeCenter(), getRelativeCenterOfMass()) + getRadius()
                + PhysicsParameters.fuselageRadius / 2f;
    }
}
