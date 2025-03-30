package grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This grid class is highly similar to one I wrote for INF101. There are some improvements, but
 * this class, its interfaces and related objects such as CellPosition are not novel for this
 * project.
 * <p>
 * There are only so many ways to skin a cat, or to implement a simple grid, see <a href=
 * "https://git.app.uib.no/ii/inf101/23v/assignments/Henrik.Dalsto_sem1-tetris">...</a> for
 * original.
 *
 * @param <E>
 * @author Henrik Dalstø
 */

public class Grid<E> implements IGrid<E> {

    private final List<List<GridCell<E>>> grid;
    private final int columns;
    private final int rows;

    /**
     * Construct a grid with {@code rows} rows and {@code columns} columns and null with default
     * value.
     *
     * @param rows    the number of rows in the grid
     * @param columns the number of columns in the grid
     */
    public Grid(int rows, int columns) {
        this(rows, columns, null);
    }

    /**
     * Construct a grid with a given {@code rows} number of rows and {@code columns} columns, and an
     * {@code elem} element stored at each cell position.
     *
     * @param rows    the number of rows
     * @param columns the number of columns
     * @param elem    elements to be stored at each CellPosition
     * @throws IllegalArgumentException if supplied either number of rows or columns < 1.
     */
    public Grid(int rows, int columns, E elem) throws IllegalArgumentException {
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("A grid must have positive integer indices");
        }
        this.columns = columns;
        this.rows = rows;
        this.grid = initializeGrid(rows, columns, elem);
    }

    private List<List<GridCell<E>>> initializeGrid(int rows, int columns, E elem) {
        List<List<GridCell<E>>> newGrid = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            List<GridCell<E>> row = new ArrayList<>(columns);
            for (int c = 0; c < columns; c++) {
                CellPosition pos = new CellPosition(r, c);
                row.add(new GridCell<>(pos, elem));
            }
            newGrid.add(row);
        }
        return newGrid;
    }

    @Override
    public void set(CellPosition pos, E value) throws IndexOutOfBoundsException {
        checkPosition(pos);
        grid.get(pos.row()).set(pos.col(), new GridCell<>(pos, value));
    }

    @Override
    public E get(CellPosition pos) throws IndexOutOfBoundsException {
        checkPosition(pos);
        return grid.get(pos.row()).get(pos.col()).value();
    }

    @Override
    public boolean positionIsOnGrid(CellPosition pos) {
        return (pos.row() >= 0) && (pos.row() < rows()) && (pos.col() >= 0) && (pos.col() < cols());
    }

    private void checkPosition(CellPosition pos) throws IndexOutOfBoundsException {
        if (!positionIsOnGrid(pos)) {
            throw new IndexOutOfBoundsException("Position is not on the grid");
        }
    }

    @Override
    public int rows() {
        return this.rows;
    }

    @Override
    public int cols() {
        return this.columns;
    }

    @Override
    public Iterator<GridCell<E>> iterator() {
        List<GridCell<E>> cells = new ArrayList<>();
        for (List<GridCell<E>> row : grid) {
            cells.addAll(row);
        }
        return cells.iterator();
    }

    @Override
    public IGrid<E> copy() {
        IGrid<E> copy = new Grid<>(rows, columns);
        for (GridCell<E> cell : this) {
            copy.set(cell.pos(), cell.value());
        }
        return copy;
    }

    @Override
    public boolean isEmptyAt(CellPosition pos) {
        return get(pos) == null;
    }

    /**
     * Shrinks a given grid to the minimal bounding box that contains all elements.
     * Removes excess rows and columns around the content while preserving relative
     * positions.
     *
     * @param expandedGrid the input grid that may contain excess empty space
     * @return a new grid that fits exactly around the non-null elements, with all positions
     * adjusted accordingly
     */
    public static <E> IGrid<E> shrinkGridToFit(IGrid<E> expandedGrid) {
        int minRow = Integer.MAX_VALUE;
        int maxRow = 0;
        int minCol = Integer.MAX_VALUE;
        int maxCol = 0;

        for (GridCell<E> cell : expandedGrid) {
            if (cell.value() != null) {
                int row = cell.pos().row();
                int col = cell.pos().col();
                minRow = Math.min(minRow, row);
                maxRow = Math.max(maxRow, row);
                minCol = Math.min(minCol, col);
                maxCol = Math.max(maxCol, col);
            }
        }

        if (minRow > maxRow || minCol > maxCol) {
            return new Grid<>(1, 1);
        }

        int newRows = maxRow - minRow + 1;
        int newCols = maxCol - minCol + 1;
        IGrid<E> shrunkGrid = new Grid<>(newRows, newCols);

        for (GridCell<E> cell : expandedGrid) {
            if (cell.value() != null) {
                int row = cell.pos().row() - minRow;
                int col = cell.pos().col() - minCol;
                CellPosition newPos = new CellPosition(row, col);
                shrunkGrid.set(newPos, cell.value());
            }
        }

        return shrunkGrid;
    }


}
