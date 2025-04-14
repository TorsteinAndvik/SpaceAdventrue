package model.SpaceCharacters;

import com.badlogic.gdx.utils.Pool.Poolable;
import model.Globals.Collectable;
import model.utils.FloatPair;

public class Diamond extends SpaceBody implements Poolable, Collectable {

    private int value;

    /**
     * Creates a diamond.
     *
     * @param value    the name of the thing.
     * @param position the description of the thing.
     */
    public Diamond(int value, FloatPair position) {
        super("Diamond", "A shiny resource worth a lot", CharacterType.DIAMOND);
        this.value = value;
        setX(position.x());
        setY(position.y());
    }

    /**
     * Creates a diamond with no value placed at (x=0, y=0)
     */
    public Diamond() {
        this(0, new FloatPair(0, 0));
    }

    @Override
    public int getValue() {
        return value;
    }


    @Override
    public float getRadius() {
        return 0.1f;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public void reset() {
        value = 0;
        setX(0);
        setY(0);
    }
}