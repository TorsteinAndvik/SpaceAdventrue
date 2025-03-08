package inf112.skeleton.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

class CellPositionTest {

    @Test
    void sanityCheck() {
        CellPosition pos = new CellPosition(1, 1);
        assertEquals(1, pos.col());
        assertEquals(1, pos.row());
    }

    @Test
    void coordinateEqualityTest() {
        CellPosition a = new CellPosition(1, 1);
        CellPosition b = new CellPosition(1, 1);

        assertNotSame(a, b);
        assertEquals(a, b);
    }

    @Test
    void coordinateNotEqualTest() {
        CellPosition a = new CellPosition(1, 1);
        CellPosition b = new CellPosition(1, 2);

        assertNotSame(a, b);
        assertNotEquals(a, b);
    }

    @Test
    void coordinateHashCodeTest() {
        CellPosition a = new CellPosition(1, 1);
        CellPosition b = new CellPosition(1, 1);
        assertEquals(a.hashCode(), b.hashCode());

        CellPosition c = new CellPosition(1, 2);
        CellPosition d = new CellPosition(1, 2);
        assertEquals(c.hashCode(), d.hashCode());
    }
}