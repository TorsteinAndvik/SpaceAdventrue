package view.bars;

import java.util.HashMap;

import model.ShipComponents.Components.stats.Stat;
import model.ShipComponents.Components.stats.StatModifier;

public class UpgradeStageDisplay {
    public UpgradeStageDisplay(StatModifier max) {
        HashMap<Stat, DiffBar> bars = new HashMap<>();
        for (Stat stat : Stat.values()) {
            DiffBar statBar = new DiffBar();
            statBar.setMaxValue(max.getModifiers().get(stat).floatValue());
            bars.put(stat, statBar);
        }
    }

}
