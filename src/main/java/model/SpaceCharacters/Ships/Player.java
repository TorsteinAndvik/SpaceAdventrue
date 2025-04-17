package model.SpaceCharacters.Ships;

import java.util.HashMap;
import model.ShipComponents.ShipStructure;
import model.ShipComponents.Components.stats.Stat;
import model.SpaceCharacters.CharacterType;
import model.constants.PhysicsParameters;

public class Player extends SpaceShip implements ViewablePlayer {

    private final PlayerInventory inventory;

    private boolean accelerateForward;
    private boolean accelerateBackward;
    private boolean accelerateClockwise;
    private boolean accelerateCounterClockwise;

    public Player(ShipStructure shipStructure, String name, String description, float x, float y) {
        super(shipStructure, name, description, CharacterType.PLAYER, x, y, 0);
        inventory = new PlayerInventory(new HashMap<>(), 300);
    }

    @Override
    public boolean isPlayerShip() {
        return true;
    }

    /**
     * @return The player inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

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

        if (accelerateClockwise) {
            addRotationSpeed(-deltaTime * torque() / getMass());
            applyRotationalSpeedLimit();
        } else if (accelerateCounterClockwise) {
            addRotationSpeed(deltaTime * torque() / getMass());
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

    private float torque() {
        return force() * PhysicsParameters.accelerationForceLimitRotational
                / PhysicsParameters.accelerationForceLimitLongitudonal;
    }

    private float maxSpeed() {
        return Math.min(
                shipStructure.getCombinedStatModifier().getModifiers().get(Stat.MAX_SPEED).floatValue(),
                PhysicsParameters.maxVelocityLongitudonal);
    }

    private float maxRotVel() {
        return maxSpeed() * PhysicsParameters.maxVelocityRotational / PhysicsParameters.maxVelocityLongitudonal;
    }

    private void applySpeedLimit() {
        float maxSpeed = maxSpeed();
        if (getSpeed() > maxSpeed) {
            velocity.scl(maxSpeed / getSpeed());
        }
    }

    private void applyRotationalSpeedLimit() {
        float maxRotSpeed = maxRotVel();

        if (getRotationSpeed() < -maxRotSpeed) {
            rotation.setRotationSpeed(-maxRotSpeed);
        } else if (getRotationSpeed() > maxRotSpeed) {
            rotation.setRotationSpeed(maxRotSpeed);
        }
    }

    private void dampVelocity(float deltaTime) {
        float deltaVelocity = deltaTime * force() / getMass();
        if (getSpeed() < deltaVelocity) {
            setVelocityX(0f);
            setVelocityY(0f);
        } else {
            scaleVelocity(1f - deltaVelocity / getSpeed());
        }
    }

    private void dampRotation(float deltaTime) {
        float deltaRotVel = deltaTime * torque() / getMass();
        if (Math.abs(getRotationSpeed()) < deltaRotVel) {
            setRotationSpeed(0f);
        } else {
            scaleRotationSpeed(1f - deltaRotVel / Math.abs(getRotationSpeed()));
        }
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

    public boolean isAccelerating() {
        return accelerateBackward || accelerateForward || accelerateClockwise || accelerateCounterClockwise;
    }
}