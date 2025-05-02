package model.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import grid.CellPosition;
import model.Globals.Collidable;
import model.Globals.DamageDealer;
import model.Globals.Damageable;

import java.util.ArrayList;
import java.util.List;

public final class SpaceCalculator {

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

    /**
     * Calculates the angle (in degrees) between two points.
     * The first 2 parameters are the coordinates of the target point, the last 2
     * are the coordinates of the source point.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float angleBetweenPoints(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;

        return (float) Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Calculates the angle (in degrees) between two points.
     * The first 2 parameters are the coordinates of the target point, the
     * <code>FloatPair</code> holds the coordinates of the source point.
     *
     * @param x
     * @param y
     * @param source
     * @return
     */
    public static float angleBetweenPoints(float x, float y, FloatPair source) {
        return angleBetweenPoints(x, y, source.x(), source.y());
    }

    /**
     * Calculates the angle (in degrees) between two points.
     * The first <code>FloatPair</code> holds the coordinates of the target point,
     * the second <code>FloatPair</code> holds the coordinates of the source point.
     *
     * @param target
     * @param source
     * @return
     */
    public static float angleBetweenPoints(FloatPair target, FloatPair source) {
        return angleBetweenPoints(target.x(), target.y(), source.x(), source.y());
    }

    /**
     * Rotates a point around a center of rotation by a given angle.
     *
     * @param point                    the <code>FloatPair</code> to be rotated.
     * @param relativeCenterOfRotation the <code>FloatPair</code> for the relative
     *                                 center of rotation.
     * @param absoluteCenterOfRotation the <code>FloatPair</code> for the absolute
     *                                 center of rotation.
     * @param angle                    the angle in degrees to rotate the point by.
     * @return the rotated and translated <code>FloatPair</code>.
     */
    public static FloatPair rotatePoint(FloatPair point, FloatPair relativeCenterOfRotation,
            FloatPair absoluteCenterOfRotation, float angle) {
        return rotatePoint(point.x(), point.y(), relativeCenterOfRotation, absoluteCenterOfRotation, angle);
    }

    /**
     * Rotates a point around a center of rotation by a given angle.
     *
     * @param x                the x coordinate of the point to be rotated
     * @param y                the y coordinate of the point to be rotated
     * @param centerOfRotation the <code>FloatPair</code> for the relative
     *                         center of rotation.
     * @param translation      the <code>FloatPair</code> for the absolute
     *                         center of rotation.
     * @param angle            the angle in degrees to rotate the point by.
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

    /**
     * Performs one-dimensional linear interpolation.
     *
     * @param source the source or start value
     * @param target the target value
     * @param alpha  the interpolation factor,
     *               should be between 0f and 1f.
     *               A larger <code>alpha</code> gives a closer result to the target
     *               value ("fast lerp"), a smaller <code>alpha</code>
     *               gives a closer result to the source value ("slow lerp").
     * @return the interpolated value.
     */
    public static float lerp1D(float source, float target, float alpha) {
        float alphaInverse = 1f - alpha;
        return alpha * target + alphaInverse * source;
    }

    /**
     * Performs two-dimensional linear interpolation.
     *
     * @param source the source or start <code>FloatPair</code>
     * @param target the target <code>FloatPair</code>
     * @param alpha  the interpolation factor,
     *               should be between 0f and 1f.
     *               A larger <code>alpha</code> gives a closer result to the target
     *               value ("fast lerp"), a smaller <code>alpha</code>
     *               gives a closer result to the source value ("slow lerp").
     * @return the interpolated <code>FloatPair</code>.
     */
    public static FloatPair lerp2D(FloatPair source, FloatPair target, float alpha) {
        float x = lerp1D(source.x(), target.x(), alpha);
        float y = lerp1D(source.y(), target.y(), alpha);
        return new FloatPair(x, y);
    }

    /**
     * Performs two-dimensional linear interpolation.
     *
     * @param source the source or start <code>Vector2</code>
     * @param target the target <code>FloatPair</code>
     * @param alpha  the interpolation factor,
     *               should be between 0f and 1f.
     *               A larger <code>alpha</code> gives a closer result to the target
     *               value ("fast lerp"), a smaller <code>alpha</code>
     *               gives a closer result to the source value ("slow lerp").
     * @return the interpolated <code>FloatPair</code>.
     */
    public static FloatPair lerp2D(Vector2 source, FloatPair target, float alpha) {
        FloatPair sourceFloatPair = new FloatPair(source.x, source.y);
        return lerp2D(sourceFloatPair, target, alpha);
    }

    /**
     * Performs two-dimensional linear interpolation.
     *
     * @param source the source or start <code>Vector3</code>. This only considers
     *               its x and y values. Useful in conjunction with viewport cameras
     *               in libGDX, which use a 3D coordinate system.
     * @param target the target <code>FloatPair</code>
     * @param alpha  the interpolation factor,
     *               should be between 0f and 1f.
     *               A larger <code>alpha</code> gives a closer result to the target
     *               value ("fast lerp"), a smaller <code>alpha</code>
     *               gives a closer result to the source value ("slow lerp").
     * @return the interpolated <code>FloatPair</code>.
     */
    public static FloatPair lerp2D(Vector3 source, FloatPair target, float alpha) {
        FloatPair sourceFloatPair = new FloatPair(source.x, source.y);
        return lerp2D(sourceFloatPair, target, alpha);
    }

    /**
     * Calculates a point at a certain distance from another point.
     *
     * @param posA     the point away from the target point <code>posB</code> which
     *                 defines the direction.
     * @param posB     the point from which the returned <code>FloatPair</code> is
     *                 at the given <code>distance</code> from, on the straight line
     *                 between
     *                 <code>posA</code> and <code>posB</code>.
     * @param distance the distance from <code>posB</code> the returned
     *                 <code>FloatPair</code> is at.
     * @return the <code>FloatPair</code> at the given distance from
     *         <code>posB</code> on the straight line between <code>posA</code> and
     *         <code>posB</code>.
     */
    public static FloatPair getPointAtDistance(FloatPair posA, FloatPair posB, float distance) {
        float dx = posA.x() - posB.x();
        float dy = posA.y() - posB.y();
        float directionVectorLength = distance(dx, dy);

        return new FloatPair(posB.x() + distance * dx / directionVectorLength,
                posB.y() + distance * dy / directionVectorLength);
    }

    /**
     * Determines whether two objects are colliding.
     *
     * @param target1 The first collidable object.
     * @param target2 The second collidable object.
     * @return {@code true} if the two objects are colliding
     *         {@code false} otherwise.
     */
    public static boolean collisionCalculator(Collidable target1, Collidable target2) {
        float dx = target1.getX() - target2.getX();
        float dy = target1.getY() - target2.getY();
        float distance = SpaceCalculator.distance(dx, dy);

        return distance < target1.getRadius() + target2.getRadius();
    }

    /**
     * Calculates the damage of a collision and applies it to the target objects.
     *
     * @param A a <code>Collidable</code> crashing into <code>B</code>.
     * @param B a <code>Collidable</code> crashing into <code>A</code>.
     */
    public static void crash(Collidable A, Collidable B) {
        int damageA = 0;
        int damageB = 0;
        if (A instanceof DamageDealer a) {
            damageA = a.getDamage();
        }
        if (B instanceof DamageDealer b) {
            damageB = b.getDamage();
        }
        if (A instanceof DamageDealer a && B instanceof Damageable b) {
            a.dealDamage(b, damageA);
        }
        if (B instanceof DamageDealer b && A instanceof Damageable a) {
            b.dealDamage(a, damageB);
        }
    }
}