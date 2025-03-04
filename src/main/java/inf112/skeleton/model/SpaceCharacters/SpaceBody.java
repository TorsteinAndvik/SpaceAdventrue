package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.Rotatable;
import inf112.skeleton.model.Globals.SpaceThing;
import inf112.skeleton.model.utils.Rotation;

public abstract class SpaceBody implements SpaceThing, Rotatable, SpaceBodyView {
  private String name;
  private String description;
  private final Rotation rotation;
  private int x;
  private int y;
  private int mass;
  private int speed;
  private int radius;

  public SpaceBody(String name, String description, int x, int y, int mass, int speed, float angle, int radius) {
    this.name = name;
    this.description = description;
    this.x = x;
    this.y = y;
    this.mass = mass;
    this.speed = speed;
    this.radius = radius;
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

  @Override
  public int getX() {
    return x;
  }

  /**
   * Set the x-coordinate of a SpaceBody object.
   */
  public void setX(int x) {
    this.x = x;
  }

  @Override
  public int getY() {
    return y;
  }

  /**
   * Set the y-coordinate of a SpaceBody object.
   */
  public void setY(int y) {
    this.y = y;
  }

  @Override
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

  @Override
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
   * Set the radius of a SpaceBody object.
   */
  public void setRadius(int r) {
    this.radius = r;
  }

  @Override
  public int getRadius() {
    return this.radius;
  }

  @Override
  public float getRotationAngle() {
    return rotation.getAngle();
  }

  @Override
  public void setRotation(float angle) {
    this.rotation.setAngle(angle);
  }

  @Override
  public void rotate(float deltaAngle) {
    this.rotation.rotate(deltaAngle);
  }
}
