package model.ShipComponents;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import grid.CellPosition;

import org.junit.jupiter.api.Test;

public class ShipValidatorTest {

    String validShip = """
            {"name":"light fighter","width":2,"height":2,"components":
            [{"x":0,"y":0,"upgrade":{"type":"Thruster","level":1}},
            {"x":1,"y":0},
            {"x":0,"y":1,"upgrade":{"type":"Thruster","level":1}},
            {"x":1,"y":1,"upgrade":{"type":"Turret","level":1}}]}
            """;
    String invalidShip = """
            {"name":"light fighter","width":2,"height":2,"components":
            [{"x":0,"y":0,"upgrade":{"type":"Thruster","level":1}},
            {"x":1,"y":0},
            {"x":0,"y":1,"upgrade":{"type":"Thruster","level":1}},
            {"x":2,"y":2,"upgrade":{"type":"Turret","level":1}}]}
            """;

    String invalidShip2 = """
            {"name":"light fighter","width":2,"height":2}
            """;

    String smallInvalidShip = """
            {"name":"light fighter","width":2,"height":2,"components":
            [
                {"x":0,"y":0},
                {"x":2,"y":2}]
            }
            """;

    public JsonReader json = new JsonReader();

    @Test
    void validShipTest() {
        JsonValue enemy = json.parse(validShip);
        assertTrue(ShipValidator.isValid(enemy));
        assertTrue(ShipValidator.isValid(ShipFactory.createShipConfigFromJson(enemy)));
    }

    @Test
    void invalidFuselagePositionTest() {
        JsonValue enemy = json.parse(invalidShip);
        assertFalse(ShipValidator.isValid(enemy));
    }

    @Test
    void missingComponentsTest() {
        JsonValue enemy = json.parse(invalidShip2);
        assertFalse(ShipValidator.isValid(enemy));
    }

    @Test
    void smallInvalidShipTest() {
        JsonValue enemy = json.parse(smallInvalidShip);
        assertFalse(ShipValidator.isValid(enemy));
    }

    @Test
    void jsonValueNullTest() {
        JsonValue jsonValue = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ShipValidator.isValid(jsonValue);
        });

        String expectedMessage = "JsonValue can't be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shipConfigNullTest() {
        ShipConfig shipConfig = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ShipValidator.isValid(shipConfig);
        });

        String expectedMessage = "ShipConfig can't be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void isShipConnectedTest() {
        List<CellPosition> componentPositions = new ArrayList<>();
        assertFalse(ShipValidator.isShipConnected(componentPositions));

        componentPositions.add(new CellPosition(0, 0));
        assertTrue(ShipValidator.isShipConnected(componentPositions));

        componentPositions.add(new CellPosition(1, 1));
        assertFalse(ShipValidator.isShipConnected(componentPositions));

        componentPositions.add(new CellPosition(1, 0));
        assertTrue(ShipValidator.isShipConnected(componentPositions));
    }

}
