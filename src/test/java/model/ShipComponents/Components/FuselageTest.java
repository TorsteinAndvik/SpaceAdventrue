package model.ShipComponents.Components;

import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.stats.Stat;
import model.constants.PhysicsParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FuselageTest {

    private Fuselage fuselageWithoutUpgrade;
    private Fuselage fuselageWithUpgrade;

    @BeforeEach
    void setUp() {
        fuselageWithoutUpgrade = new Fuselage();
        fuselageWithUpgrade = new Fuselage(new Turret());
    }

    @Test
    void constructorTest() {
        assertFalse((new Fuselage(new Fuselage())).hasUpgrade());
    }

    @Test
    void setUpgradeTest() {
        ShipUpgrade upgrade = new Turret();
        assertTrue(fuselageWithoutUpgrade.setUpgrade(upgrade));
        assertFalse(fuselageWithoutUpgrade.setUpgrade(upgrade)); // already has upgrade
        assertEquals(upgrade, fuselageWithoutUpgrade.getUpgrade());

        Fuselage emptyFuselage = new Fuselage();
        assertFalse(emptyFuselage.setUpgrade(null));
        assertFalse(emptyFuselage.setUpgrade(new Fuselage()));
    }

    @Test
    void removeUpgradeTest() {
        UpgradeType upgradeType = fuselageWithUpgrade.removeUpgrade();
        assertEquals(upgradeType, new Turret().getType());
        assertNull(fuselageWithUpgrade.getUpgrade());
    }

    @Test
    void correctMassTest() {
        assertEquals(PhysicsParameters.fuselageMass, fuselageWithoutUpgrade.getMass());
        assertEquals(PhysicsParameters.fuselageMass + PhysicsParameters.shipUpgradeMass,
                fuselageWithUpgrade.getMass());
    }

    @Test
    void toStringTest() {
        assertEquals("fuselage: empty", fuselageWithoutUpgrade.toString());

        ShipUpgrade turret = new Turret();
        assertEquals("fuselage: " + turret.getName(), fuselageWithUpgrade.toString());

        fuselageWithUpgrade.removeUpgrade();
        assertEquals("fuselage: empty", fuselageWithUpgrade.toString());
    }

    @Test
    void resourceValueTest() {
        assertTrue(fuselageWithoutUpgrade.getResourceValue() > 0);
        assertEquals(fuselageWithoutUpgrade.getResourceValue() + (new Turret()).getResourceValue(),
                fuselageWithUpgrade.getResourceValue());
    }

    @Test
    void getStatModifierTest() {
        assertEquals(PhysicsParameters.fuselageMass, fuselageWithoutUpgrade.getModifiers().get(Stat.MASS).floatValue());
        assertEquals(PhysicsParameters.fuselageMass + PhysicsParameters.shipUpgradeMass,
                fuselageWithUpgrade.getModifiers().get(Stat.MASS).floatValue());

        assertEquals(PhysicsParameters.fuselageMass,
                fuselageWithoutUpgrade.getStatModifier().getModifiers().get(Stat.MASS).floatValue());
        assertEquals(PhysicsParameters.fuselageMass + PhysicsParameters.shipUpgradeMass,
                fuselageWithUpgrade.getStatModifier().getModifiers().get(Stat.MASS).floatValue());
    }
}