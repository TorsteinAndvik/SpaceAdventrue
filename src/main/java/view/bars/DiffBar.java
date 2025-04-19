package view.bars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import view.Palette;

public class DiffBar extends PercentageBar {
    private float diffValue = 0f;
    private boolean diffIsBeneficial = true;
    private Color goodColor = Palette.MUTED_GREEN_LIGHT;
    private Color badColor = Palette.MUTED_RED_LIGHT;

    public DiffBar(float maxValue, float currentValue) {
        super(maxValue, currentValue);
        setup();
    }

    public DiffBar() {
        super();
        setup();
    }

    private void setup() {
        setDrawOutline(true);
        setColors(Palette.WHITE, Palette.BLACK, Palette.BLACK);
        setNumNotches(0);
    }

    /**
     * Updates the diff value of the bar. The value is clamped such that
     * <code>currentValue</code> + <code>diffValue</code> remains between
     * <code>0f</code> and <code>maxValue</code>.
     * 
     * @param diffValue
     * @param positiveIsBeneficial
     */
    public void updateDiff(float diffValue, boolean positiveIsBeneficial) {
        this.diffValue = Math.max(-currentValue, Math.min(diffValue, maxValue - currentValue));
        this.diffIsBeneficial = positiveIsBeneficial == (diffValue > 0f);
    }

    @Override
    public void draw(ShapeRenderer renderer) {
        super.draw(renderer);
        if (diffValue == 0f) {
            return;
        }

        Color oldColor = renderer.getColor();
        if (diffIsBeneficial) {
            renderer.setColor(goodColor);
        } else {
            renderer.setColor(badColor);
        }

        float x = bar.x + outlineWidth + barWidth();
        float y = bar.y + outlineWidth;
        float width = outlineWidthScaleFactor() * scaleX * diffValue / maxValue;
        renderer.rect(x, y, width, (scaleY * bar.height - 2f * outlineWidth) / 2f);

        renderer.setColor(oldColor);
    }
}
