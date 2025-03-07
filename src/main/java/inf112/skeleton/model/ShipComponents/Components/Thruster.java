package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.model.ShipComponents.UpgradeStage;
import inf112.skeleton.model.ShipComponents.UpgradeType;

public class Thruster extends ShipUpgrade {

    public Thruster() {this(UpgradeStage.ZERO);}
    
    public Thruster(UpgradeStage stage) {
        super("Thruster", "Increases the speed of the ship", UpgradeType.THRUSTER, stage);
    }
}
