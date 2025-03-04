package inf112.skeleton.model.Globals;

public interface Rotatable {

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
   * Rotates an object around a given angle
   *
   * @param deltaAngle the angle to rotate
   */
  void rotate(float deltaAngle);
}
