package model.SpaceCharacters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.SpaceCharacters.Ships.Inventory;
import model.SpaceCharacters.Ships.PlayerInventory;
import model.World.GameItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerInventoryTest {

    private PlayerInventory inventory;
    GameItem item;

    @BeforeEach
    void setup() {
        inventory = new PlayerInventory();
        item = new GameItem("Ray gun", "An old gun. It doesn't work.");
    }

    @Test
    void containsItemTest() {

        assertFalse(inventory.containsItem(item));

        inventory.addItem(item);

        assertTrue(inventory.containsItem(item));
    }

    @Test
    void containsIdenticalItemTest() {
        GameItem identicalItem = new GameItem("Ray gun", "An old gun. It doesn't work.");
        inventory.addItem(item);
        assertTrue(inventory.containsItem(identicalItem));
    }

    @Test
    void getItemByNameTest() {
        inventory.addItem(item);
        assertEquals(item, inventory.getItemByName("Ray gun"));
    }

    @Test
    void addMoreThanItemCapacityTest() {
        for (int i = 0; i < Inventory.ITEM_CAPACITY; i++) {
            assertTrue(inventory.hasItemCapacity(item));
            assertTrue(inventory.addItem(item));
        }

        assertFalse(inventory.hasItemCapacity(item));
        assertFalse(inventory.addItem(item));
    }

    @Test
    void addMoreThanInventoryCapacityTest() {
        for (int i = 0; i < Inventory.INVENTORY_ITEM_CAPACITY; i++) {
            assertTrue(inventory.hasCapacity());
            assertTrue(inventory.addItem(new GameItem(String.valueOf(i), "description")));
        }

        assertFalse(inventory.hasCapacity());
        assertFalse(inventory.addItem(item));
    }

    @Test
    void useItemTest() {
        assertFalse(inventory.containsItem(item));
        assertFalse(inventory.use(item));

        inventory.addItem(item);
        assertTrue(inventory.containsItem(item));
        assertTrue(inventory.use(item));

        assertFalse(inventory.use(item));
        assertFalse(inventory.containsItem(item));
    }

    @Test
    void addResourceTest() {
        assertFalse(inventory.hasResourceAmount(1));

        inventory.addResource(100);
        assertTrue(inventory.hasResourceAmount(100));

        assertFalse(inventory.hasResourceAmount(101));

    }

    @Test
    void spendResourceTest() {
        inventory.addResource(100);
        assertFalse(inventory.spendResources(101));
        assertTrue(inventory.spendResources(49));
        assertFalse(inventory.hasResourceAmount(100));
        assertTrue(inventory.hasResourceAmount(51));
        assertTrue(inventory.spendResources(51));
    }

    @Test
    void listInventoryTest() {

        assertEquals("Inventory is empty.", inventory.listInventory());
        inventory.addResource(1000);

        String inventoryContent = """
                Inventory:
                Resources    1000""";
        assertEquals(inventoryContent, inventory.listInventory());

        inventory.addItem(new GameItem("Hammer", "Super heavy"));
        for (int n = 0; n < 10; n++) {
            inventory.addItem(new GameItem("Nail", "Old and rusty"));
        }
        inventory.addItem(new GameItem("Piece of wood", "44.5 inches"));
        inventory.addItem(new GameItem("Piece of wood", "44.5 inches"));
        inventory.addItem(new GameItem("Piece of wood", "45.0 inches"));

        inventoryContent = """
                Inventory:
                Resources                      1000
                Hammer (Super heavy)           1
                Nail (Old and rusty)           10
                Piece of wood (45.0 inches)    1
                Piece of wood (44.5 inches)    2""";
        assertEquals(inventoryContent, inventory.listInventory());
    }
}
