package inf112.skeleton.model.ShipComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import inf112.skeleton.grid.CellPosition;
import inf112.skeleton.model.ShipComponents.Components.Fuselage;
import inf112.skeleton.model.ShipComponents.Components.ShipStructure;
import inf112.skeleton.model.ShipComponents.Components.Thruster;
import inf112.skeleton.model.ShipComponents.Components.Turret;
import inf112.skeleton.model.ShipComponents.ShipConfig.ShipComponent;
import inf112.skeleton.model.ShipComponents.ShipConfig.Upgrade;
import java.util.ArrayList;

public class ShipFactory {

    public ShipFactory() {
    }

    public ShipConfig createShipConfigFromJson(String filename) {
        JsonReader json = new JsonReader();
        JsonValue shipData = json.parse(Gdx.files.internal("ships/" + filename));

        return createShipConfigFromJson(shipData);
    }

    public ShipConfig createShipConfigFromJson(JsonValue shipData) {

        if (!ShipValidator.isValid(shipData)) {
            throw new IllegalArgumentException("Invalid ship JSON: does not meet requirements.");
        }

        ShipConfig shipConfig = new ShipConfig();

        shipConfig.width = shipData.get("width").asInt();
        shipConfig.height = shipData.get("height").asInt();
        shipConfig.components = new ArrayList<>();
        for (JsonValue component : shipData.get("components")) {
            ShipComponent shipComponent = new ShipComponent();
            shipComponent.x = component.get("x").asInt();
            shipComponent.y = component.get("y").asInt();

            shipConfig.components.add(shipComponent);

            if (!component.has("upgrade")) {
                continue;
            }

            Upgrade upgrade = new Upgrade();

            upgrade.type = UpgradeType.getUpgradeType(
                    component.get("upgrade").get("type").asString());
            upgrade.level = component.get("upgrade").get("level").asInt();

            shipComponent.upgrade = upgrade;
        }

        if (!ShipValidator.isValid(shipConfig)) {
            throw new IllegalArgumentException(
                    "Invalid ship configuration for " + shipData.get("name"));
        }

        return shipConfig;
    }

    public ShipStructure createShipFromJson(String filename) {
        return CreateShipFromShipConfig(createShipConfigFromJson(filename));
    }

    public ShipStructure CreateShipFromShipConfig(ShipConfig shipConfig) {
        return new ShipStructure(shipConfig);
    }

    /**
     * @return A 1x2 <code>ShipStructure</code> with a turret at the front and a thruster at the
     * back.
     */
    public ShipStructure simpleShip() {
        ShipStructure ship = new ShipStructure(1, 2);
        ship.set(new CellPosition(1, 0), new Fuselage(new Turret()));
        ship.set(new CellPosition(0, 0), new Fuselage(new Thruster()));
        return ship;
    }
}
