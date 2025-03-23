package inf112.skeleton.model.World;


//@TODO what are some more handy in-game item features?
public interface GameItem {


    /**
     * Get the name of the item.
     *
     * @return the name of the item.
     */
    String getName();

    /**
     * Get a description of the item.
     *
     * @return a string description of an item.
     */
    String getItemDescription();
}
