package model.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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

    public static float distance(FloatPair posA, FloatPair posB) {
        return distance(posA.x() - posB.x(), posA.y() - posB.y());
    }

    public static float distance(float x, float y, FloatPair pos) {
        return distance(x - pos.x(), y - pos.y());
    }

    public static float angleBetweenPoints(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;

        return (float) Math.toDegrees(Math.atan2(y, x));
    }

    public static float angleBetweenPoints(float x, float y, FloatPair pos) {
        return angleBetweenPoints(x, y, pos.x(), pos.y());
    }

    public static float angleBetweenPoints(FloatPair posA, FloatPair posB) {
        return angleBetweenPoints(posA.x(), posA.y(), posB.x(), posB.y());
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

        float r = distance(x, y, centerOfRotation);

        float offsetAngle = angleBetweenPoints(x, y, centerOfRotation);

        float x0 = r * (float) Math.cos(Math.toRadians(angle + offsetAngle));
        float y0 = r * (float) Math.sin(Math.toRadians(angle + offsetAngle));

        float x1 = translation.x() + x0;
        float y1 = translation.y() + y0;

        return new FloatPair(x1, y1);
    }

    // TODO: Write complete javadocs
    /**
     * perform one-dimensional linear interpolation
     */
    public static float lerp1D(float source, float target, float alpha) {
        float alphaInverse = 1f - alpha;
        return alpha * target + alphaInverse * source;
    }

    /**
     * perform two-dimensional linear interpolation
     */
    public static FloatPair lerp2D(FloatPair source, FloatPair target, float alpha) {
        float x = lerp1D(source.x(), target.x(), alpha);
        float y = lerp1D(source.y(), target.y(), alpha);
        return new FloatPair(x, y);
    }

    /**
     * perform two-dimensional linear interpolation
     */
    public static FloatPair lerp2D(Vector2 source, FloatPair target, float alpha) {
        FloatPair sourceFloatPair = new FloatPair(source.x, source.y);
        return lerp2D(sourceFloatPair, target, alpha);
    }

    /**
     * perform two-dimensional linear interpolation
     */
    public static FloatPair lerp2D(Vector3 source, FloatPair target, float alpha) {
        FloatPair sourceFloatPair = new FloatPair(source.x, source.y);
        return lerp2D(sourceFloatPair, target, alpha);
    }

    public static FloatPair getPointAtDistance(FloatPair posA, FloatPair posB, float distance) {
        float dx = posA.x() - posB.x();
        float dy = posA.y() - posB.y();
        float directionVectorLength = distance(dx, dy);

        return new FloatPair(posB.x() + distance * dx / directionVectorLength,
                posB.y() + distance * dy / directionVectorLength);
    }
}