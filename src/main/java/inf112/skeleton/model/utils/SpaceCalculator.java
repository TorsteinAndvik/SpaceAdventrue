package inf112.skeleton.model.utils;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.grid.CellPosition;
import java.util.ArrayList;
import java.util.List;

public class SpaceCalculator {

    /**
     * Calculates the velocity vector given an angle (in degrees) and speed.
     *
     * @param angle The angle in degrees, where 0° points to the right (positive x-axis).
     * @param speed The speed value, which must be non-negative.
     * @return A Vector2 representing the velocity in the x and y directions.
     * @throws IllegalArgumentException if the speed is negative.
     */
    public static Vector2 velocityFromAngleSpeed(float angle, float speed) {
        ArgumentChecker.greaterOrEqualToZero(speed, "Speed can't be negative.");
        float radians = (float) Math.toRadians(angle);
        float velocityX = speed * (float) Math.cos(radians);
        float velocityY = speed * (float) Math.sin(radians);
        return new Vector2(velocityX, velocityY);
    }

    /**
     * Checks if two cell positions are orthogonally adjacent (horizontally or vertically). Diagonal
     * adjacency is not considered.
     *
     * @param a The first CellPosition.
     * @param b The second CellPosition.
     * @return true if the positions are adjacent, otherwise false.
     */
    public static boolean orthogonallyAdjacent(CellPosition a, CellPosition b) {
        int dx = Math.abs(a.col() - b.col());
        int dy = Math.abs(a.row() - b.row());
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

    public static List<CellPosition> getOrthogonalNeighbours(CellPosition a) {
        List<CellPosition> neighbours = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            neighbours.add(new CellPosition(a.row() + dir[0], a.col() + dir[1]));
        }
        return neighbours;
    }
}