package model;

import com.badlogic.gdx.math.Rectangle;

public interface ScreenBoundsProvider {
    /**
     * Creates a <code>Rectangle/<code> objects that represent the current
     * bounds of the space. 
     * 
     * The bounds changes as the location of the player changes.
     * They should therefore not be stored once, but rather calculated on the spot.
     * 
     * @return a <code>Ractangle</code> object
     */
    public Rectangle getBounds();
}
