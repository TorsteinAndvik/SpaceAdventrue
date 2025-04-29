package model.SpaceCharacters;

public class Bullet extends Projectile {

    private boolean isPlayerBullet;

    /**
     * Constructs a new Bullet with direction and speed.
     *
     * @param name           the name of the bullet.
     * @param description    a short description of the bullet.
     * @param x              the x-coordinate of the bullet's position.
     * @param y              the y-coordinate of the bullet's position.
     * @param speed          the speed at which the bullet travels.
     * @param angle          the direction the bullet travels, in degrees.
     * @param radius         the collision radius of the bullet.
     * @param isPlayerBullet true if fired by the player; false if by an enemy.
     */
    public Bullet(String name, String description, float x, float y, float speed, float angle, float radius,
            boolean isPlayerBullet) {
        super(name, description, CharacterType.BULLET, x,
                y, speed, 1, 0f, angle, radius);
        this.isPlayerBullet = isPlayerBullet;
    }

    /**
     * Constructs a new Bullet with explicit velocity and hit points.
     *
     * @param name           the name of the bullet.
     * @param description    a short description of the bullet.
     * @param x              the x-coordinate of the bullet's position.
     * @param y              the y-coordinate of the bullet's position.
     * @param vX             horizontal velocity.
     * @param vY             vertical velocity.
     * @param hitPoints      health or damage value of the bullet.
     * @param angle          the direction the bullet is facing, in degrees.
     * @param radius         the collision radius of the bullet.
     * @param isPlayerBullet true if fired by the player; false if by an enemy.
     */
    public Bullet(String name, String description, float x, float y, float vX, float vY, int hitPoints, float angle,
            float radius, boolean isPlayerBullet) {
        super(name, description, CharacterType.BULLET, x, y, vX, vY, hitPoints, 0f, angle, radius, 0f);
        this.isPlayerBullet = isPlayerBullet;
    }

    /**
     * Initializes the bullet with new values. Useful for object pooling.
     *
     * @param x              new x-coordinate.
     * @param y              new y-coordinate.
     * @param speed          speed of the bullet.
     * @param angle          direction in degrees.
     * @param radius         collision radius.
     * @param isPlayerBullet true if fired by the player; false otherwise.
     */
    public void init(float x, float y, float speed, float angle, float radius, boolean isPlayerBullet) {
        super.init(x, y, speed, 1, 1f, angle, radius, 0f);
        this.isPlayerBullet = isPlayerBullet;
    }

    /**
     * Returns whether this bullet was fired by the player.
     *
     * @return true if fired by the player; false if by an enemy.
     */
    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }

    @Override
    public void reset() {
        super.reset();
        isPlayerBullet = false;
    }
}
