package view.lighting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.Pool;

import grid.CellPosition;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.Ships.SpaceShip;


public class ShipThrusterLightMap {

    Pool<ThrusterLight> thrusterLightPool = new Pool<ThrusterLight>() {
        @Override
        protected ThrusterLight newObject() {
            return new ThrusterLight();
        }
    };

    public final HashMap<SpaceShip, List<ThrusterLight>> thrusterLightMap = new HashMap<>();

    public ShipThrusterLightMap(int lightPreFill) {
        thrusterLightPool.fill(lightPreFill);
    }

    public void update(List<SpaceShip> spaceShips) {
        // Remove destroyed ships from map
        Iterator<SpaceShip> shipIterator = thrusterLightMap.keySet().iterator();
        while (shipIterator.hasNext()) {
            SpaceShip ship = shipIterator.next();
            if (!spaceShips.contains(ship)) {
                for (ThrusterLight thrusterLight : thrusterLightMap.get(ship)) {
                    thrusterLightPool.free(thrusterLight);
                }
                shipIterator.remove();
            }
        }

        // Add new ships to map
        for (SpaceShip ship : spaceShips) {
            if (!this.thrusterLightMap.containsKey(ship)) {
                List<CellPosition> thrusters = ship.getUpgradeTypePositions(UpgradeType.THRUSTER);
                List<ThrusterLight> thrusterLights = new LinkedList<ThrusterLight>();
                for (int i = 0; i < thrusters.size(); i++) {
                    ThrusterLight thrusterLight = thrusterLightPool.obtain();
                    thrusterLight.init();
                    thrusterLights.add(thrusterLight);
                }
                this.thrusterLightMap.put(ship, thrusterLights);
            }
        }
    }

    public List<ThrusterLight> get(SpaceShip ship) {
        return thrusterLightMap.get(ship);
    }
}
