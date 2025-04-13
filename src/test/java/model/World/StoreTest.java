package model.World;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class StoreTest {

    private Store<String> store;

    @BeforeEach
    void setUp() {
        store = new Store<>();
    }

    @Test
    void testAddToStock() {
        store.addToStock("ItemA");
        assertTrue(store.isInStock("ItemA"));
    }

    @Test
    void testRemoveFromStock_Success() {
        store.addToStock("ItemA");
        boolean removed = store.removeFromStock("ItemA");
        assertTrue(removed);
        assertFalse(store.isInStock("ItemA"));
    }

    @Test
    void testRemoveFromStock_NotInStock() {
        boolean removed = store.removeFromStock("ItemA");
        assertFalse(removed);
    }

    @Test
    void testIsInStock() {
        store.addToStock("ItemA");
        assertTrue(store.isInStock("ItemA"));
        assertFalse(store.isInStock("ItemB"));
    }

    @Test
    void testGetStock() {
        store.addToStock("ItemA");
        store.addToStock("ItemB");
        List<String> stock = store.getStock();
        assertEquals(2, stock.size());
        assertTrue(stock.contains("ItemA"));
        assertTrue(stock.contains("ItemB"));
    }

    @Test
    void testGetStockSafety() {
        store.addToStock("ItemA");
        List<String> externalStock = store.getStock();
        externalStock.remove("ItemA");

        assertTrue(store.isInStock("ItemA"));
    }
}
