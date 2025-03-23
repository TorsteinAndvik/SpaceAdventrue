package inf112.skeleton.model.SpaceCharacters;

public class Asteroid extends Projectile {

    private static final float ROTATION_SPEED = 0.01f;

    public final boolean isLarge;

    public Asteroid(String name, String description, float x, float y, float vX, float vY,
            int hitPoints, float mass, float angle, float radius) {
        this(name, description, x, y, vX, vY, hitPoints, mass, angle,
                radius, false);
    }

    public Asteroid(String name, String description, float x, float y, float vX, float vY,
            int hitPoints, float mass, float angle, float radius, boolean isLarge) {
        super(name, description, CharacterType.ASTEROID, x, y, vX, vY, hitPoints, mass, angle,
                radius, ROTATION_SPEED);
        this.isLarge = isLarge;
    }

}
