package model.SpaceCharacters;

import com.badlogic.gdx.math.Vector2;
import controller.ControllableSpaceBody;
import model.Globals.Collidable;
import model.Globals.Rotatable;
import model.Globals.SpaceThing;
import model.utils.ArgumentChecker;
import model.utils.FloatPair;
import model.utils.Rotation;

import java.util.Objects;

public abstract class SpaceBody implements SpaceThing, Rotatable, Collidable, ViewableSpaceBody,
        ControllableSpaceBody {

    private String name;
    private String description;
    private final CharacterType characterType;
    protected final Rotation rotation;
    protected final Vector2 position;
    protected final Vector2 velocity;
    protected float mass;
    protected float radius;

    /**
     * Constructs a SpaceBody with the specified attributes.
     *
     * @param name          The name of the SpaceBody.
     * @param description   A brief description of the SpaceBody.
     * @param characterType The CharacterType representing the type of SpaceBody.
     * @param position      The position of the SpaceBody as a 2D vector.
     * @param velocity      The velocity of the SpaceBody as a 2D vector.
     * @param mass          The mass of the SpaceBody (in kilograms).
     * @param angle         The initial angle of rotation (in degrees).
     * @param rotationSpeed The speed of rotation (in degrees per second).
     * @param radius        The radius of the SpaceBody (in meters).
     */
    public SpaceBody(String name, String description, CharacterType characterType,
            Vector2 position, Vector2 velocity, float mass, float angle, float rotationSpeed,
            float radius) {
        ArgumentChecker.requireNonEmptyString(name, "Name cannot be null or empty.");
        ArgumentChecker.requireNonEmptyString(description, "Description cannot be null or empty.");
        ArgumentChecker.requireNonNull(characterType, "The SpaceBody requires a character type.");
        ArgumentChecker.requireNonNull(position, "The SpaceBody requires a position");
        ArgumentChecker.requireNonNull(velocity, "Velocity can't be null.");
        ArgumentChecker.greaterOrEqualToZero(mass, "Mass can't be negative.");
        ArgumentChecker.greaterOrEqualToZero(radius, "Radius can't be negative.");
        this.name = name;
        this.description = description;
        this.characterType = characterType;
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
        this.radius = radius;
        rotation = new Rotation(angle, rotationSpeed);
    }

    /**
     * Constructs a SpaceBody with essential properties for rendering.
     *
     * @param name          The name of the SpaceBody.
     * @param description   A brief description of the SpaceBody.
     * @param characterType The type of character this SpaceBody represents.
     * @param x             The x-coordinate of the SpaceBody in space.
     * @param y             The y-coordinate of the SpaceBody in space.
     * @param angle         The angle of rotation (in degrees).
     * @param radius        The radius of the SpaceBody (in meters).
     * @implNote This constructor calls the full constructor with a default mass and
     *         rotation speed of 0.
     */
    public SpaceBody(String name, String description, CharacterType characterType, float x, float y,
            float angle, float radius) {
        this(name, description, characterType, new Vector2(x, y), new Vector2(0, 0), 0, angle, 0,
                radius);
    }

    /**
     * Creates a default SpaceBody object with all positional, physical, and
     * rotational values set
     * to zero.
     *
     * @param name          The name of the SpaceBody.
     * @param description   A brief description of the SpaceBody.
     * @param characterType The CharacterType representing the type of SpaceBody.
     * @implNote This constructor initializes the SpaceBody at (0,0) with zero mass,
     *         angle, velocity, and radius.
     */
    public SpaceBody(String name, String description, CharacterType characterType) {
        this(name, description, characterType, new Vector2(0, 0), new Vector2(0, 0), 0, 0, 0, 0);
    }

    public void init(float x, float y, float vX, float vY, float mass, float angle, float radius, float rotationSpeed) {
        position.set(x, y);
        velocity.set(vX, vY);
        this.mass = mass;
        rotation.setAngle(angle);
        rotation.setRotationSpeed(rotationSpeed);
        this.radius = radius;
    }

    @Override
    public void setName(String name) {
        ArgumentChecker.requireNonEmptyString(name, "Name cannot be null or empty.");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void setDescription(String description) {
        ArgumentChecker.requireNonEmptyString(description, "Description cannot be null or empty.");
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public CharacterType getCharacterType() {
        return characterType;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public void setX(float x) {
        this.position.x = x;
    }

    @Override
    public float getY() {
        return position.y;
    }

    @Override
    public void setY(float y) {
        this.position.y = y;
    }

    @Override
    public void setPosition(FloatPair pos) {
        setX(pos.x());
        setY(pos.y());
    }

    @Override
    public void translate(FloatPair translation) {
        setX(getX() + translation.x());
        setY(getY() + translation.y());
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public void setMass(float mass) {
        ArgumentChecker.greaterOrEqualToZero(mass, "Mass can't be negative.");
        this.mass = mass;
    }

    @Override
    public float getSpeed() {
        return velocity.len();
    }

    @Override
    public void setRadius(float radius) {
        ArgumentChecker.greaterOrEqualToZero(radius, "Radius can't be negative.");
        this.radius = radius;
    }

    @Override
    public float getRadius() {
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

    @Override
    public void scaleRotationSpeed(float scale) {
        rotation.setRotationSpeed(scale * getRotationSpeed());
    }

    @Override
    public void addRotationSpeed(float deltaRotationSpeed) {
        rotation.setRotationSpeed(rotation.getRotationSpeed() + deltaRotationSpeed);
    }

    @Override
    public void setVelocityX(float x) {
        velocity.x = x;

    }

    @Override
    public void setVelocityY(float y) {
        velocity.y = y;

    }

    @Override
    public void addVelocityX(float deltaX) {
        velocity.x += deltaX;

    }

    @Override
    public void addVelocityY(float deltaY) {
        velocity.y += deltaY;
    }

    @Override
    public void setVelocity(Vector2 velocity) {
        Objects.requireNonNull(velocity, "Velocity can't be null.");
        setVelocity(velocity.x, velocity.y);
    }

    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    @Override
    public void scaleVelocity(float scale) {
        this.velocity.scl(scale);
    }

    @Override
    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    @Override
    public int getResourceValue() {
        return 0;
    }

    /**
     * Moves the SpaceBody based on the given time step.
     *
     * @param deltaTime The time step for movement calculation (in seconds).
     */
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        rotation.update(deltaTime);
    }
}
