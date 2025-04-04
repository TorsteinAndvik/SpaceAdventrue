package view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PercentageBar {
    protected float maxValue;
    protected float currentValue;
    protected float scaleX = 1f;
    protected float scaleY = 1f;

    protected Rectangle dimensions;
    protected Rectangle bar;

    protected Color barColor;
    protected Color bgColor;

    // TODO: Extend functionality with a drawOutline option
    // TODO: Extend functionality with a drawNotches option

    public PercentageBar(Rectangle dimensions, float maxValue, float currentValue, Color barColor, Color bgColor) {
        this.dimensions = dimensions;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.barColor = barColor;
        this.bgColor = bgColor;
    }

    public PercentageBar(Rectangle dimensions, float maxValue, float currentValue) {
        this(dimensions, maxValue, currentValue, Color.BLACK, Color.WHITE);
    }

    public PercentageBar(Rectangle dimensions, float maxValue) {
        this(dimensions, maxValue, maxValue);
    }

    public PercentageBar() {
        this(new Rectangle(0f, 0f, 1f, 1f), 1f);
    }

    public void update(float delta) {
        setCurrentValue(currentValue + delta);
    }

    private void updateBar() {
        float ratio = currentValue / maxValue;
        float length = dimensions.width * ratio;
        bar.setWidth(length);
    }

    /**
     * Draws the percentage bar at the x and y location set by
     * <code>setDimensions(Rectangle dimensions)</code>,
     * using its width and height scaled by the scalars set by
     * <code>setScale(float scaleX, float scaleY)</code> or
     * <code>setScale(float scale)</code>.
     * 
     * The ShapeRenderer must be begun and ended outside this method.
     * 
     * @param renderer the ShapeRenderer to draw with.
     */
    public void draw(ShapeRenderer renderer) {
        Color oldColor = renderer.getColor();

        renderer.setColor(bgColor);
        renderer.rect(dimensions.x, dimensions.y, scaleX * dimensions.width, scaleY * dimensions.height);

        renderer.setColor(barColor);
        renderer.rect(bar.x, bar.y, scaleX * bar.width, scaleY * bar.height);

        renderer.setColor(oldColor);
    }

    public void setBarColor(Color barColor) {
        this.barColor = barColor;
    }

    public void setBackgroundColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setColors(Color barColor, Color bgColor) {
        setBarColor(barColor);
        setBackgroundColor(bgColor);
    }

    /**
     * Sets the current value of the bar to a minimum of 0.
     * Updates the bar width.
     * 
     * @param currentValue the new current value
     */
    public void setCurrentValue(float currentValue) {
        this.currentValue = Math.max(0f, currentValue);
        updateBar();
    }

    /**
     * Sets the max value of the bar to a minimum of 0.
     * Updates the bar width.
     * 
     * @param maxValue the new max value
     */
    public void setMaxValue(float maxValue) {
        this.maxValue = Math.max(0f, maxValue);
    }

    /**
     * Sets the scaling of the bar when drawing.
     * 
     * @param scaleX the scalar for the bar's width
     * @param scaleY the scalar for the bar's height
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = Math.max(0f, scaleX);
        this.scaleY = Math.max(0f, scaleY);
    }

    /**
     * Sets the scaling of the bar when drawing.
     * 
     * @param scale the scale to set x-scale and y-scale to.
     */
    public void setScale(float scale) {
        setScale(scale, scale);
    }
}
