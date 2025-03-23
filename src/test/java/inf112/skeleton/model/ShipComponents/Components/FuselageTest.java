package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.model.ShipComponents.UpgradeType;
import inf112.skeleton.model.constants.PhysicsParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FuselageTest {

    private Fuselage fuselageWithoutUpgrade;
    private Fuselage fuselageWithUpgrade;

    @BeforeEach
    void setUp() {
        fuselageWithoutUpgrade = new Fuselage();
        fuselageWithUpgrade = new Fuselage(new Turret());
    }

    @Test
    void setUpgradeTest() {
        ShipUpgrade upgrade = new Turret();
        fuselageWithoutUpgrade.setUpgrade(upgrade);
        assertEquals(upgrade, fuselageWithoutUpgrade.getUpgrade());
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

}