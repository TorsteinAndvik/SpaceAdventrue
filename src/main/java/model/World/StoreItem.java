package model.World;

import model.ShipComponents.UpgradeType;

public record StoreItem(UpgradeType item, int price, String description) { }