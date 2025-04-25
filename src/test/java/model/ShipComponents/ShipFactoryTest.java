package model.ShipComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import grid.CellPosition;
import grid.GridCell;
import model.ShipComponents.Components.Fuselage;

import org.junit.jupiter.api.Test;

public class ShipFactoryTest {

    String shipJson = """
            {
              "name": "ship1",
              "width": 1,
              "height": 3,
              "components": [
                {"x": 0,"y": 0,"upgrade": {"type": "Thruster", "level": 1}},
                {"x": 0,"y": 1,"upgrade": {"type": "Turret", "level": 1}},
                {"x": 0,"y": 2}
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

        int i = 0;
        for (JsonValue component : components.iterator()) {
            assertTrue(component.has("x"));
            assertTrue(component.has("y"));
            if (i < 2) {
                assertTrue(component.has("upgrade"));
                JsonValue upgrade = component.get("upgrade");
                assertTrue(upgrade.has("type"));
                assertTrue(upgrade.has("level"));
            } else {
                assertFalse(component.has("upgrade"));
            }
            i++;
        }
    }

    @Test
    void createShipConfigFromJsonTest() {
        JsonReader json = new JsonReader();
        JsonValue shipData = json.parse(shipJson);

        String shipConfig = "ShipConfig{width=1, height=3, components=[(0,0) = THRUSTER, lvl= 1, (0,1) = TURRET, lvl= 1, (0,2) = null]}";
        assertEquals(shipConfig, ShipFactory.createShipConfigFromJson(shipData).toString());

        String invalidJsonString = "this is not a valid ship description";
        String noComponentsString = """
                {
                  "name": "ship1",
                  "width": 1,
                  "height": 3,
                  "components": [
                    {"x": 0,"y": 0,"upgrade": {"type": "Thruster", "level": 1}},
                    {"x": 0,"y": 2,"upgrade": {"type": "Turret", "level": 1}}
                  ]
                }
                """;

        JsonValue invalidJson = json.parse(invalidJsonString);
        JsonValue noComponentsJson = json.parse(noComponentsString);
        assertThrows(IllegalArgumentException.class, () -> ShipFactory.createShipConfigFromJson(invalidJson));
        assertThrows(IllegalArgumentException.class, () -> ShipFactory.createShipConfigFromJson(noComponentsJson));
    }

    @Test
    void headlessApplicationTest() {

        Files filesMock = mock(Files.class);

        when(filesMock.internal("ships/player.json")).thenReturn(new FileHandle("ships/player.json"));

        @SuppressWarnings("unused")
        HeadlessApplication headlessApplication = new HeadlessApplication(new ApplicationListener() {
            @Override
            public void create() {
                Gdx.files = filesMock;
            }

            @Override
            public void dispose() {
            }

            @Override
            public void pause() {
            }

            @Override
            public void render() {
            }

            @Override
            public void resize(int arg0, int arg1) {
            }

            @Override
            public void resume() {
            }
        });

        String shipConfig = "ShipConfig{width=1, height=2, components=[(0,0) = THRUSTER, lvl= 1, (0,1) = TURRET, lvl= 1]}";
        assertEquals(shipConfig, ShipFactory.createShipConfigFromJson("player.json").toString());

        // a ship structure is created, no exceptions are thrown
        assertNotNull(ShipFactory.createShipFromJson("player.json"));
    }

    @Test
    void playerShipTest() {
        ShipStructure player = ShipFactory.playerShip();
        assertNotNull(player);
        assertEquals(2, player.getHeight());
        assertEquals(1, player.getWidth());
        assertTrue(player.hasFuselage(new CellPosition(0, 0)));
        assertTrue(player.hasFuselage(new CellPosition(1, 0)));
        assertSame(player.getGridCopy().get(new CellPosition(0, 0)).getUpgrade().getType(), UpgradeType.THRUSTER);
        assertSame(player.getGridCopy().get(new CellPosition(1, 0)).getUpgrade().getType(), UpgradeType.TURRET);
    }

    @Test
    void generateShipStructureTest() {
        assertThrows(IllegalArgumentException.class, () -> ShipFactory.generateShipStructure(0, 0));
        assertThrows(IllegalArgumentException.class, () -> ShipFactory.generateShipStructure(0, 3));
        assertThrows(IllegalArgumentException.class, () -> ShipFactory.generateShipStructure(5, 0));
        assertThrows(IllegalArgumentException.class, () -> ShipFactory.generateShipStructure(4, 8));

        for (int i = 0; i < 500; i++) {
            ShipStructure basicShip = ShipFactory.generateShipStructure(2, 2);
            assertNotNull(basicShip);
            assertEquals(2, basicShip.getHeight() * basicShip.getWidth());

            List<UpgradeType> upgrades = new ArrayList<>();
            for (GridCell<Fuselage> gridCell : basicShip) {
                upgrades.add(gridCell.value().getUpgrade().getType());
            }

            assertTrue(upgrades.contains(UpgradeType.THRUSTER));
            assertTrue(upgrades.contains(UpgradeType.TURRET));

            ShipStructure bigShip = ShipFactory.generateShipStructure(50, 2);
            List<CellPosition> positions = new ArrayList<>();
            for (GridCell<Fuselage> gridCell : bigShip) {
                positions.add(gridCell.pos());
            }
            assertTrue(ShipValidator.isShipConnected(positions));
        }
    }
}
