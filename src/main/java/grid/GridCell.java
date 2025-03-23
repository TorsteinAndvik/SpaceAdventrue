package grid;

/**
 * A GridCell is an object storing a CellPosition, and a value stored at that position.
 *
 * @param pos   A CellPosition, representing the coordinates of the grid-cell.
 * @param value A value stored at that position.
 */
public record GridCell<E>(CellPosition pos, E value) {

}
