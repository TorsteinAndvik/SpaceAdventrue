package view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

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
        PercentageBar newBar = new PercentageBar(rect, maxValue, currentValue, barColor, bgColor);

        assertEquals(rect, newBar.dimensions);
        assertEquals(currentValue, newBar.currentValue);
        assertEquals(maxValue, newBar.maxValue);
        assertEquals(barColor, newBar.barColor);
        assertEquals(bgColor, newBar.bgColor);
        assertEquals(rect.x, newBar.bar.x);
        assertEquals(rect.y, newBar.bar.y);
        assertEquals(rect.height, newBar.bar.height);
        assertEquals(rect.width * currentValue / maxValue, newBar.bar.width);
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
}
