package model.ShipComponents.Components.stats;

import java.util.HashMap;

public class StatModifier {

    protected final HashMap<Stat, Number> modifiers;

    public StatModifier() {
        modifiers = new HashMap<>();
        for (Stat stat : Stat.values()) {
            if (stat.intBased) {
                modifiers.put(stat, 0);
            } else {
                modifiers.put(stat, 0f);
            }
        }
    }

    /**
     * Sets the value of a stat.
     * 
     * @param stat  the <code>Stat</code> to set
     * @param value the new value
     */
    public void setModifier(Stat stat, Number value) {
        if (stat.intBased) {
            modifiers.put(stat, value.intValue());
        } else {
            modifiers.put(stat, value.floatValue());
        }
    }

    /**
     * @return the HashMap of modifiers.
     */
    public HashMap<Stat, Number> getModifiers() {
        return modifiers;
    }

    /**
     * Adds the modifiers of another <code>StatModifier</code> to <code>this</code>,
     * and returns <code>this</code>.
     * 
     * @param addedModifiers the <code>StatModifier</code> to add
     * @return <code>this</code>
     */
    public StatModifier addModifier(StatModifier addedModifiers) {
        for (Stat stat : Stat.values()) {
            if (stat.intBased) {
                modifiers.put(stat,
                        modifiers.get(stat).intValue() + addedModifiers.getModifiers().get(stat).intValue());
            } else {
                modifiers.put(stat,
                        modifiers.get(stat).floatValue() + addedModifiers.getModifiers().get(stat).floatValue());
            }
        }
        return this;
    }

    /**
     * @return a safe-to-modify copy of <code>this</code>.
     */
    public StatModifier copy() {
        StatModifier copy = new StatModifier();
        for (Stat stat : Stat.values()) {
            if (stat.intBased) {
                copy.setModifier(stat, modifiers.get(stat).intValue());
            } else {
                copy.setModifier(stat, modifiers.get(stat).floatValue());
            }
        }
        return copy;
    }

    @Override
    public String toString() {
        return modifiers.toString();
    }
}
