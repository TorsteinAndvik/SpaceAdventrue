package inf112.skeleton.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
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
  void throwsExceptionWhenCoordinatesOutOfBoundsTest() {
    IGrid<String> grid = new Grid<>(3, 3, "x");
    try {
      @SuppressWarnings("unused") String x = grid.get(new CellPosition(3, 1));
      fail();
    } catch (IndexOutOfBoundsException e) {
      //Test passing
    }
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
    assertTrue(items.contains(new GridCell<String>(new CellPosition(0, 0), "a")));
    assertTrue(items.contains(new GridCell<String>(new CellPosition(0, 1), "b")));
    assertTrue(items.contains(new GridCell<String>(new CellPosition(0, 2), "c")));
  }

}

