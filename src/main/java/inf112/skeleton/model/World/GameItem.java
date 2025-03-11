package inf112.skeleton.model.World;

import inf112.skeleton.model.Globals.ItemType;

//@TODO what are some more handy in-game item features?
public interface GameItem {

    /**
     * Get the type of in-game item.
     *
     * @return an item type.
     */
    ItemType getItemType();

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
