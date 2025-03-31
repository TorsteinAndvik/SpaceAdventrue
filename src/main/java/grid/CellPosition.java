package grid;

/**
 * A CellPosition is an object that stores two integers. Together they make for a coordinate within
 * a grid.
 *
 * @param row an int representing the row of the object.
 * @param col an int representing the column of the object.
 */
public record CellPosition(int row, int col) {

    public CellPosition offset(int offsetRow, int offsetCol) {
        return new CellPosition(this.row() + offsetRow, this.col() + offsetCol);
    }
}
