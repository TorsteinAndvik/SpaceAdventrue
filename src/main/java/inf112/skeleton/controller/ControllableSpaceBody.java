package inf112.skeleton.controller;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.Globals.Rotatable;

/**
 * Represents a controllable SpaceBody that can be moved and updated dynamically.
 * Provides methods to manipulate position, velocity, mass, and radius.
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
   * Sets the velocity of the SpaceBody using a 2D vector.
   *
   * @param velocity The velocity vector (x, y).
   */
  void setVelocity(Vector2 velocity);

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
