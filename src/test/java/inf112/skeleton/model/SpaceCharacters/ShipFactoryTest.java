package inf112.skeleton.model.SpaceCharacters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import inf112.skeleton.model.ShipComponents.ShipFactory;
import org.junit.jupiter.api.Test;

public class ShipFactoryTest {
  String shipJson =
      """
      {
        "name": "ship1",
        "width": 1,
        "height": 2,
        "components": [
          {"x": 0,"y": 0,"upgrade": {"type": "Thruster", "level": 1}},
          {"x": 1,"y": 0,"upgrade": {"type": "Turret", "level": 1}}
        ]
      }
      """;

  @Test
  void jsonStructureTest() {
    JsonReader json = new JsonReader();

    JsonValue enemy1 = json.parse(shipJson);
    assertTrue(enemy1.has("name"));
    assertTrue(enemy1.has("width"));
    assertTrue(enemy1.has("height"));
    assertTrue(enemy1.has("components"));
    JsonValue components = enemy1.get("components");

    for (JsonValue component : components.iterator()) {
      assertTrue(component.has("x"));
      assertTrue(component.has("y"));
      assertTrue(component.has("upgrade"));
      JsonValue upgrade = component.get("upgrade");
      assertTrue(upgrade.has("type"));
      assertTrue(upgrade.has("level"));
    }
  }

  @Test
  void shipFactoryTest() {

    JsonValue shipData = new JsonReader().parse(shipJson);
    ShipFactory sf = new ShipFactory();
    String shipConfig =
        "ShipConfig{width=1, height=2, components=[(0,0) = THRUSTER, lvl= 1, (1,0) = TURRET, lvl= 1]}";
    assertEquals(shipConfig, sf.createShipConfigFromJson(shipData).toString());
  }
}
