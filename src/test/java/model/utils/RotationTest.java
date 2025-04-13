package model.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RotationTest {

    @Test
    void SetterGetterTest() {
        Rotation r = new Rotation(0);
        assertEquals(0, r.getAngle());
        r.setAngle(45);
        assertEquals(45, r.getAngle());
    }

    @Test
    void normalizationTest() {
        Rotation r = new Rotation(-1);
        assertEquals(359, r.getAngle());
        r.rotate(1);
        assertEquals(0, r.getAngle());
        r.rotate(3 * 360 + 180);
        assertEquals(180, r.getAngle());
        r.rotate(-3 * 360 - 180);
        assertEquals(0, r.getAngle());
    }

    @Test
    void updateTest() {
        Rotation r = new Rotation(30f);
        r.setRotationSpeed(10f);
        r.update(2f);
        assertEquals(50f, r.getAngle());

        r.update(40f);
        assertEquals(90f, r.getAngle());
    }

    @Test
    void toStringTest() {
        Rotation r = new Rotation(180);
        assertEquals("Rotation{angle=180.0}", r.toString());
    }
}
