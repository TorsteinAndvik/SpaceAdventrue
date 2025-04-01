package model.SpaceCharacters;

import java.util.List;

import grid.CellPosition;
import model.Globals.DamageDealer;
import model.Globals.Damageable;
import model.Globals.Repairable;
import model.ShipComponents.Components.ShipStructure;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;
import java.util.UUID;

public abstract class SpaceShip extends SpaceBody implements DamageDealer, Damageable, Repairable {

    private final String id;
    private int maxHitPoints;
    private int hitPoints;

    private boolean accelerateForward;
    private boolean accelerateBackward;
    private boolean accelerateClockwise;
    private boolean accelerateCounterClockwise;

    private ShipStructure shipStructure;

    public SpaceShip(ShipStructure shipStructure, String name, String description,
            CharacterType characterType, float x,
            float y, int maxHealthPoints, float angle) {
        this(shipStructure, name, description, characterType, x, y, maxHealthPoints,
                maxHealthPoints, angle);
    }

    public SpaceShip(ShipStructure shipStructure, String name, String description,
            CharacterType characterType, float x,
            float y, int maxHitPoints, int hitPoints, float angle) {
        super(name, description, characterType, x, y, angle, 0f);
        if (hitPoints <= 0 || maxHitPoints <= 0) {
            throw new IllegalArgumentException("Hit points must be positive on ship creation");
        }
        if (maxHitPoints < hitPoints) {
            throw new IllegalArgumentException("Hit points can't be higher than max hit points");
        }
        this.hitPoints = hitPoints;
        this.maxHitPoints = maxHitPoints;
        this.shipStructure = shipStructure;
        if (this.shipStructure != null) {
            this.setRadius(SpaceCalculator.distance(shipStructure.getWidth() / 2f,
                    shipStructure.getHeight() / 2f));
        }
        id = UUID.randomUUID().toString();
    }

    public ShipStructure getShipStructure() {
        return shipStructure;
    }

    public boolean isPlayerShip() {
        return false;
    }

    public List<CellPosition> getTurretPositions() {
        return shipStructure.getTurretPositions();
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
    protected void move(float deltaTime) {
        if (accelerateForward) {
            // rotate by 90f since in view 0 degrees is North.
            // TODO: refactor after ship rotation is fully moved to model (if possible)
            addVelocityX(deltaTime * PhysicsParameters.accelerationLimitLongitudonal
                    * (float) Math.cos(Math.toRadians(rotation.getAngle() + 90f)));
            addVelocityY(deltaTime * PhysicsParameters.accelerationLimitLongitudonal
                    * (float) Math.sin(Math.toRadians(rotation.getAngle() + 90f)));
            applySpeedLimit();
        } else if (accelerateBackward) {
            addVelocityX(-deltaTime * PhysicsParameters.accelerationLimitLongitudonal
                    * (float) Math.cos(Math.toRadians(rotation.getAngle() + 90f)));
            addVelocityY(-deltaTime * PhysicsParameters.accelerationLimitLongitudonal
                    * (float) Math.sin(Math.toRadians(rotation.getAngle() + 90f)));
            applySpeedLimit();
        } else {
            dampVelocity(deltaTime);
        }

        if (accelerateClockwise) {
            addRotationSpeed(-deltaTime * PhysicsParameters.accelerationLimitRotational);
            applyRotationalSpeedLimit();
        } else if (accelerateCounterClockwise) {
            addRotationSpeed(deltaTime * PhysicsParameters.accelerationLimitRotational);
            applyRotationalSpeedLimit();
        } else {
            dampRotation(deltaTime);
        }

        rotate(deltaTime * getRotationSpeed());
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
    }

    private void applySpeedLimit() {
        if (getSpeed() > PhysicsParameters.maxVelocityLongitudonal) {
            velocity.scl(PhysicsParameters.maxVelocityLongitudonal / getSpeed());
        }
    }

    private void applyRotationalSpeedLimit() {
        if (getRotationSpeed() < -PhysicsParameters.maxVelocityRotational) {
            rotation.setRotationSpeed(-PhysicsParameters.maxVelocityRotational);
        } else if (getRotationSpeed() > PhysicsParameters.maxVelocityRotational) {
            rotation.setRotationSpeed(PhysicsParameters.maxVelocityRotational);
        }
    }

    private void dampVelocity(float deltaTime) {
        float deltaVelocity = deltaTime * PhysicsParameters.dampingLongitudonal;
        if (getSpeed() < deltaVelocity) {
            setVelocityX(0f);
            setVelocityY(0f);
        } else {
            scaleVelocity(1f - deltaVelocity / getSpeed());
        }
    }

    private void dampRotation(float deltaTime) {
        float deltaRotVel = deltaTime * PhysicsParameters.dampingRotational;
        if (Math.abs(getRotationSpeed()) < deltaRotVel) {
            setRotationSpeed(0f);
        } else {
            scaleRotationSpeed(1f - deltaRotVel / Math.abs(getRotationSpeed()));
        }
    }

    /**
     * @return absolute center point of the ship grid.
     *         Shifts the relative center by the ship's global X and Y coordinates.
     */
    public FloatPair getAbsoluteCenter() {
        return new FloatPair(getX() + shipStructure.getWidth() / 2f,
                getY() + shipStructure.getHeight() / 2f);
    }

    /**
     * @return center point of the ship grid relative to its bottom left corner.
     */
    public FloatPair getRelativeCenter() {
        return new FloatPair(shipStructure.getWidth() / 2f, shipStructure.getHeight() / 2f);
    }

    /**
     * @return absolute center of mass of the ship structure.
     *         Shifts the relative center of mass by the ship's global X and Y
     *         coordinates.
     */
    public FloatPair getAbsoluteCenterOfMass() {
        return new FloatPair(getX() + shipStructure.getCenterOfMass().x(),
                getY() + shipStructure.getCenterOfMass().y());
    }

    /**
     * @return center of mass of the ship structure relative to its bottom left
     *         corner.
     */
    public FloatPair getRelativeCenterOfMass() {
        return shipStructure.getCenterOfMass();
    }

    @Override
    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    @Override
    public void dealDamage(Damageable target) {
        int targetHP = target.getHitPoints();
        target.takeDamage(this.hitPoints);
        this.takeDamage(targetHP);
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

    /**
     * Get the unique ID for this {@code SpaceBody}
     *
     * @return The ID as a {@code String}
     */
    public String getID() { return this.id; }

    @Override
    public int getResourceValue() {
        return getShipStructure().getResourceValue();
    }
}
