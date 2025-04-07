package model.SpaceCharacters.Ships;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import model.Globals.Collectable;
import model.World.GameItem;

public class PlayerInventory implements Inventory, ViewableInventory {

    private final Map<GameItem, Integer> items;
    private int resources;

    public PlayerInventory() {
        items = new HashMap<>();
        resources = 0;
    }

    @Override
    public boolean addItem(GameItem item) {
        int itemCount = items.getOrDefault(item, 0);
        if (itemCount >= Inventory.ITEM_CAPACITY || !hasCapacity()) {
            return false;
        }
        items.put(item, itemCount + 1);
        return true;
    }

    @Override
    public boolean containsItem(GameItem item) {
        return items.getOrDefault(item, 0) > 0;
    }

    @Override
    public boolean use(GameItem item) {
        if (!containsItem(item)) {
            return false;
        }
        items.put(item, items.get(item) - 1);
        return true;
    }

    @Override
    public GameItem getItemByName(String name) {
        for (GameItem item : items.keySet()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int getResourceCount() {
        return resources;
    }

    @Override
    public boolean hasCapacity() {
        return countAllItems() < INVENTORY_ITEM_CAPACITY;
    }


    @Override
    public boolean hasItemCapacity(GameItem item) {
        return items.getOrDefault(item, 0) < ITEM_CAPACITY;
    }

    @Override
    public void addResource(Collectable c) {
        addResource(c.getValue());
    }

    @Override
    public void addResource(int value) {
        resources += value;
    }

    @Override
    public boolean spendResources(int amount) {
        if (!hasResourceAmount(amount)) {
            return false;
        }
        resources -= amount;
        return true;
    }

    @Override
    public boolean hasResourceAmount(int amount) {
        return resources >= amount;
    }

    private int countAllItems() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String listInventory() {
        if (items.isEmpty() && resources == 0) {
            return "Inventory is empty.";
        }

        StringBuilder inventoryList = new StringBuilder("Inventory:\n");

        int maxLength = 0;
        for (GameItem item : items.keySet()) {
            int length = (item.name() + " (" + item.description() + ")").length();
            maxLength = Math.max(maxLength, length);
        }
        int padding = 4;
        int spaceCount = Math.max(maxLength - "Resources".length(), 0);
        String space = " ".repeat(spaceCount + padding);
        inventoryList.append("Resources").append(space).append(resources).append("\n");

        List<Entry<GameItem, Integer>> sortedItems = new ArrayList<>(items.entrySet());
        sortedItems.sort(Comparator.comparing(entry -> entry.getKey().name()));

        for (Entry<GameItem, Integer> entry : sortedItems) {
            String itemInfo = entry.getKey().name() + " (" + entry.getKey().description() + ")";
            space = " ".repeat(maxLength - itemInfo.length() + padding);
            inventoryList.append(itemInfo).append(space).append(entry.getValue()).append("\n");
        }
        return inventoryList.toString().trim();
    }

}
