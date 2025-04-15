package model.ShipComponents.Components.stats;

import java.util.HashMap;

public class StatModifier {
    private final HashMap<Stat, Number> modifiers;

    public StatModifier() {
        modifiers = new HashMap<>();
        for (Stat stat : Stat.values()) {
            modifiers.put(stat, 0f);
        }
    }

    public void setModifier(Stat stat, Number value) {
        modifiers.put(stat, value);
    }

    public HashMap<Stat, Number> getModifiers() {
        return modifiers;
    }
}
