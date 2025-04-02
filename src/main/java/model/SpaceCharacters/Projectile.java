package model.SpaceCharacters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import model.Globals.DamageDealer;
import model.Globals.Damageable;
import model.utils.SpaceCalculator;

public abstract class Projectile extends SpaceBody implements Damageable, DamageDealer, Poolable {

    private int hitPoints;
    private int maxHitPoints;
    private String sourceID;
    private final int multiplier = 1;

    public Projectile(String name, String description, CharacterType characterType, float x,
            float y, float speed, int hitPoints, float mass, float angle, float radius) {
        super(name, description, characterType, x, y, angle, radius);
        this.setMass(mass);
        this.hitPoints = hitPoints;
        this.maxHitPoints = hitPoints;
        setVelocity(SpaceCalculator.velocityFromAngleSpeed(angle, speed));
    }

    public Projectile(String name, String description, CharacterType characterType, float x,
            float y, float vX, float vY, int hitPoints, float mass, float angle, float radius,
            float rotationSpeed) {
        super(name, description, characterType, new Vector2(x, y), new Vector2(vX, vY), mass, angle,
                rotationSpeed, radius);
        this.hitPoints = hitPoints;
        this.maxHitPoints = hitPoints;
    }

    /**
     * Get the health points of a Projectile object.
     */
    @Override
    public int getHitPoints() {
        return this.hitPoints;
    }

    @Override
    public int getMaxHitPoints() {
        return this.maxHitPoints;
    }

    @Override
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void takeDamage(int hitPoints) {
        this.hitPoints = Math.max(this.hitPoints - hitPoints, 0);
    }


    public void dealDamage(Damageable target, int damage) {
        target.takeDamage(damage);
    }

    public int getDamage() {
        return this.multiplier * this.hitPoints;
    }

    @Override
    public void reset() {
        this.position.set(0f, 0f);
        this.velocity.set(0f, 0f);
        this.rotation.setAngle(0f);
        this.rotation.setRotationSpeed(0f);
        this.hitPoints = 0;
        this.maxHitPoints = 1;
        this.sourceID = "";
    }

    public void init(float x, float y, float vX, float vY, int hitPoints, float mass, float angle,
            float radius,
            float rotationSpeed) {
        position.set(x, y);
        velocity.set(vX, vY);
        this.hitPoints = hitPoints;
        maxHitPoints = hitPoints;
        this.mass = mass;
        rotation.setAngle(angle);
        rotation.setRotationSpeed(rotationSpeed);
        this.radius = radius;
        this.sourceID = "";
    }

    public void init(float x, float y, float speed, int hitPoints, float mass, float angle,
            float radius,
            float rotationSpeed) {
        Vector2 velocity = SpaceCalculator.velocityFromAngleSpeed(angle, speed);
        init(x, y, velocity.x, velocity.y, hitPoints, mass, angle, radius, rotationSpeed);
    }

    /**
     * Set the source ID for this {@code Projectile}
     */
    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    /**
     * Get the unique ID for this {@code Projectile}
     *
     * @return The sourceID as a {@code String}
     */
    public String getSourceID() {
        return sourceID;
    }
}
