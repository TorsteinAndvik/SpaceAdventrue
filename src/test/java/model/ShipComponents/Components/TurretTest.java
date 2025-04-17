package model.ShipComponents.Components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.Components.stats.Stat;
import model.constants.PhysicsParameters;

public class TurretTest {

    @Test
    void constructorTest() {
        Turret turret = new Turret();
        assertEquals(UpgradeStage.ZERO, turret.getStage());

        for (Stat stat : Stat.values()) {
            if (stat == Stat.MASS) {
                assertTrue(turret.getModifiers().get(stat).floatValue() == PhysicsParameters.shipUpgradeMass);
            } else if (stat == Stat.FIRE_RATE) {
                assertTrue(turret.getModifiers().get(stat).floatValue() > 0f);
            } else if (stat == Stat.RESOURCE_VALUE) {
                assertTrue(turret.getModifiers().get(stat).intValue() > 0);
            } else {
                assertTrue(turret.getModifiers().get(stat).floatValue() == 0f);
            }
        }
    }

    @Test
    void turretBarrelLocationTest() {
        assertNotNull(Turret.turretBarrelLocation());
    }

    @Test
    void shootingTest() {
        Turret turret = new Turret();

        assertTrue(turret.canShoot());
        assertFalse(turret.shoot());

        turret.setToShoot(true);

        assertTrue(turret.shoot());
        turret.setToShoot(true);
        assertFalse(turret.shoot()); // need to reload

        float offset = 0.01f;
        turret.update(1f / turret.getModifiers().get(Stat.FIRE_RATE).floatValue() - offset);
        assertFalse(turret.shoot());
        turret.update(offset);
        assertTrue(turret.shoot());
    }
}
