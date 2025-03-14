package inf112.skeleton.model.utils;

/**
 * Utility class for handling rotations in a 2D space. This class represents the
 * rotation of an
 * object by managing its angle in degrees. It provides methods to set, get, and
 * adjust the rotation
 * angle.
 */
public class Rotation {

    private float angle;
    private float rotationSpeed;

    /**
     * Constructs a new Rotation object with the specified initial angle.
     *
     * @param angle the initial angle in degrees.
     */
    public Rotation(float angle) {
        this(angle, 0);
    }

    public Rotation(float angle, float rotationSpeed) {
        this.angle = normalizeAngle(angle);
        this.rotationSpeed = rotationSpeed;
    }

    /**
     * Retrieves the current rotation angle.
     *
     * @return the current angle in degrees.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Sets the rotation angle.
     *
     * @param angle the new angle in degrees.
     */
    public void setAngle(float angle) {
        this.angle = normalizeAngle(angle);
    }

    /**
     * Set the rotation speed.
     *
     * @param rotationSpeed the rotation speed.
     */
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    /**
     * Get the rotation speed.
     *
     * @return the rotation speed.
     */
    public float getRotationSpeed() {
        return this.rotationSpeed;
    }

    /**
     * Adjusts rotation by a given delta.
     *
     * @param deltaAngle the change in angle in degrees.
     */
    public void rotate(float deltaAngle) {
        angle += deltaAngle;
        angle = normalizeAngle(angle);
    }

    /**
     * Normalizes the angle to ensure it is within the range [0, 360) degrees.
     *
     * @param angle the angle to normalize.
     * @return the normalized angle.
     */
    private float normalizeAngle(float angle) {
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }
        return Math.abs(angle);
    }

    public void update(float deltaTime) {
        angle = normalizeAngle(angle + rotationSpeed * deltaTime);
    }

    /**
     * Returns a string representation of the rotation object.
     *
     * @return a string representation of the current angle.
     */
    @Override
    public String toString() {
        return "Rotation{angle=" + angle + '}';
    }

}