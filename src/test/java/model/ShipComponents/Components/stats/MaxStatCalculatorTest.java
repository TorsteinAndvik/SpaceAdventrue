package model.ShipComponents.Components.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.Shield;
import model.ShipComponents.Components.Thruster;
import model.ShipComponents.Components.Turret;

public class MaxStatCalculatorTest {
    private MaxStatCalculator calculator;

    @BeforeEach
    private void setup() {
        calculator = new MaxStatCalculator();
    }

    private void compare(StatModifier expected, StatModifier actual) {
        for (Stat stat : Stat.values()) {
            assertEquals(expected.getModifiers().get(stat), actual.getModifiers().get(stat));
        }
    }

    @Test
    public void fuselageMaxTest() {
        StatModifier expected = new StatModifier();
        compare(expected, calculator.getFuselageMax(0));

        Fuselage turret = new Fuselage(new Turret(UpgradeStage.MAX), UpgradeStage.MAX);
        Fuselage shield = new Fuselage(new Shield(UpgradeStage.MAX), UpgradeStage.MAX);
        Fuselage thruster = new Fuselage(new Thruster(UpgradeStage.MAX), UpgradeStage.MAX);

        for (Stat stat : Stat.values()) {
            Number statValue = turret.getModifiers().get(stat);
            if (statValue.floatValue() > (expected.getModifiers().get(stat)).floatValue()) {
                expected.setModifier(stat, statValue);
            }
            statValue = shield.getModifiers().get(stat);
            if (statValue.floatValue() > (expected.getModifiers().get(stat)).floatValue()) {
                expected.setModifier(stat, statValue);
            }
            statValue = thruster.getModifiers().get(stat);
            if (statValue.floatValue() > (expected.getModifiers().get(stat)).floatValue()) {
                expected.setModifier(stat, statValue);
            }
        }

        compare(expected, calculator.getFuselageMax(1));
        compare(expected.addModifier(expected), calculator.getFuselageMax(2));
        compare(expected.addModifier(expected), calculator.getFuselageMax(4));
    }
}
