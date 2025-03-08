package inf112.skeleton.model.SpaceCharacters;

import com.badlogic.gdx.math.Vector2;

public class Asteroid extends Projectile {
  private static final float ROTATION_SPEED = 0.01f;

  public Asteroid(String name, String description, float x, float y, float vX, float vY, int hitPoints, float mass, float angle, float radius) {
    super(name, description, CharacterType.ASTEROID, x, y, vX, vY, hitPoints, mass, angle, radius, ROTATION_SPEED);
  }

}
