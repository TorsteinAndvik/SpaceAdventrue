package inf112.skeleton.model.SpaceCharacters;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.model.Globals.Rotatable;
import inf112.skeleton.model.Globals.SpaceThing;
import inf112.skeleton.model.utils.Rotation;

public abstract class SpaceBody implements SpaceThing, Rotatable, SpaceBodyView {
  private String name;
  private String description;
  private final Rotation rotation;

  private Vector2 position;
  private Vector2 velocity;
  private int mass;
  private int radius;


  public SpaceBody(String name, String description, float x, float y, int mass, float angle, float rotationSpeed, int radius) {
    this.name = name;
    this.description = description;
    this.position = new Vector2(x, y);
    this.velocity = new Vector2(0, 0);
    this.mass = mass;
    this.radius = radius;
    rotation = new Rotation(angle, rotationSpeed);
  }

  public SpaceBody(String name, String description, float x, float y, int mass, float angle, int radius) {
    this(name, description, x, y, mass, angle, 0, radius);
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
  public float getX() {
    return position.x;
  }

  /**
   * Set the x-coordinate of a SpaceBody object.
   */
  public void setX(int x) {
    this.position.x = x;
  }

  @Override
  public float getY() {
    return (int) position.y;
  }

  /**
   * Set the y-coordinate of a SpaceBody object.
   */
  public void setY(int y) {
    this.position.y = y;
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
  public float getSpeed() {
    return velocity.len();
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

  @Override
  public float getRotationSpeed() {
    return rotation.getRotationSpeed();
  }

  @Override
  public void setRotationSpeed(float rotationSpeed) {
    rotation.setRotationSpeed(rotationSpeed);
  }


  public void setVelocityX(float x) {
    velocity.x = x;
  }

  public void setVelocityY(float y) {
    velocity.y = y;
  }

  public void move(float deltaTime) {
    position = position.add(velocity.x * deltaTime, velocity.y * deltaTime);
    rotation.update(deltaTime);

  }
}
