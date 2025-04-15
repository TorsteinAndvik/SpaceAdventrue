package model.ShipComponents.Components;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.constants.PhysicsParameters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpgradesTest {

    private Shield shield;
    private Shield superShield;

    private Thruster thruster;
    private Thruster superThruster;

    private Turret turret;
    private Turret superTurret;

    @BeforeEach
    void setUp() {
        shield = new Shield();
        superShield = new Shield(UpgradeStage.MAX);

        thruster = new Thruster();
        superThruster = new Thruster(UpgradeStage.MAX);

        turret = new Turret();
        superTurret = new Turret(UpgradeStage.MAX);
    }

    @Test
    void shieldTest() {
        assertEquals("Shield", shield.getName());
        assertEquals(UpgradeStage.ZERO, shield.getStage());
        assertEquals("Reduces damage taken from enemies and collisions", shield.getDescription());
        assertEquals(UpgradeType.SHIELD, shield.getType());
    }

    @Test
    void superShieldTest() {
        assertEquals("Shield", superShield.getName());
        assertEquals(UpgradeStage.MAX, superShield.getStage());
        assertEquals("Reduces damage taken from enemies and collisions", shield.getDescription());
        assertEquals(UpgradeType.SHIELD, shield.getType());
    }

    @Test
    void thrusterTest() {
        assertEquals("Thruster", thruster.getName());
        assertEquals(UpgradeStage.ZERO, thruster.getStage());
        assertEquals("Increases the speed of the ship", thruster.getDescription());
        assertEquals(UpgradeType.THRUSTER, thruster.getType());
    }

    @Test
    void superThrusterTest() {
        assertEquals("Thruster", superThruster.getName());
        assertEquals(UpgradeStage.MAX, superThruster.getStage());
        assertEquals("Increases the speed of the ship", superThruster.getDescription());
        assertEquals(UpgradeType.THRUSTER, superThruster.getType());

    }

    @Test
    void turretTest() {
        assertEquals("Turret", turret.getName());
        assertEquals(UpgradeStage.ZERO, turret.getStage());
        assertEquals("Fires lasers at enemies and asteroids", turret.getDescription());
        assertEquals(UpgradeType.TURRET, turret.getType());
    }

    @Test
    void superTurretTest() {
        assertEquals("Turret", superTurret.getName());
        assertEquals(UpgradeStage.MAX, superTurret.getStage());
        assertEquals("Fires lasers at enemies and asteroids", superTurret.getDescription());
        assertEquals(UpgradeType.TURRET, superTurret.getType());
    }

    @Test
    void upgradesAreUpgradeableTest() {
        assertTrue(shield.upgrade());
        assertTrue(thruster.upgrade());
        assertTrue(turret.upgrade());
    }

    @Test
    void upgradesAreNotUpgradeableTest() {
        assertFalse(superShield.upgrade());
        assertFalse(superThruster.upgrade());
        assertFalse(superTurret.upgrade());
    }

    @Test
    void correctMassTest() {
        assertEquals(PhysicsParameters.shipUpgradeMass, shield.getMass());
        assertEquals(PhysicsParameters.shipUpgradeMass, thruster.getMass());
        assertEquals(PhysicsParameters.shipUpgradeMass, turret.getMass());

        // Note: Super upgrades may have a different mass in future
        String message = "mass does not match value specified in PhysicsParameters (did you implement or change an upgrade such that the mass changed without updating PhysicsParameters accordingly?)";
        assertEquals(PhysicsParameters.shipUpgradeMass, superShield.getMass(),
                "superShield: " + message);
        assertEquals(PhysicsParameters.shipUpgradeMass, superThruster.getMass(),
                "superThruster: " + message);
        assertEquals(PhysicsParameters.shipUpgradeMass, superTurret.getMass(),
                "superTurret: " + message);
    }
}