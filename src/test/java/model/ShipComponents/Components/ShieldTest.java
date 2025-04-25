package model.ShipComponents.Components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.Components.stats.Stat;
import model.constants.PhysicsParameters;

public class ShieldTest {

    @Test
    void constructorTest() {
        Shield shield = new Shield();
        assertEquals(UpgradeStage.ZERO, shield.getStage());

        for (Stat stat : Stat.values()) {
            if (stat == Stat.MASS) {
                assertEquals(PhysicsParameters.shipUpgradeMass, shield.getModifiers().get(stat).floatValue());
            } else if (stat == Stat.HEALTH_VALUE) {
                assertTrue(shield.getModifiers().get(stat).intValue() > 0);
            } else if (stat == Stat.HEALTH_REGENERATION_RATE) {
                assertTrue(shield.getModifiers().get(stat).floatValue() > 0f);
            } else if (stat == Stat.RESOURCE_VALUE) {
                assertTrue(shield.getModifiers().get(stat).intValue() > 0);
            } else {
                assertEquals(0f, shield.getModifiers().get(stat).floatValue());
            }
        }
    }
}
