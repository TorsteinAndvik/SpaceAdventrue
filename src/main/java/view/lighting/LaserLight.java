package view.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import view.SpaceScreen;

public class LaserLight extends PointLight implements Poolable {

    public static final int defaultRayNum = 10;
    public static final Color defaultColor = Color.CYAN;
    public static final float defaultDistance = 1.5f;

    public LaserLight(RayHandler rayHandler, int rays, Color color, float distance, float x, float y) {
        super(rayHandler, rays, color, distance, x, y);
        setActive(false);
    }

    public LaserLight() {
        this(SpaceScreen.rayHandler, defaultRayNum, defaultColor, defaultDistance, 0f, 0f);
    }

    @Override
    public void reset() {
        this.setPosition(0f, 0f);
        this.setActive(false);
    }
}
