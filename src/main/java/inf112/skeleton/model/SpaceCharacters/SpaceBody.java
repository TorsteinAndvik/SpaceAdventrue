package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.SpaceThing;
import inf112.skeleton.model.utils.Rotation;

public abstract class SpaceBody implements SpaceThing {
  private String name;
  private String description;
  private final Rotation rotation;
  private int x;
  private int y;
  private int healthPoints;
  private int mass;
  private int speed;

  public SpaceBody(String name, String description, int x, int y, int healthPoints, int mass, int speed, float angle) {
    this.name = name;
    this.description = description;
    this.x = x;
    this.y = y;
    this.healthPoints = healthPoints;
    this.mass = mass;
    this.speed = speed;
    rotation = new Rotation(angle);
  }


  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Get the x-coordinate of a SpaceBody object.
   */
  public int getX() {
    return x;
  }

  /**
   * Set the x-coordinate of a SpaceBody object.
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Get the y-coordinate of a SpaceBody object.
   */
  public int getY() {
    return y;
  }

  /**
   * Set the y-coordinate of a SpaceBody object.
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Get the health points of a SpaceBody object.
   */
  public int getHealthPoints() {
    return healthPoints;
  }

  /**
   * Set the health points of a SpaceBody object.
   */
  public void setHealthPoints(int healthPoints) {
    this.healthPoints = healthPoints;
  }


  /**
   * Get the mass of a SpaceBody object.
   *
   * @return the mass og a given object.
   */
  public int getMass() {
    return mass;
  }

  /**
   * Set the mass of a SpaceBody object.
   *
   * @param mass the mass of the given object.
   */
  public void setMass(int mass) {
    this.mass = mass;
  }

  /**
   * Get the speed of a SpaceBody object.
   *
   * @return the speed of the given object.
   */
  public int getSpeed() {
    return speed;
  }

  /**
   * Set the speed of a given SpaceBody object.
   *
   * @param speed the speed of the given object.
   */
  public void setSpeed(int speed) {
    this.speed = speed;
  }

  /**
   * The rotation of a SpaceBody object.
   *
   * @return the rotation of the given object.
   */
  public float getRotationAngle() {
    return rotation.getAngle();
  }

  /**
   * Set the rotation of a given object.
   *
   * @param angle the angle of the object.
   * @return true if the object is rotated, false otherwise.
   */
  public boolean setRotation(float angle) {
    this.rotation.setAngle(angle);
    return true; // TODO: Check if can rotate;
  }

  public boolean rotate(float deltaAngle) {
    this.rotation.rotate(deltaAngle);
    return true; // TODO: Check if can rotate;
  }

}
