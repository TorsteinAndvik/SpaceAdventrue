package model.SpaceCharacters;

public class Bullet extends Projectile {

    public Bullet(String name, String description, float x, float y, int hitPoints, float angle,
        float speed, float radius) {
        super(name, description, CharacterType.BULLET, x, y, hitPoints, angle, speed, radius);
    }

}
