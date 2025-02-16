package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.World.GameItem;

/**
 * Inventory is responsible for tracking character items.
 */
public interface Inventory {

  /**
   * Add an item to the inventory. Check if capacity.
   *
   * @return true if added to inventory, false otherwise.
   */
  boolean addToInventory(GameItem item);

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
}
