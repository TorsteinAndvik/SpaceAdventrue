package model;

import model.utils.FloatPair;

public interface ScreenBoundsProvider {
    /**
     * Creates an array of <code>FloatPair</code> objects that represent the current
     * bounds of the space. They are ordered as follows:
     * 
     * <pre>
     * bottom-left, bottom-right, top-right, top-left
     * </pre>
     * 
     * The array changes as the location of the player changes.
     * It should therefore not be stored once, but rather calculated on the spot.
     * 
     * @return an array of <code>FloatPair</code> objects
     */
    public FloatPair[] getBounds();
}
