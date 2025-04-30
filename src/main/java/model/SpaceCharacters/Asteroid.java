package model.SpaceCharacters;

public class Asteroid extends Projectile {

    private boolean isLarge;

    public Asteroid() {
        this("Asteroid", "An asteroid");
    }

    public Asteroid(String name, String description) {
        super(name, description, CharacterType.ASTEROID, 0f, 0f, 0f, 0f, 1, 0f, 0f, 0, 0);
        this.isLarge = false;
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
        int resourceValueMultiplier = 10;
        return (int) (mass * radius * resourceValueMultiplier);
    }
}
