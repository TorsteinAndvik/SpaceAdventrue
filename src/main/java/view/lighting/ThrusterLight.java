package view.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool.Poolable;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import view.Palette;
import view.screens.SpaceScreen;

public class ThrusterLight extends ConeLight implements Poolable {

    public static final int defaultRayNum = 12;
    // public static final Color defaultColor = Color.YELLOW;
    public static final Color defaultColor = Palette.THRUSTER_LIGHT;
    public static final float defaultDistance = 2.4f;
    public static final float defaultConeDegree = 24f;

    public ThrusterLight(RayHandler rayHandler, int rays, Color color, float distance, float x, float y,
            float directionDegree, float coneDegree) {
        super(rayHandler, rays, color, distance, x, y, directionDegree, coneDegree);
        setActive(false);
    }

    public ThrusterLight() {
        this(SpaceScreen.rayHandler, defaultRayNum, defaultColor, defaultDistance, 0f, 0f, 0f, defaultConeDegree);
    }

    @Override
    public void reset() {
        this.setPosition(0f, 0f);
        this.setDirection(0f);
        this.setConeDegree(0f);
        this.setActive(false);
    }
}
