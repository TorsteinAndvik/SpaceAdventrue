package inf112.skeleton.model.SpaceCharacters;

public class Bullet extends Projectile {

  public Bullet(String name, String description, int healthPoints, float x, float y, float angle, float speed, float radius) {
    super(name, description, CharacterType.BULLET, x, y, healthPoints, angle, speed, radius);
  }

}
