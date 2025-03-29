package model.SpaceCharacters;

import model.Globals.DamageDealer;
import model.Globals.Damageable;
import model.Globals.Repairable;
import model.ShipComponents.Components.ShipStructure;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;

public abstract class SpaceShip extends SpaceBody implements DamageDealer, Damageable, Repairable {

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
            this.setRadius(
                    (float) Math.sqrt(
                            Math.pow(shipStructure.getWidth() / 2f, 2) + Math.pow(
                                    shipStructure.getHeight() / 2f, 2)));
        }
    }

    public ShipStructure getShipStructure() {
        return shipStructure;
    }

    public boolean isPlayerShip() {
        return false;
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
            scaleRotationSpeed(1f - deltaRotVel / (float) Math.abs(getRotationSpeed()));
        }
    }

    /**
     * @return center point of the ship, as opposed to getX() and getY() which
     *         return the bottom
     *         left corner
     */
    public FloatPair getCenter() {
        return new FloatPair(getX() + ((float) shipStructure.getWidth()) / 2f,
                getY() + ((float) shipStructure.getHeight()) / 2f);
    }

    @Override
    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    @Override
    public void dealDamage(Damageable target) {
        // Damage dealt when ship hits damageable object
        target.takeDamage(getHitPoints());
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
}
