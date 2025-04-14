package model.ShipComponents;

import static model.utils.SpaceCalculator.orthogonallyAdjacent;

import com.badlogic.gdx.utils.JsonValue;
import grid.CellPosition;

import grid.GridCell;
import grid.IGrid;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.ShipConfig.ShipComponent;
import model.utils.ArgumentChecker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class ShipValidator {

    private ShipValidator() {
        // Utility class
    }

    public static boolean isValid(JsonValue shipJson) {

        List<CellPosition> cellPositions = extractCellPositions(shipJson);
        if (cellPositions == null) {
            return false;
        }
        return isShipConnected(cellPositions);
    }

    public static boolean isValid(ShipConfig shipConfig) {

        List<CellPosition> cellPositions = extractCellPositions(shipConfig);

        return isShipConnected(cellPositions);

    }

    public static boolean isValid(IGrid<Fuselage> shipGrid) {
        List<CellPosition> cellPositions = extractCellPositions(shipGrid);
        if (cellPositions.isEmpty()) {
            return true;
        }
        return isShipConnected(cellPositions);
    }


    public static boolean isShipConnected(List<CellPosition> componentPositions) {
        if (componentPositions.isEmpty()) {
            return false;
        }

        Set<CellPosition> visited = new HashSet<>();
        Queue<CellPosition> toSearch = new LinkedList<>();

        CellPosition startPos = componentPositions.get(0);
        toSearch.add(startPos);
        toSearch.addAll(getAdjacent(componentPositions, startPos));
        visited.add(startPos);

        while (!toSearch.isEmpty()) {
            CellPosition current = toSearch.poll();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (CellPosition adjPos : getAdjacent(componentPositions, current)) {
                if (!visited.contains(adjPos)) {
                    toSearch.add(adjPos);
                }
            }
        }
        return componentPositions.size() == visited.size();
    }

    private static List<CellPosition> getAdjacent(List<CellPosition> components, CellPosition cp) {
        List<CellPosition> adjacent = new ArrayList<>();
        for (CellPosition adjPos : components) {
            if (orthogonallyAdjacent(adjPos, cp)) {
                adjacent.add(adjPos);
            }
        }
        return adjacent;
    }

    private static List<CellPosition> extractCellPositions(ShipConfig shipConfig) {
        ArgumentChecker.requireNonNull(shipConfig, "ShipConfig can't be null");
        List<CellPosition> componentPositions = new ArrayList<>();
        for (ShipComponent cp : shipConfig.components) {
            componentPositions.add(cp.getPosition());
        }
        return componentPositions;
    }

    private static List<CellPosition> extractCellPositions(JsonValue shipData) {
        ArgumentChecker.requireNonNull(shipData, "JsonValue can't be null");
        if (!shipData.has("components")) {
            return null;
        }

        List<CellPosition> componentPositions = new ArrayList<>();
        for (JsonValue component : shipData.get("components")) {

            componentPositions.add(
                    new CellPosition(component.get("y").asInt(), component.get("x").asInt()));
        }

        return componentPositions;
    }

    private static List<CellPosition> extractCellPositions(IGrid<Fuselage> shipGrid) {
        ArgumentChecker.requireNonNull(shipGrid, "ShipGrid can't be null");
        List<CellPosition> componentPositions = new ArrayList<>();
        for (GridCell<Fuselage> cp : shipGrid) {
            if (cp.value() != null) {
                componentPositions.add(cp.pos());
            }
        }
        return componentPositions;
    }

}
