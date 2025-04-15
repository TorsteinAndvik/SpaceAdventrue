package model.World;

import model.ShipComponents.UpgradeType;

public record StoreItem<E>(E item, int price, String description) { }