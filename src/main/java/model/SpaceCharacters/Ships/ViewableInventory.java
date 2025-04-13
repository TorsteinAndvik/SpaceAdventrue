package model.SpaceCharacters.Ships;

import model.World.GameItem;

public interface ViewableInventory {

    /**
     * @return The number of resources in the inventory
     */
    int getResourceCount();

    /**
     * Check if the inventory can hold more {@code Items}
     *
     * @return {@code true} if it can hold more, {@code false} otherwise.
     */
    boolean hasCapacity();

    /**
     * Check if the inventory can hold more of a given {@code Item}
     *
     * @param item The item to check
     * @return {@code true} if it can hold more, {@code false} otherwise.
     */
    boolean hasItemCapacity(GameItem item);

    /**
     * Checks if the inventory holds at least the specified amount of resources.
     *
     * @param amount The amount of resources to check for.
     * @return {@code true} if the inventory has at least the given amount, {@code false} otherwise.
     */
    boolean canAfford(int amount);


}
