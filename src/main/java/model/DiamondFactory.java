package model;

import com.badlogic.gdx.utils.Pool;
import java.util.Random;
import model.SpaceCharacters.Diamond;
import model.SpaceCharacters.ViewableSpaceBody;

public class DiamondFactory {

    private final Random random = new Random();
    private final Pool<Diamond> diamondPool = new Pool<>() {
        @Override
        protected Diamond newObject() {
            return new Diamond();
        }
    };

    /**
     * Spawns a {@link Diamond} instance and places it relative to the given {@link ViewableSpaceBody}.
     * The diamond’s value is randomly determined based on the resource value of the parent.
     *
     * @param parent the space object (e.g., asteroid) where the diamond is spawned
     * @return a {@link Diamond} instance
     */
    public Diamond spawn(ViewableSpaceBody parent) {
        Diamond diamond = diamondPool.obtain();
        diamond.setX(parent.getX() + (parent.getRadius() / 2 - diamond.getRadius() / 2));
        diamond.setY(parent.getY() + (parent.getRadius() / 2 - diamond.getRadius() / 2));

        int minValue = parent.getResourceValue() / 2 + 1;
        int value = random.nextInt(minValue) + minValue;
        diamond.setValue(value);
        return diamond;
    }

    /**
     * Pre-fills the internal pool with a specified number of {@link Diamond} instances.
     *
     * @param diamondPreFill number of diamond instances to pre-allocate
     */
    public void fill(int diamondPreFill) {
        diamondPool.fill(diamondPreFill);
    }

    /**
     * Frees a {@link Diamond} instance and returns it to the pool for reuse.
     *
     * @param diamond the diamond instance to release
     */
    public void free(Diamond diamond) {
        diamondPool.free(diamond);
    }
}
