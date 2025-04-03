package model.SpaceCharacters;

public class Asteroid extends Projectile {

    private boolean isLarge;

    public Asteroid() {
        this("asteroid", "an asteroid", 0f, 0f, 0f, 0f, 1, 1f, 0f, 0f, 0f, false);
    }

    public Asteroid(String name, String description, float x, float y, float vX, float vY,
            int hitPoints, float mass, float angle, float radius, float rotationSpeed, boolean isLarge) {
        super(name, description, CharacterType.ASTEROID, x, y, vX, vY, hitPoints, mass, angle,
                radius, rotationSpeed);
        this.isLarge = isLarge;
    }

    public Asteroid(String name, String description, float x, float y, float vX, float vY,
            float mass, float angle, float radius, float rotationSpeed) {
        this(name, description, x, y, vX, vY, 0, mass, angle,
                radius, rotationSpeed, false);
    }

    public void init(float x, float y, float speed, int hitPoints, float mass, float angle, float radius,
            float rotationSpeed, boolean isLarge) {
        super.init(x, y, speed, hitPoints, mass, angle, radius, rotationSpeed);
        this.isLarge = isLarge;
    }

    public void init(float x, float y, float vX, float vY, int hitPoints, float mass, float angle, float radius,
            float rotationSpeed, boolean isLarge) {
        super.init(x, y, vX, vY, hitPoints, mass, angle, radius, rotationSpeed);
        this.isLarge = isLarge;
    }

    public boolean isLarge() {
        return this.isLarge;
    }

    @Override
    public int getResourceValue() {
        return (int) (mass * radius * 10);
    }
}
