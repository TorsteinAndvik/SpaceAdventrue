package model.SpaceCharacters;

public class Bullet extends Projectile {

    public final boolean isPlayerBullet;

    public Bullet(String name, String description, float x, float y, int hitPoints, float angle,
            float speed, float radius, boolean isPlayerBullet) {
        super(name, description, CharacterType.BULLET, x, y, hitPoints, angle, speed, radius);
        this.isPlayerBullet = isPlayerBullet;
    }
}
