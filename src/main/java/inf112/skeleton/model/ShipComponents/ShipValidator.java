package inf112.skeleton.model.ShipComponents;

import static inf112.skeleton.model.utils.SpaceCalculator.orthogonallyAdjacent;

import com.badlogic.gdx.utils.JsonValue;
import inf112.skeleton.grid.CellPosition;

import inf112.skeleton.model.ShipComponents.ShipConfig.ShipComponent;
import inf112.skeleton.model.utils.ArgumentChecker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ShipValidator {

    public static boolean isValid(JsonValue shipJson) {

        List<CellPosition> cellPositions = extractCellPositions(shipJson);
        if (cellPositions == null) {
            return false;
        }
        return isShipConnected(cellPositions);
    }

    public static boolean isValid(ShipConfig shipConfig) {

        List<CellPosition> cellPositions = extractCellPositions(shipConfig);
        if (cellPositions == null) {
            return false;
        }

        return isShipConnected(cellPositions);

    }

    private static boolean isShipConnected(List<CellPosition> componentPositions) {
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
        System.out.println(componentPositions.size());
        System.out.println(+visited.size());
        return componentPositions.size() == visited.size();
    }


    private static List<CellPosition> getAdjacent(List<CellPosition> components, CellPosition cp) {
        List<CellPosition> adjacent = new ArrayList<>();
        for (CellPosition adjPos : components) {
            if (!adjPos.equals(cp) && orthogonallyAdjacent(adjPos, cp)) {
                adjacent.add(adjPos);
            }
        }
        return adjacent;
    }

    public static List<CellPosition> extractCellPositions(ShipConfig shipConfig) {
        ArgumentChecker.requireNonNull(shipConfig, "ShipConfig can't be null");
        if (shipConfig.components == null) {
            return null;
        }
        List<CellPosition> componentPositions = new ArrayList<>();
        for (ShipComponent cp : shipConfig.components) {
            componentPositions.add(cp.getPosition());
        }
        return componentPositions;
    }

    public static List<CellPosition> extractCellPositions(JsonValue shipData) {
        ArgumentChecker.requireNonNull(shipData, "JsonValue can't be null");
        if (!shipData.has("components")) {
            return null;
        }

        List<CellPosition> componentPositions = new ArrayList<>();
        for (JsonValue component : shipData.get("components")) {

            componentPositions.add(
                    new CellPosition(component.get("y").asInt(), component.get("x").asInt()));
        }
        System.out.println(componentPositions);
        return componentPositions;
    }
}

