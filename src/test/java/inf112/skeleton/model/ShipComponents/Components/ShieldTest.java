package inf112.skeleton.model.ShipComponents.Components;

import inf112.skeleton.model.ShipComponents.UpgradeStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShieldTest {

    Shield shield;
    Shield superShield;

    @BeforeEach
    void setUp() {
        shield = new Shield();
        superShield = new Shield(UpgradeStage.MAX);
    }

    @Test
    void shieldTest() {
        assertEquals("Shield", shield.getName());
        assertEquals(UpgradeStage.ZERO, shield.getStage());
    }

    @Test
    void superShieldTest() {
        assertEquals("Shield", superShield.getName());
        assertEquals(UpgradeStage.MAX, superShield.getStage());
    }
}