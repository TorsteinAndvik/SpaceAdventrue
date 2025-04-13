package grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class GridCellTest {

    @Test
    void sanityTest() {
        String item = "Test";
        CellPosition pos = new CellPosition(1, 2);
        GridCell<String> gridCell = new GridCell<>(pos, item);

        assertEquals(pos, gridCell.pos());
        assertEquals(item, gridCell.value());
    }

    @Test
    void gridCellEqualityAndHashCodeTest() {
        String item = "Test";
        CellPosition pos = new CellPosition(1, 2);
        GridCell<String> gridCell = new GridCell<>(pos, item);

        String otherItem = "Test";
        CellPosition otherPos = new CellPosition(1, 2);
        GridCell<String> otherGridCell = new GridCell<>(otherPos, otherItem);

        assertEquals(otherGridCell, gridCell);
        assertEquals(otherGridCell.hashCode(), gridCell.hashCode());
    }

    @Test
    void gridCellInequalityTest() {
        String item = "Test";
        CellPosition pos = new CellPosition(1, 2);
        GridCell<String> gridCell = new GridCell<>(pos, item);

        String otherItem = "Test2";
        CellPosition otherPos = new CellPosition(2, 2);
        GridCell<String> otherGridCell = new GridCell<>(otherPos, item);
        GridCell<String> otherGridCell2 = new GridCell<>(pos, otherItem);

        assertNotEquals(gridCell, otherGridCell);
        assertNotEquals(gridCell, otherGridCell2);
        assertNotEquals(otherGridCell, otherGridCell2);
    }
}