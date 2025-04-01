package model.SpaceCharacters;

import model.World.GameItem;

/**
 * Inventory is responsible for tracking character items.
 */
public interface Inventory {

    int ITEM_CAPACITY = 10;
    int INVENTORY_ITEM_CAPACITY = 100;


    /**
     * Add an item to the inventory. Check if capacity.
     */
    boolean addItem(GameItem item);

    /**
     * Lists character inventory.
     *
     * @return a string representation of the character inventory.
     */
    String listInventory();

    /**
     * Check if character has given item in their inventory,
     *
     * @param item an item to check for.
     * @return true if item is in inventory, false otherwise.
     */
    boolean containsItem(GameItem item);

    /**
     * Use an item from the inventory. If the item is the last of its kind in the inventory do not
     * delete key in mapping, instead set its counter to null.
     *
     * @param item an item to use.
     * @return a game item.
     */
    boolean use(GameItem item);

    /**
     * Get an item by its name.
     *
     * @param name the name of the item.
     * @return a GameItem, if it exists.
     */
    GameItem getItemByName(String name);

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
     * Add an amount of resources to the inventory
     *
     * @param amount The amount of resources to add
     */
    void addResource(int amount);

    /**
     * Spend an amount of resources.
     *
     * @param amount The amount of resources to spend
     * @return true if the resources was spent, false otherwise..
     */
    boolean spendResources(int amount);

    /**
     * Checks if the inventory holds at least the specified amount of resources.
     *
     * @param amount The amount of resources to check for.
     * @return {@code true} if the inventory has at least the given amount, {@code false} otherwise.
     */
    boolean hasResourceAmount(int amount);

}
