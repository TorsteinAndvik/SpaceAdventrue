package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.model.ShipComponents.UpgradeStage;
import inf112.skeleton.model.ShipComponents.UpgradeType;

public abstract class ShipUpgrade {

    private String name;
    private String description;
    
    private UpgradeType type;
    private UpgradeStage stage;

    public ShipUpgrade(String name, String description, UpgradeType type, UpgradeStage stage) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.stage = stage;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public UpgradeType getType() {
        return type;
    }
    
    public UpgradeStage getStage() {
        return stage;
    }

    /**
     * Upgrade <code>this</code> to the next stage if possible
     * 
     * @return true if <code>this</code> was upgraded, false otherwise
     */
    public boolean upgrade() {
        if(stage.isUpgradeable()) {
            stage = stage.nextStage(stage);
            return true;
        } else {return false;}
    }
}
