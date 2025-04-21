package model.World;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.ShipComponents.UpgradeType;

public class UpgradeStore extends Store<StoreItem<UpgradeType>> {

    private final Comparator<StoreItem<UpgradeType>> comparator = Comparator.comparing(StoreItem::price);

    private Map<UpgradeType, Integer> upgradeStageShelf;

    public UpgradeStore() {
        addStock();
        upgradeStageShelf = addUpgradeStageShelf();
    }

    public UpgradeStore(Set<StoreItem<UpgradeType>> shelf) {
        for (StoreItem<UpgradeType> item : shelf) {
            addToStock(item);
        }
    }

    private void addStock() {
        addToStock(new StoreItem<>(UpgradeType.FUSELAGE, 50,
                "Fuselage:\nUsed to expand the ship. New upgrades are attached to Fuselage."));
        addToStock(new StoreItem<>(UpgradeType.TURRET, 150,
                "Turret:\nFires lasers at enemies and asteroids."));
        addToStock(new StoreItem<>(UpgradeType.THRUSTER, 130,
                "Rocket:\nImproves acceleration and top speed of the ship."));
        addToStock(new StoreItem<>(UpgradeType.SHIELD, 100,
                "Shield:\nIncrease the ship's health."));
    }

    private Map<UpgradeType, Integer> addUpgradeStageShelf() {
        return Map.ofEntries(
                Map.entry(UpgradeType.FUSELAGE, getStoreItem(UpgradeType.FUSELAGE).price() / 2),
                Map.entry(UpgradeType.TURRET, getStoreItem(UpgradeType.TURRET).price() / 2),
                Map.entry(UpgradeType.THRUSTER, getStoreItem(UpgradeType.THRUSTER).price() / 2),
                Map.entry(UpgradeType.SHIELD, getStoreItem(UpgradeType.SHIELD).price() / 2));
    }

    @Override
    public List<StoreItem<UpgradeType>> getStock() {
        List<StoreItem<UpgradeType>> stock = super.getStock();
        stock.sort(comparator);
        return stock;
    }

    public Map<UpgradeType, Integer> getUpgradeStageShelf() {
        return upgradeStageShelf;
    }

    /**
     * Retrieves a <code>StoreItem</code> from the sorted stock at the given index.
     *
     * @param index the index of the StoreItem in the sorted stock list
     * @return the StoreItem at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public StoreItem<UpgradeType> getStoreItem(int index) throws IndexOutOfBoundsException {
        List<StoreItem<UpgradeType>> stock = getStock();
        return stock.get(index);
    }

    /**
     * Retrieves the <code>StoreItem</code> for the given <code>UpgradeType</code>.
     * 
     * @param upgradeType the <code>UpgradeType</code> of the <code>StoreItem</code>
     *                    to retrieve
     * @return the <code>StoreItem</code> for the given <code>UpgradeType</code>
     * @throws IllegalArgumentException if no <code>StoreItem</code> is found for
     *                                  the given <code>UpgradeType</code>
     */
    private StoreItem<UpgradeType> getStoreItem(UpgradeType upgradeType) throws IllegalArgumentException {
        for (StoreItem<UpgradeType> item : getStock()) {
            if (item.item() == upgradeType) {
                return item;
            }
        }

        throw new IllegalArgumentException(
                "Attempting to get a non-existent StoreItem for upgrade type: " + upgradeType);
    }
}
