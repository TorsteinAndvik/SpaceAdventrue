package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;

public abstract class UpdateableShipUpgrade extends ShipUpgrade {

    public UpdateableShipUpgrade(String name, String description, UpgradeType type, UpgradeStage stage) {
        super(name, description, type, stage);
    }

    public abstract void update(float deltaTime);
}
