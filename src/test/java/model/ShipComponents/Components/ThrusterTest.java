package model.ShipComponents.Components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.Components.stats.Stat;
import model.constants.PhysicsParameters;

public class ThrusterTest {

    @Test
    void constructorTest() {
        Thruster thruster = new Thruster();
        assertEquals(UpgradeStage.ZERO, thruster.getStage());

        for (Stat stat : Stat.values()) {
            if (stat == Stat.MASS) {
                assertTrue(thruster.getModifiers().get(stat).floatValue() == PhysicsParameters.shipUpgradeMass);
            } else if (stat == Stat.ACCELERATION_FORCE) {
                assertTrue(thruster.getModifiers().get(stat).floatValue() > 0f);
            } else if (stat == Stat.RESOURCE_VALUE) {
                assertTrue(thruster.getModifiers().get(stat).intValue() > 0);
            } else if (stat == Stat.MAX_SPEED) {
                assertTrue(thruster.getModifiers().get(stat).floatValue() > 0f);
            } else {
                assertTrue(thruster.getModifiers().get(stat).floatValue() == 0f);
            }
        }
    }

    @Test
    void thrusterFlameLocationTest() {
        assertNotNull(Thruster.thrusterFlameLocation());
    }
}
