package inf112.skeleton.model.utils;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceCalculatorTest {

  @Test
  void velocityFromAngleSpeedDefault() {
    Vector2 velocity = SpaceCalculator.velocityFromAngleSpeed(0, 0);
    assertEquals(new Vector2(0, 0), velocity);
  }

  @Test
  void velocityFromAngleSpeed10North() {
    Vector2 velocity = SpaceCalculator.velocityFromAngleSpeed(90, 10);
    float delta = 1e-6f;
    assertEquals(0, velocity.x, delta);
    assertEquals(10, velocity.y);
  }

  @Test
  void velocityFromAngleSpeed10SouthWest() {
    Vector2 velocity = SpaceCalculator.velocityFromAngleSpeed(225, 10);
    float delta = 0.001f;
    assertEquals(-7.071, velocity.x, delta);
    assertEquals(-7.071, velocity.y, delta);
  }

  @Test
  void velocityFromAngleSpeed20SouthWest() {
    Vector2 velocity = SpaceCalculator.velocityFromAngleSpeed(225, 20);
    float delta = 0.001f;
    assertEquals(-14.142, velocity.x, delta);
    assertEquals(-14.142, velocity.y, delta);
  }
}
