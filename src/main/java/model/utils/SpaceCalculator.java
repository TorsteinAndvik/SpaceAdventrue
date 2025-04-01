package model.utils;

import com.badlogic.gdx.math.Vector2;
import grid.CellPosition;

import java.util.ArrayList;
import java.util.List;

public class SpaceCalculator {

    /**
     * Calculates the velocity vector given an angle (in degrees) and speed.
     *
     * @param angle The angle in degrees, where 0° points to the right (positive
     *              x-axis).
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
     * Checks if two cell positions are orthogonally adjacent (horizontally or
     * vertically). Diagonal
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

    /**
     * Returns a list of orthogonally adjacent (non-diagonal) neighboring positions
     * for a given {@code CellPosition}.
     *
     * @param cellPosition the central {@code CellPosition} for which to find
     *                     neighbors.
     * @return a list of four orthogonally adjacent {@code CellPosition}s (up, down,
     *         left, right).
     */
    public static List<CellPosition> getOrthogonalNeighbours(CellPosition cellPosition) {
        List<CellPosition> neighbours = new ArrayList<>();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int[] dir : directions) {
            neighbours.add(
                    new CellPosition(cellPosition.row() + dir[0], cellPosition.col() + dir[1]));
        }
        return neighbours;
    }

    public static float distance(float x, float y) {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * Rotates a point around a center of rotation by a given angle, and translates
     * it.
     * 
     * @param point            the <code>FloatPair</code> to be rotated
     * @param centerOfRotation the <code>FloatPair</code> at the center of rotation
     * @param translation      the <code>FloatPair</code> the rotated point will be
     *                         translated by
     * @param angle            the angle in degrees to rotate the point by
     * @return the rotated and translated <code>FloatPair</code>
     */
    public static FloatPair rotatePoint(FloatPair point, FloatPair centerOfRotation, FloatPair translation,
            float angle) {
        return rotatePoint(point.x(), point.y(), centerOfRotation, translation, angle);
    }

    /**
     * Rotates a point's x and y coordinates around a center of rotation by a given
     * angle, and translates it.
     * 
     * @param x                the x coordinate of the point to be rotated
     * @param y                the y coordinate of the point to be rotated
     * @param centerOfRotation the <code>FloatPair</code> at the center of rotation
     * @param translation      the <code>FloatPair</code> the rotated point will be
     *                         translated by
     * @param angle            the angle in degrees to rotate the point by
     * @return the rotated and translated <code>FloatPair</code>
     */
    public static FloatPair rotatePoint(float x, float y, FloatPair centerOfRotation, FloatPair translation,
            float angle) {
        float x0 = x - centerOfRotation.x();
        float y0 = y - centerOfRotation.y();
        float r = SpaceCalculator.distance(x0, y0);

        float offsetAngle = (float) Math.toDegrees(Math.atan2(y0, x0));

        float x1 = r * (float) Math.cos(Math.toRadians(angle + offsetAngle));
        float y1 = r * (float) Math.sin(Math.toRadians(angle + offsetAngle));

        float x2 = translation.x() + x1;
        float y2 = translation.y() + y1;

        return new FloatPair(x2, y2);
    }
}