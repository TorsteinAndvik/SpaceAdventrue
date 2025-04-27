package view.bars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DiffBarTest {

    private DiffBar bar;

    @BeforeEach
    public void setup() {
        bar = new DiffBar(10f, 5f);
    }

    @Test
    public void constructorTest() {
        assertEquals(10f, bar.maxValue);
        assertEquals(5f, bar.currentValue);
        assertEquals(0f, bar.diffValue);
        assertNotNull(bar.diffIsBeneficial);
        assertNotNull(bar.goodColor);
        assertNotNull(bar.badColor);

        // default constructor test initializes fields 'legally'
        bar = new DiffBar();
        assertTrue(bar.maxValue >= DiffBar.MIN_MAX_VALUE);
        assertTrue(bar.currentValue >= DiffBar.MIN_CURRENT_VALUE);
        assertTrue(bar.currentValue <= bar.maxValue);
        assertNotNull(bar.diffValue);
        assertNotNull(bar.diffIsBeneficial);
        assertNotNull(bar.goodColor);
        assertNotNull(bar.badColor);
    }

    @Test
    public void updateDiffTest() {
        bar.updateDiff(5f, true);
        assertEquals(5f, bar.diffValue);
        assertTrue(bar.diffIsBeneficial);

        bar.updateDiff(6f, false);
        assertEquals(5f, bar.diffValue); // current + diff cannot exceed max
        assertFalse(bar.diffIsBeneficial);

        bar.updateDiff(-3f, false);
        assertEquals(-3f, bar.diffValue);

        // negative diff while *positive* isn't beneficial means diff is beneficial
        assertTrue(bar.diffIsBeneficial);

        bar.updateDiff(-7f, true);
        assertEquals(-5f, bar.diffValue);
        // negative diff while *positive* is beneficial means diff isn't beneficial
        assertFalse(bar.diffIsBeneficial);
    }
}
