package model.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Store<E> {

    private final Set<E> stock;

    /**
     * Constructs an empty store with no items in stock.
     */
    public Store() {
        stock = new HashSet<>();
    }

    /**
     * Adds an item to the store's stock.
     * If the item already exists, it will not be added again.
     *
     * @param storeItem the item to add to stock
     */
    public void addToStock(E storeItem) {
        stock.add(storeItem);
    }

    /**
     * Removes an item from the store's stock if it exists.
     *
     * @param storeItem the item to remove
     * @return true if the item was present and removed, false otherwise
     */
    public boolean removeFromStock(E storeItem) {
        if (isInStock(storeItem)) {
            return stock.remove(storeItem);
        }
        return false;
    }

    /**
     * Checks if an item is currently in stock.
     *
     * @param storeItem the item to check
     * @return true if the item is in stock, false otherwise
     */
    public boolean isInStock(E storeItem) {
        return stock.contains(storeItem);
    }

    /**
     * Returns a list containing all items currently in stock.
     * The returned list is a copy and modifications to it will not affect the store's internal stock.
     *
     * @return a list of all items in stock
     */
    public List<E> getStock() {
        return new ArrayList<>(stock);
    }
}
