package model.ShipComponents.Components.stats;

import java.util.Arrays;
import java.util.List;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.Shield;
import model.ShipComponents.Components.Thruster;
import model.ShipComponents.Components.Turret;

public class MaxStatCalculator {

    private final StatModifier fuselageMax;

    public MaxStatCalculator() {
        Fuselage fuselageTurret = new Fuselage(new Turret(UpgradeStage.MAX), UpgradeStage.MAX);
        Fuselage fuselageShield = new Fuselage(new Shield(UpgradeStage.MAX), UpgradeStage.MAX);
        Fuselage fuselageThruster = new Fuselage(new Thruster(UpgradeStage.MAX), UpgradeStage.MAX);

        fuselageMax = new StatModifier();

        List<Fuselage> upgrades = Arrays.asList(fuselageTurret, fuselageShield, fuselageThruster);
        for (Fuselage fuselage : upgrades) {
            for (Stat stat : Stat.values()) {
                Number statValue = fuselage.getModifiers().get(stat);
                if (statValue.floatValue() > (fuselageMax.getModifiers().get(stat)).floatValue()) {
                    fuselageMax.setModifier(stat, statValue);
                }
            }
        }
    }

    /**
     * Calculates the maximum value of a stat for a ship with the specified number
     * of fuselages.
     * <p>
     * Negative values return zeroed stats.
     * 
     * @param numFuselage number of fuselages
     * @return a <code>StatModifier</code> containing the maximum value of each stat
     */
    public StatModifier getFuselageMax(int numFuselage) {
        if (numFuselage <= 0) {
            return new StatModifier();
        } else {
            StatModifier shipMax = new StatModifier();
            for (int i = 0; i < numFuselage; i++) {
                shipMax.addModifier(fuselageMax);
            }
            return shipMax;
        }
    }
}
