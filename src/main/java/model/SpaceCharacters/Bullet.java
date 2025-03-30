package model.SpaceCharacters;

public class Bullet extends Projectile {

    private boolean isPlayerBullet;

    public Bullet(String name, String description, float x, float y, float speed, float angle, float radius,
            boolean isPlayerLaser) {
        super(name, description, CharacterType.BULLET, x,
                y, speed, 1, 0f, angle, radius);
    }

    public Bullet(String name, String description, float x, float y, float vX, float vY, int hitPoints, float angle,
            float radius, boolean isPlayerBullet) {
        super(name, description, CharacterType.BULLET, x, y, vX, vY, hitPoints, 0f, angle, radius, 0f);
        this.isPlayerBullet = isPlayerBullet;
    }

    public void init(float x, float y, float speed, float angle, float radius, boolean isPlayerBullet) {
        super.init(x, y, speed, 1, 1f, angle, radius, 0f);
        this.isPlayerBullet = isPlayerBullet;
    }

    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }
}
