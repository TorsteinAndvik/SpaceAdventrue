package controller;

import com.badlogic.gdx.math.Vector2;

import model.utils.FloatPair;

/**
 * Represents a controllable SpaceBody that can be moved and updated
 * dynamically. Provides methods
 * to manipulate position, velocity, mass, and radius.
 */
public interface ControllableSpaceBody {

    /**
     * Sets the x-coordinate of the SpaceBody.
     *
     * @param x The new x-coordinate.
     */
    void setX(float x);

    /**
     * Sets the y-coordinate of the SpaceBody.
     *
     * @param y The new y-coordinate.
     */
    void setY(float y);

    /**
     * Sets the x and y coordinate of the <code>SpaceBody</code>
     * to the x and y values held by <code>pos</code>.
     *
     * @param pos a <code>FloatPair</code> giving the new position.
     */
    void setPosition(FloatPair pos);

    /**
     * Translates the <code>SpaceBody</code> by a given vector,
     * represented by a <code>FloatPair</code>.
     * 
     * @param translation the <code>FloatPair</code> to translate by.
     */
    void translate(FloatPair translation);

    /**
     * Sets the mass of the SpaceBody.
     *
     * @param mass The mass of the SpaceBody (in kilograms).
     */
    void setMass(float mass);

    /**
     * Sets the radius of the SpaceBody.
     *
     * @param r The radius of the SpaceBody (in meters).
     */
    void setRadius(float r);

    /**
     * Sets the x-component of the SpaceBody's velocity.
     *
     * @param x The velocity in the x direction.
     */
    void setVelocityX(float x);

    /**
     * Sets the y-component of the SpaceBody's velocity.
     *
     * @param y The velocity in the y direction.
     */
    void setVelocityY(float y);

    /**
     * Adds to the x-component of the SpaceBody's velocity.
     *
     * @param deltaX The added velocity in the x direction.
     */
    void addVelocityX(float deltaX);

    /**
     * Adds to the y-component of the SpaceBody's velocity.
     *
     * @param deltaY The added velocity in the y direction.
     */
    void addVelocityY(float deltaY);

    /**
     * Sets the velocity of the SpaceBody using a 2D vector.
     *
     * @param velocity The velocity vector (x, y).
     */
    void setVelocity(Vector2 velocity);

    /**
     * Scale the velocity of the SpaceBody by the given scalar.
     * 
     * @param scale The scalar to scale the velocity by.
     */
    void scaleVelocity(float scale);

    /**
     * Get the rotation angle of an object
     *
     * @return the objects rotation angle
     */
    float getRotationAngle();

    /**
     * Set the rotation of a given object.
     *
     * @param angle the angle of the object.
     */
    void setRotation(float angle);

    /**
     * Set the rotation speed of a rotatable object.
     *
     * @param rotationSpeed the rotation speed.
     */
    void setRotationSpeed(float rotationSpeed);

    /**
     * Adds to the rotation speed of a rotatable object.
     *
     * @param deltaRotationSpeed the added rotation speed.
     */
    void addRotationSpeed(float deltaRotationSpeed);

    /**
     * Scale the rotational speed of the SpaceBody by the given scalar.
     * 
     * @param scale The scalar to scale the rotational velocity by.
     */
    void scaleRotationSpeed(float scale);

    /**
     * Rotates an object around a given angle
     *
     * @param deltaAngle the angle to rotate
     */
    void rotate(float deltaAngle);

    /**
     * Updates the state of the SpaceBody.
     *
     * @param deltaTime the time since last update.
     */
    void update(float deltaTime);

}