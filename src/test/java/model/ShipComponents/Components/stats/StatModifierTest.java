package model.ShipComponents.Components.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StatModifierTest {

    private StatModifier modifier;

    @BeforeEach
    void setup() {
        modifier = new StatModifier();
    }

    @Test
    void constructorTest() {
        for (Stat stat : Stat.values()) {
            if (stat.intBased) {
                assertEquals(0, modifier.getModifiers().get(stat));
            } else {
                assertEquals(0f, modifier.getModifiers().get(stat));
            }
        }
    }

    @Test
    void setModifierTest() {
        for (Stat stat : Stat.values()) {
            if (stat.intBased) {
                assertEquals(0, modifier.getModifiers().get(stat));
                modifier.setModifier(stat, 1);
                assertEquals(1, modifier.getModifiers().get(stat));
                modifier.setModifier(stat, 3);
                assertEquals(3, modifier.getModifiers().get(stat));
            } else {
                assertEquals(0f, modifier.getModifiers().get(stat));
                modifier.setModifier(stat, 1f);
                assertEquals(1f, modifier.getModifiers().get(stat));
                modifier.setModifier(stat, 3f);
                assertEquals(3f, modifier.getModifiers().get(stat));
            }
        }
    }

    @Test
    void getModifiersTest() {
        assertEquals(modifier.modifiers, modifier.getModifiers());
    }

    @Test
    void addModifierTest() {

        modifier.setModifier(Stat.MASS, 1f);
        modifier.setModifier(Stat.HEALTH_REGENERATION_RATE, 0.5f);
        modifier.setModifier(Stat.ACCELERATION_FORCE, 3f);

        StatModifier addedModifiers = new StatModifier();
        addedModifiers.setModifier(Stat.MASS, 2f);
        addedModifiers.setModifier(Stat.RESOURCE_VALUE, 15);
        addedModifiers.setModifier(Stat.ACCELERATION_FORCE, 3f);

        modifier.addModifier(addedModifiers);

        assertEquals(3f, modifier.getModifiers().get(Stat.MASS));
        assertEquals(0.5f, modifier.getModifiers().get(Stat.HEALTH_REGENERATION_RATE));
        assertEquals(6f, modifier.getModifiers().get(Stat.ACCELERATION_FORCE));
        assertEquals(15, modifier.getModifiers().get(Stat.RESOURCE_VALUE));
    }

    @Test
    void toStringTest() {
        for (Stat stat : Stat.values()) {
            String expected;
            if (stat.intBased) {
                expected = stat + "=0";
            } else {
                expected = stat + "=0.0";
            }
            assertTrue(modifier.toString().contains(expected));
        }
    }
}
