package inf112.skeleton.model.ShipComponents;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import inf112.skeleton.grid.CellPosition;
import java.util.List;
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
    void SmallInvalidShipTest() {
        JsonValue enemy = json.parse(smallInvalidShip);
        assertFalse(ShipValidator.isValid(enemy));
    }
    
}
