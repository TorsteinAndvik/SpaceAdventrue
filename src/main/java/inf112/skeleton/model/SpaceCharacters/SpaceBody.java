package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.SpaceThing;

public interface SpaceBody extends SpaceThing {

  /**
   * Get the health point of a SpaceBody object.
   */
  public int getHealthPoints();

  /**
   * Get the mass of a SpaceBody object.
   *
   * @return the mass og a given object.
   */
  int getMass();

  /**
   * Set the mass of a SpaceBody object.
   *
   * @param mass the mass of the given object.
   */
  void setMass(int mass);

  /**
   * Get the speed of a SpaceBody object.
   *
   * @return the speed of the given object.
   */
  int getSpeed();

  /**
   * Set the speed of a given SpaceBody object.
   *
   * @param speed the speed of the given object.
   */
  void setSpeed(int speed);

  /**
   * The rotation of a SpaceBody object.
   *
   * @return the rotation of the given object.
   */
  float getRotationAngle();

  /**
   * Set the rotation of a given object.
   *
   * @param angle the angle of the object.
   * @return true if the object is rotated, false otherwise.
   */
  boolean setRotation(float angle);
}
