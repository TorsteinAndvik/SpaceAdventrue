package model.World;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import model.ShipComponents.UpgradeType;

public class UpgradeStore extends Store<StoreItem<UpgradeType>> {

    private final Comparator<StoreItem<UpgradeType>> comparator = Comparator.comparing(StoreItem::price);


    public UpgradeStore() {
        addStock();
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

    @Override
    public List<StoreItem<UpgradeType>> getStock() {
        List<StoreItem<UpgradeType>> stock = super.getStock();
        stock.sort(comparator);
        return stock;
    }

    /**
     * Retrieves a StoreItem from the sorted stock at the given index.
     *
     * @param index the index of the StoreItem in the sorted stock list
     * @return the StoreItem at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public StoreItem<UpgradeType> getStoreItem(int index) {
        List<StoreItem<UpgradeType>> stock = getStock();
        return stock.get(index);
    }
}
