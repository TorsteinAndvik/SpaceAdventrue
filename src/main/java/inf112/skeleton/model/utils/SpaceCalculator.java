package inf112.skeleton.model.utils;

import com.badlogic.gdx.math.Vector2;

public class SpaceCalculator {

    public static Vector2 velocityFromAngleSpeed(float angle, float speed) {
        ArgumentChecker.greaterOrEqualToZero(speed, "Speed can't be negative.");
        float radians = (float) Math.toRadians(angle);
        float velocityX = speed * (float) Math.cos(radians);
        float velocityY = speed * (float) Math.sin(radians);
        return new Vector2(velocityX, velocityY);
    }
}