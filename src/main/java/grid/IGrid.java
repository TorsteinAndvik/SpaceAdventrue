package grid;

public interface IGrid<E> extends IGridDimension, Iterable<GridCell<E>> {

    /**
     * Sets the value of a position in the grid. A subsequent call to {@link #get} with the same
     * position as argument will return the value which was set. This method will replace any
     * precious value that was stored at a given location.
     *
     * @param pos   the position in which to store a value.
     * @param value the value to store at a given position
     */

    void set(CellPosition pos, E value);

    /**
     * Gets the current value at a given CellPosition (coordinate)
     *
     * @param pos the position to get.
     * @return the value stored at that position.
     * @throws IndexOutOfBoundsException if the position does not exist in the grid.
     */
    E get(CellPosition pos);

    /**
     * Reports whether a position is within bounds for this grid
     *
     * @param pos position to check
     * @return true if the position is within bounds, false otherwise.
     */
    boolean positionIsOnGrid(CellPosition pos);

    /**
     * @return a copy of the grid.
     */
    IGrid<E> copy();

    /**
     * Checks if the specified position in the grid is empty.
     *
     * @param pos the {@code CellPosition} to check.
     * @return {@code true} if the position is empty (i.e., contains {@code null}), {@code false} otherwise.
     */
    boolean isEmptyAt(CellPosition pos);

    /**
     * Checks if the whole grid is empty
     *
     * @return {@code true} if the grid is empty, {@code false} otherwise.
     */
    boolean isEmpty();
}
