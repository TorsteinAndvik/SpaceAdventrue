package model.utils;

import static model.utils.SpaceCalculator.orthogonallyAdjacent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import grid.CellPosition;
import model.SpaceCharacters.Asteroid;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class SpaceCalculatorTest {

        @Test
        void cellPositionAdjacencyTest() {
                CellPosition a = new CellPosition(0, 0);
                CellPosition b = new CellPosition(1, 0);
                assertTrue(orthogonallyAdjacent(a, b));

                a = new CellPosition(0, 1);
                b = new CellPosition(0, 0);
                assertTrue(orthogonallyAdjacent(a, b));

                a = new CellPosition(0, 1);
                b = new CellPosition(1, 0);
                assertFalse(orthogonallyAdjacent(a, b));

                a = new CellPosition(1, 0);
                b = new CellPosition(0, 1);
                assertFalse(orthogonallyAdjacent(a, b));
        }

        @Test
        void cellPositionNeighgboursTest() {
                CellPosition a = new CellPosition(1, 1);
                List<CellPosition> orthNeigh = SpaceCalculator.getOrthogonalNeighbours(a);
                assertTrue(orthNeigh.contains(new CellPosition(0, 1)));
                assertTrue(orthNeigh.contains(new CellPosition(2, 1)));
                assertTrue(orthNeigh.contains(new CellPosition(1, 0)));
                assertTrue(orthNeigh.contains(new CellPosition(1, 2)));
                assertEquals(4, orthNeigh.size());
        }

        private boolean compareVector2(Vector2 a, Vector2 b) {
                float tolerance = 0.0001f;
                return Math.abs(a.x - b.x) < tolerance && Math.abs(a.y - b.y) < tolerance;
        }

        @Test
        void velocityFromAngleSpeedTest() {

                assertTrue(compareVector2(new Vector2(0f, 0f), SpaceCalculator.velocityFromAngleSpeed(0f, 0f)));
                assertTrue(compareVector2(new Vector2(1f, 0f), SpaceCalculator.velocityFromAngleSpeed(0f, 1f)));
                assertTrue(compareVector2(new Vector2(2f, 0f), SpaceCalculator.velocityFromAngleSpeed(0f, 2f)));

                assertTrue(compareVector2(new Vector2(0f, 5f), SpaceCalculator.velocityFromAngleSpeed(90f, 5f)));
                assertTrue(compareVector2(new Vector2(0f, -4f), SpaceCalculator.velocityFromAngleSpeed(270f, 4f)));

                assertTrue(compareVector2(new Vector2(4f, 3f), SpaceCalculator.velocityFromAngleSpeed(36.869857f, 5f)));
        }

        @Test
        void distanceTest() {
                assertEquals(0f, SpaceCalculator.distance(0f, 0f));
                assertEquals((float) Math.sqrt(2f), SpaceCalculator.distance(1f, 1f));

                assertEquals(5f, SpaceCalculator.distance(3f, 4f));
                assertEquals(5f, SpaceCalculator.distance(-3f, 4f));
                assertEquals(5f, SpaceCalculator.distance(3f, -4f));
                assertEquals(5f, SpaceCalculator.distance(-3f, -4f));

                assertEquals(5f, SpaceCalculator.distance(new FloatPair(0f, 0f), new FloatPair(3f, 4f)));
                assertEquals(5f, SpaceCalculator.distance(new FloatPair(-2f, -1f), new FloatPair(1f, 3f)));

                assertEquals(5f, SpaceCalculator.distance(0f, 0f, new FloatPair(3f, 4f)));
                assertEquals(5f, SpaceCalculator.distance(-2f, -1f, new FloatPair(1f, 3f)));
        }

        @Test
        void angleBetweenPointsTest() {
                assertEquals(0f, SpaceCalculator.angleBetweenPoints(0f, 0f, 0f, 0f));
                assertEquals(90f, SpaceCalculator.angleBetweenPoints(0f, 1f, 0f, 0f));
                assertEquals(0f, SpaceCalculator.angleBetweenPoints(1f, 0f, 0f, 0f));
                assertEquals(45f, SpaceCalculator.angleBetweenPoints(1f, 1f, 0f, 0f));

                assertEquals(0f, SpaceCalculator.angleBetweenPoints(0f, 0f, 0f, 0f));
                assertEquals(-90f, SpaceCalculator.angleBetweenPoints(0f, 0f, 0f, 1f));
                assertEquals(180f, SpaceCalculator.angleBetweenPoints(0f, 0f, 1f, 0f));
                assertEquals(-135f, SpaceCalculator.angleBetweenPoints(0f, 0f, 1f, 1f));

                assertEquals(0f, SpaceCalculator.angleBetweenPoints(0f, 0f, new FloatPair(0f, 0f)));
                assertEquals(90f, SpaceCalculator.angleBetweenPoints(0f, 1f, new FloatPair(0f, 0f)));
                assertEquals(0f, SpaceCalculator.angleBetweenPoints(1f, 0f, new FloatPair(0f, 0f)));
                assertEquals(45f, SpaceCalculator.angleBetweenPoints(1f, 1f, new FloatPair(0f, 0f)));

                assertEquals(0f, SpaceCalculator.angleBetweenPoints(0f, 0f, new FloatPair(0f, 0f)));
                assertEquals(-90f, SpaceCalculator.angleBetweenPoints(0f, 0f, new FloatPair(0f, 1f)));
                assertEquals(180f, SpaceCalculator.angleBetweenPoints(0f, 0f, new FloatPair(1f, 0f)));
                assertEquals(-135f, SpaceCalculator.angleBetweenPoints(0f, 0f, new FloatPair(1f, 1f)));

                assertEquals(0f, SpaceCalculator.angleBetweenPoints(new FloatPair(0f, 0f), new FloatPair(0f, 0f)));
                assertEquals(90f, SpaceCalculator.angleBetweenPoints(new FloatPair(0f, 1f), new FloatPair(0f, 0f)));
                assertEquals(0f, SpaceCalculator.angleBetweenPoints(new FloatPair(1f, 0f), new FloatPair(0f, 0f)));
                assertEquals(45f, SpaceCalculator.angleBetweenPoints(new FloatPair(1f, 1f), new FloatPair(0f, 0f)));

                assertEquals(0f, SpaceCalculator.angleBetweenPoints(new FloatPair(0f, 0f), new FloatPair(0f, 0f)));
                assertEquals(-90f, SpaceCalculator.angleBetweenPoints(new FloatPair(0f, 0f), new FloatPair(0f, 1f)));
                assertEquals(180f, SpaceCalculator.angleBetweenPoints(new FloatPair(0f, 0f), new FloatPair(1f, 0f)));
                assertEquals(-135f, SpaceCalculator.angleBetweenPoints(new FloatPair(0f, 0f), new FloatPair(1f, 1f)));
        }

        private boolean compareFloatPair(FloatPair a, FloatPair b) {
                float tolerance = 0.0001f;
                return Math.abs(a.x() - b.x()) < tolerance && Math.abs(a.y() - b.y()) < tolerance;
        }

        @Test
        void rotatePointTest() {
                FloatPair start = new FloatPair(1f, 2f);
                FloatPair translation = new FloatPair(-2f, 3f);
                FloatPair centerOfRotation = new FloatPair(4f, 5f);

                assertTrue(compareFloatPair(new FloatPair(-2f, -1.24264f),
                                SpaceCalculator.rotatePoint(start, centerOfRotation, translation, 45f)));

                assertTrue(compareFloatPair(new FloatPair(-2f, -1.24264f),
                                SpaceCalculator.rotatePoint(start.x(), start.y(), centerOfRotation, translation, 45f)));
        }

        @Test
        void lerp1DTest() {
                assertEquals(0f, SpaceCalculator.lerp1D(0f, 1f, 0f));
                assertEquals(0.5f, SpaceCalculator.lerp1D(0f, 1f, 0.5f));
                assertEquals(1f, SpaceCalculator.lerp1D(0f, 1f, 1f));

                assertEquals(1f, SpaceCalculator.lerp1D(1f, 2f, 0f));
                assertEquals(1.5f, SpaceCalculator.lerp1D(1f, 2f, 0.5f));
                assertEquals(2f, SpaceCalculator.lerp1D(1f, 2f, 1f));

                assertEquals(0f, SpaceCalculator.lerp1D(0f, -1f, 0f));
                assertEquals(-0.5f, SpaceCalculator.lerp1D(0f, -1f, 0.5f));
                assertEquals(-1f, SpaceCalculator.lerp1D(0f, -1f, 1f));

                assertEquals(1f, SpaceCalculator.lerp1D(1f, -2f, 0f));
                assertEquals(-0.5f, SpaceCalculator.lerp1D(1f, -2f, 0.5f));
                assertEquals(-2f, SpaceCalculator.lerp1D(1f, -2f, 1f));
        }

        @Test
        void lerp2DTest() {
                assertTrue(compareFloatPair(new FloatPair(0f, 0f),
                                SpaceCalculator.lerp2D(new FloatPair(0f, 0f), new FloatPair(0f, 0f), 0f)));

                assertTrue(compareFloatPair(new FloatPair(1f, 3f),
                                SpaceCalculator.lerp2D(new FloatPair(1f, 3f), new FloatPair(4f, 4f), 0f)));

                assertTrue(compareFloatPair(new FloatPair(2.5f, 3.5f),
                                SpaceCalculator.lerp2D(new FloatPair(1f, 3f), new FloatPair(4f, 4f), 0.5f)));

                assertTrue(compareFloatPair(new FloatPair(4f, 4f),
                                SpaceCalculator.lerp2D(new FloatPair(1f, 3f), new FloatPair(4f, 4f), 1f)));

                assertTrue(compareFloatPair(new FloatPair(0f, 0f),
                                SpaceCalculator.lerp2D(new Vector2(0f, 0f), new FloatPair(0f, 0f), 0f)));

                assertTrue(compareFloatPair(new FloatPair(1f, 3f),
                                SpaceCalculator.lerp2D(new Vector2(1f, 3f), new FloatPair(4f, 4f), 0f)));

                assertTrue(compareFloatPair(new FloatPair(2.5f, 3.5f),
                                SpaceCalculator.lerp2D(new Vector2(1f, 3f), new FloatPair(4f, 4f), 0.5f)));

                assertTrue(compareFloatPair(new FloatPair(4f, 4f),
                                SpaceCalculator.lerp2D(new Vector2(1f, 3f), new FloatPair(4f, 4f), 1f)));

                assertTrue(compareFloatPair(new FloatPair(0f, 0f),
                                SpaceCalculator.lerp2D(new Vector3(0f, 0f, -3.5f), new FloatPair(0f, 0f), 0f)));

                assertTrue(compareFloatPair(new FloatPair(1f, 3f),
                                SpaceCalculator.lerp2D(new Vector3(1f, 3f, 50f), new FloatPair(4f, 4f), 0f)));

                assertTrue(compareFloatPair(new FloatPair(2.5f, 3.5f),
                                SpaceCalculator.lerp2D(new Vector3(1f, 3f, -77f), new FloatPair(4f, 4f), 0.5f)));

                assertTrue(compareFloatPair(new FloatPair(4f, 4f),
                                SpaceCalculator.lerp2D(new Vector3(1f, 3f, 0f), new FloatPair(4f, 4f), 1f)));
        }

        @Test
        void getPointAtDistanceTest() {
                FloatPair posA = new FloatPair(10f, 8f);
                FloatPair posB = new FloatPair(18f, 6f);
                FloatPair posC = new FloatPair(34f, 2f);
                float distance = 2.5f;

                FloatPair targetA = new FloatPair(15.57464f, 6.60634f); // values from GeoGebra
                FloatPair targetB = new FloatPair(20.42536f, 5.39366f);

                assertTrue(compareFloatPair(targetA, SpaceCalculator.getPointAtDistance(posA, posB, distance)));
                assertTrue(compareFloatPair(targetB, SpaceCalculator.getPointAtDistance(posC, posB, distance)));
        }

        @Test
        void collisionCalculatorTest() {
                Asteroid target1 = new Asteroid("null", "null", 1, 1, 1, 1, 1, 1, 1, 1);
                Asteroid target2 = new Asteroid("null2", "null2", 1, 1, 1, 1, 1, 1, 1, 1);
                assertTrue(SpaceCalculator.collisionCalculator(target1, target2));

                target1 = new Asteroid("null", "null", 1, 1, 1, 1, 1, 1, 10, 1);
                target2 = new Asteroid("null2", "null2", 100, 100, 1, 1, 1, 1, 10, 1);
                assertFalse(SpaceCalculator.collisionCalculator(target1, target2));
        }

}
