package grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import model.ShipComponents.Components.Fuselage;
import org.junit.jupiter.api.Test;

class GridTest {

    @Test
    void gridGetRowsAndColumnsTest() {
        IGrid<Integer> grid = new Grid<>(3, 3);
        assertEquals(3, grid.rows());
        assertEquals(3, grid.cols());
    }

    @Test
    void gridSanityTest() {
        String defaultValue = "x";
        IGrid<String> grid = new Grid<>(3, 3, defaultValue);

        assertEquals(3, grid.rows());
        assertEquals(3, grid.cols());

        assertEquals("x", grid.get(new CellPosition(0, 0)));
        assertEquals("x", grid.get(new CellPosition(0, 1)));

        grid.set(new CellPosition(1, 1), "y");

        assertEquals("y", grid.get(new CellPosition(1, 1)));
        assertEquals("x", grid.get(new CellPosition(1, 2)));
        assertEquals("x", grid.get(new CellPosition(2, 2)));
    }

    @Test
    void gridCanHoldNullTest() {
        String defaultValue = "x";
        IGrid<String> grid = new Grid<>(3, 3, defaultValue);
        assertEquals("x", grid.get(new CellPosition(0, 0)));
        assertEquals("x", grid.get(new CellPosition(0, 1)));

        grid.set(new CellPosition(1, 1), null);

        assertNull(grid.get(new CellPosition(1, 1)));
        assertEquals("x", grid.get(new CellPosition(1, 2)));
        assertEquals("x", grid.get(new CellPosition(2, 2)));
    }

    @Test
    void gridNullsInDefaultConstructorTest() {
        IGrid<String> grid = new Grid<>(3, 3);
        assertNull(grid.get(new CellPosition(0, 0)));
        assertNull(grid.get(new CellPosition(0, 1)));

        grid.set(new CellPosition(1, 1), "y");
        assertEquals("y", grid.get(new CellPosition(1, 1)));
        assertNull(grid.get(new CellPosition(1, 2)));
        assertNull(grid.get(new CellPosition(2, 2)));

    }

    @Test
    void throwsExceptionWhenGivenInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new Grid<>(-1, 3));
        assertThrows(IllegalArgumentException.class, () -> new Grid<>(5, 0));
    }

    @Test
    void throwsExceptionWhenCoordinatesOutOfBoundsTest() {
        IGrid<String> grid = new Grid<>(3, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> grid.get(new CellPosition(3, 1)));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.get(new CellPosition(1, 5)));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.get(new CellPosition(-1, 2)));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.get(new CellPosition(0, -112)));
    }

    @Test
    void iteratorTest() {
        IGrid<String> grid = new Grid<>(3, 3, "x");
        grid.set(new CellPosition(0, 0), "a");
        grid.set(new CellPosition(0, 1), "b");
        grid.set(new CellPosition(0, 2), "c");

        List<GridCell<String>> items = new ArrayList<>();
        for (GridCell<String> cell : grid) {
            items.add(cell);
        }
        assertEquals(3 * 3, items.size());
        assertTrue(items.contains(new GridCell<>(new CellPosition(0, 0), "a")));
        assertTrue(items.contains(new GridCell<>(new CellPosition(0, 1), "b")));
        assertTrue(items.contains(new GridCell<>(new CellPosition(0, 2), "c")));
    }

    @Test
    void shrinkToFitTest() {
        IGrid<Fuselage> emptyGrid = new Grid<>(3, 4);
        assertEquals(1, Grid.shrinkGridToFit(emptyGrid).rows());
        assertEquals(1, Grid.shrinkGridToFit(emptyGrid).cols());

        IGrid<Fuselage> grid = new Grid<>(3, 3);
        grid.set(new CellPosition(2, 1), new Fuselage());

        assertEquals(3, grid.rows());
        assertEquals(3, grid.cols());
        assertFalse(grid.isEmptyAt(new CellPosition(2, 1)));

        grid = Grid.shrinkGridToFit(grid);

        assertEquals(1, grid.rows());
        assertEquals(1, grid.cols());
        assertFalse(grid.isEmptyAt(new CellPosition(0, 0)));
    }

    @Test
    void toStringTest() {
        IGrid<String> emptyGrid = new Grid<>(1, 1);
        assertEquals("Grid is empty.", emptyGrid.toString());

        IGrid<String> grid = new Grid<>(2, 2);
        grid.set(new CellPosition(0, 0), "first element");
        grid.set(new CellPosition(0, 1), "second");

        assertEquals(
                "[GridCell[pos=CellPosition[row=0, col=0], value=first element], GridCell[pos=CellPosition[row=0, col=1], value=second]]",
                grid.toString());
    }

    @Test
    void testIsEmpty() {
        IGrid<Integer> grid = new Grid<>(2, 2);
        assertTrue(grid.isEmpty());
        grid.set(new CellPosition(0, 0), 1);
        assertFalse(grid.isEmpty());

    }
}
