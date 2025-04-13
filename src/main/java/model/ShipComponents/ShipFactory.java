package model.ShipComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import grid.CellPosition;
import grid.Grid;
import grid.GridCell;
import grid.IGrid;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.Shield;
import model.ShipComponents.Components.ShipStructure;
import model.ShipComponents.Components.Thruster;
import model.ShipComponents.Components.Turret;
import model.ShipComponents.ShipConfig.ShipComponent;
import model.ShipComponents.ShipConfig.Upgrade;
import model.utils.SpaceCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public final class ShipFactory {

    private ShipFactory() {
        // Utility class
    }

    public static ShipConfig createShipConfigFromJson(String filename) {
        JsonReader json = new JsonReader();
        JsonValue shipData = json.parse(Gdx.files.internal("ships/" + filename));

        return createShipConfigFromJson(shipData);
    }

    public static ShipConfig createShipConfigFromJson(JsonValue shipData) {

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

        return shipConfig;
    }

    public static ShipStructure createShipFromJson(String filename) {
        return CreateShipFromShipConfig(createShipConfigFromJson(filename));
    }

    public static ShipStructure CreateShipFromShipConfig(ShipConfig shipConfig) {
        return new ShipStructure(shipConfig);

    }

    /**
     * @return A 1x2 <code>ShipStructure</code> with a turret at the front and a
     *         thruster at the
     *         back.
     */
    public static ShipStructure simpleShip() {
        ShipStructure ship = new ShipStructure(1, 2);
        ship.set(new CellPosition(1, 0), new Fuselage(new Turret()));
        ship.set(new CellPosition(0, 0), new Fuselage(new Thruster()));
        return ship;
    }

    /**
     * @return a <code>this.simpleShip()</code> centered on a grid
     */
    public static ShipStructure playerShip() {
        return simpleShip();
    }

    /**
     * Generates an enemy ship with the specified number of fuselages and upgrades.
     * 
     * @param numFuselage number of <code>Fuselages</code> in
     *                    <code>ShipStructure</code>.
     * @param numUpgrades number of <code>ShipUpgrade</code> held by in
     *                    <code>ShipStructure</code>.
     * 
     * @return a randomly generated and valid <code>ShipStructure</code> with the
     *         specified number of fuselages and upgrades.
     * 
     * @throws IllegalArgumentException if either <code>numFuselage</code> or
     *                                  <code>numUpgrades</code> is less
     *                                  than 2, or <code>numUpgrades</code> is
     *                                  greater than <code>numFuselage</code>.
     */
    public static ShipStructure generateShipStructure(int numFuselage, int numUpgrades)
            throws IllegalArgumentException {
        if (numFuselage < 2 || numUpgrades < 2 || numFuselage < numUpgrades) {
            throw new IllegalArgumentException(
                    "numFuselage and numUpgrades must be 2 or greater, and numFuselage must be greater than numUpgrades");
        }

        Random rng = new Random();

        LinkedList<Fuselage> components = new LinkedList<>();
        components.add(new Fuselage(new Thruster()));
        components.add(new Fuselage(new Turret()));

        int componentsToAdd = numFuselage - components.size();
        for (int i = 0; i < componentsToAdd; i++) {
            if (components.size() > numUpgrades) {
                components.add(new Fuselage());
                continue;
            }

            int upgrade = rng.nextInt(3);
            if (upgrade == 0) {
                components.add(new Fuselage(new Thruster()));
            } else if (upgrade == 1) {
                components.add(new Fuselage(new Turret()));
            } else {
                components.add(new Fuselage(new Shield()));
            }
        }

        // shuffle around the upgrade order for the end ShipStructure
        Collections.shuffle(components);

        // startpos is in the middle, with enough padding to allow
        // randomly adding all components on a line
        IGrid<Fuselage> grid = new Grid<>(2 * numFuselage - 1, 2 * numFuselage - 1);
        CellPosition startPosition = new CellPosition(numFuselage - 1, numFuselage - 1);

        grid.set(startPosition, components.poll());

        while (!components.isEmpty()) {
            List<CellPosition> validPositions = getEmptyAdjacentPositions(grid);
            int index = rng.nextInt(validPositions.size());
            grid.set(validPositions.get(index), components.poll());
        }

        return new ShipStructure(Grid.shrinkGridToFit(grid));
    }

    private static List<CellPosition> getEmptyAdjacentPositions(IGrid<Fuselage> grid) {
        ArrayList<CellPosition> positions = new ArrayList<>();
        for (GridCell<Fuselage> gridCell : grid) {
            if (gridCell.value() == null) {
                continue;
            }

            List<CellPosition> adjacent = SpaceCalculator.getOrthogonalNeighbours(gridCell.pos());
            for (CellPosition cell : adjacent) {
                if (grid.get(cell) == null) {
                    if (!positions.contains(cell)) {
                        positions.add(cell);
                    }
                }
            }
        }
        return positions;
    }
}
