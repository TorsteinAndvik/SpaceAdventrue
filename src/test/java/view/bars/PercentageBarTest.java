package view.bars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import model.utils.FloatPair;

public class PercentageBarTest {

    PercentageBar bar;

    @BeforeEach
    public void setup() {
        bar = new PercentageBar();
    }

    @Test
    public void constructorTest() {
        Rectangle rect = new Rectangle(0f, 1f, 2f, 3f);
        float currentValue = 4f;
        float maxValue = 5f;
        Color barColor = Color.FIREBRICK;
        Color bgColor = Color.MAROON;
        Color outlineColor = Color.ROYAL;

        PercentageBar newBar = new PercentageBar(rect, maxValue, currentValue, barColor, bgColor, outlineColor);

        assertEquals(rect, newBar.dimensions);
        assertEquals(currentValue, newBar.currentValue);
        assertEquals(maxValue, newBar.maxValue);
        assertEquals(barColor, newBar.barColor);
        assertEquals(bgColor, newBar.bgColor);
        assertEquals(outlineColor, newBar.outlineColor);
        assertEquals(rect.x, newBar.bar.x);
        assertEquals(rect.y, newBar.bar.y);
        assertEquals(rect.height, newBar.bar.height);
        assertEquals(rect.width * currentValue / maxValue, newBar.bar.width);

        newBar = new PercentageBar(maxValue, currentValue);
        assertEquals(maxValue, newBar.maxValue);
        assertEquals(currentValue, newBar.currentValue);

        assertNotNull(newBar.dimensions);
        assertNotNull(newBar.bar);
        assertNotNull(newBar.barColor);
        assertNotNull(newBar.bgColor);
        assertNotNull(newBar.outlineColor);
        assertNotNull(newBar.numNotches);
        assertNotNull(newBar.drawOutline);
        assertNotNull(newBar.halfNotch);
        assertNotNull(newBar.visible);
        assertNotNull(newBar.notchScale);
        assertNotNull(newBar.outlineScale);
        assertNotNull(newBar.outlineWidth);
    }

    @Test
    public void updateTest() {
        float currentValue = bar.currentValue;
        float half = currentValue / 2;

        assertEquals(currentValue, bar.currentValue);
        bar.update(-half);
        assertEquals(currentValue - half, bar.currentValue);
        bar.update(half);
        assertEquals(currentValue, bar.currentValue);

        float maxValue = bar.maxValue;
        bar.update(2f * maxValue);
        assertEquals(maxValue, bar.currentValue);

        bar.update(-2f * maxValue);
        assertEquals(0f, bar.currentValue);
    }

    @Test
    public void dimensionsTest() {
        Rectangle rect = new Rectangle(0f, 1f, 2f, 3f);
        bar.setDimensions(rect);
        assertEquals(rect, bar.dimensions);
        assertEquals(2f * bar.currentValue / bar.maxValue, bar.bar.width);

        Rectangle rect2 = new Rectangle(0f, 1f, 3f, 3f);
        bar.setDimensions(rect2);
        assertEquals(rect2, bar.dimensions);
        assertEquals(3f * bar.currentValue / bar.maxValue, bar.bar.width);
    }

    @Test
    public void ratioTest() {
        bar.setMaxValue(1f);
        bar.setCurrentValue(1f);

        assertEquals(1f, bar.bar.width / bar.dimensions.width);

        bar.update(-0.3f);
        assertEquals(0.7f, bar.bar.width / bar.dimensions.width);

        bar.setDimensions(new Rectangle(0f, 0f, 3f * bar.dimensions.width, bar.dimensions.height));
        assertEquals(0.7f, bar.bar.width / bar.dimensions.width);

        bar.update(0.1f);
        assertEquals(0.8f, bar.bar.width / bar.dimensions.width);
    }

    @Test
    public void scaleTest() {
        assertEquals(1f, bar.scaleX);
        assertEquals(1f, bar.scaleY);

        bar.setScale(2f);
        assertEquals(2f, bar.scaleX);
        assertEquals(2f, bar.scaleY);

        bar.setScale(3f, 0.5f);
        assertEquals(3f, bar.scaleX);
        assertEquals(0.5f, bar.scaleY);
    }

    @Test
    public void visibilityTest() {
        bar.setVisible(true);
        assertTrue(bar.visible);
        bar.setVisible(true);
        assertTrue(bar.visible);
        bar.setVisible(false);
        assertFalse(bar.visible);
        bar.setVisible(false);
        assertFalse(bar.visible);
        bar.setVisible(true);
        assertTrue(bar.visible);
    }

    @Test
    public void setAndGetPositionTest() {
        assertEquals(new FloatPair(0f, 0f), bar.getPosition());

        bar.setPosition(new FloatPair(-1f, 2f));

        assertEquals(new FloatPair(-1f, 2f), bar.getPosition());
        assertEquals(new Rectangle(-1f, 2f, 1f, 1f), bar.dimensions);
        assertEquals(new Rectangle(-1f, 2f, 1f, 1f), bar.bar);
    }

    @Test
    public void setCenterTest() {
        bar.setCurrentValue(0.7f);
        bar.setCenter(new FloatPair(-3f, 5f));
        assertEquals(new Rectangle(-3.5f, 4.5f, 1f, 1f), bar.dimensions);
        assertEquals(new Rectangle(-3.5f, 4.5f, 0.7f, 1f), bar.bar);
    }

    @Test
    public void additionalSetterTest() {
        bar.setColors(Color.RED, Color.WHITE, Color.BLUE);
        assertEquals(Color.RED, bar.barColor);
        assertEquals(Color.WHITE, bar.bgColor);
        assertEquals(Color.BLUE, bar.outlineColor);

        bar.setColors(Color.WHITE, Color.BLUE, Color.RED);
        assertEquals(Color.WHITE, bar.barColor);
        assertEquals(Color.BLUE, bar.bgColor);
        assertEquals(Color.RED, bar.outlineColor);

        bar.setNotchScale(0.5f);
        assertEquals(0.5f, bar.notchScale);
        bar.setNotchScale(0.2f);
        assertEquals(0.2f, bar.notchScale);
        bar.setNotchScale(-1f);
        assertEquals(PercentageBar.MIN_NOTCH_SCALE, bar.notchScale);

        bar.setHalfNotch(false);
        assertFalse(bar.halfNotch);
        bar.setHalfNotch(true);
        assertTrue(bar.halfNotch);

        bar.setDrawOutline(false);
        assertFalse(bar.drawOutline);
        bar.setDrawOutline(true);
        assertTrue(bar.drawOutline);

        bar.setNumNotches(0);
        assertEquals(0, bar.numNotches);
        bar.setNumNotches(-1323);
        assertEquals(PercentageBar.MIN_NUM_NOTCHES, bar.numNotches);
        bar.setNumNotches(5);
        assertEquals(5, bar.numNotches);
        bar.setNumNotches(125);
        assertEquals(PercentageBar.MAX_NUM_NOTCHES, bar.numNotches);

        bar.setOutlineScale(0f);
        assertEquals(PercentageBar.MIN_OUTLINE_SCALE, bar.outlineScale);
        bar.setOutlineScale(1f);
        assertEquals(PercentageBar.MAX_OUTLINE_SCALE, bar.outlineScale);
        bar.setOutlineScale(0.15f);
        assertEquals(0.15f, bar.outlineScale);
    }

    @Test
    public void minDimensionTest() {
        bar.setScale(0.5f, 1f);
        assertEquals(0.5f, bar.minDimension());

        bar.setScale(0.6f, 0.55f);
        assertEquals(0.55f, bar.minDimension());
    }
}
