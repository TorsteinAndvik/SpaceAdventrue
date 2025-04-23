package model.ShipComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UpgradeTypeTest {

    @Test
    public void getUpgradeTypeTest() {
        assertEquals(UpgradeType.FUSELAGE, UpgradeType.getUpgradeType("fuselage"));
        assertEquals(UpgradeType.THRUSTER, UpgradeType.getUpgradeType("thruster"));
        assertEquals(UpgradeType.TURRET, UpgradeType.getUpgradeType("turret"));
        assertEquals(UpgradeType.SHIELD, UpgradeType.getUpgradeType("shield"));

        assertEquals(UpgradeType.FUSELAGE, UpgradeType.getUpgradeType("FuSeLaGe"));
        assertEquals(UpgradeType.FUSELAGE, UpgradeType.getUpgradeType("fUsElAgE"));
        assertEquals(UpgradeType.TURRET, UpgradeType.getUpgradeType("tuRRet"));

        assertThrows(IllegalArgumentException.class, () -> UpgradeType.getUpgradeType("skjold"));
    }
}
