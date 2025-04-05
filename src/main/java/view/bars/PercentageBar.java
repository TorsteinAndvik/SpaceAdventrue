package view.bars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import model.utils.FloatPair;

public class PercentageBar {
    protected float maxValue;
    protected float currentValue;
    protected float scaleX = 1f;
    protected float scaleY = 1f;

    private static final float MIN_SCALE = 0.001f;

    protected Rectangle dimensions;
    protected Rectangle bar;

    protected Color outlineColor;
    protected Color barColor;
    protected Color bgColor;

    protected boolean visible = true;
    protected boolean drawOutline = false;

    // TODO: Extend functionality with a drawOutline option
    // TODO: Extend functionality with a drawNotches option

    public PercentageBar(Rectangle dimensions, float maxValue, float currentValue,
            Color barColor, Color bgColor, Color outlineColor) {
        this.dimensions = dimensions;
        this.bar = copyRectangle(dimensions);

        setMaxValue(maxValue);
        setCurrentValue(currentValue);

        updateBar();

        this.barColor = barColor;
        this.bgColor = bgColor;
        this.outlineColor = outlineColor;
    }

    public PercentageBar(Rectangle dimensions, float maxValue, float currentValue) {
        this(dimensions, maxValue, currentValue, Color.BLACK, Color.WHITE, Color.BLACK);
    }

    public PercentageBar(Rectangle dimensions, float maxValue) {
        this(dimensions, maxValue, maxValue);
    }

    public PercentageBar(float maxValue, float currentValue) {
        this();
        setMaxValue(maxValue);
        setCurrentValue(currentValue);
    }

    public PercentageBar() {
        this(new Rectangle(0f, 0f, 1f, 1f), 1f);
    }

    private Rectangle copyRectangle(Rectangle rect) {
        return new Rectangle(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Updates the current value of the bar by the given delta.
     * 
     * @param delta the amount to update the current value by.
     */
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
     * <p>
     * <code>this</code> is only drawn if visibility has been set to true
     * by <code>setVisible(true)</code>. <code>this</code> is visible by default.
     * <p>
     * The ShapeRenderer must be begun and ended outside this method.
     * 
     * @param renderer the ShapeRenderer to draw with.
     */
    public void draw(ShapeRenderer renderer) {
        if (!visible) {
            return;
        }

        Color oldColor = renderer.getColor();

        float outlineWidth = 0f;
        if (drawOutline) {
            outlineWidth = 0.1f * Math.min(scaleX * dimensions.width, scaleY * dimensions.height);
            renderer.setColor(outlineColor);
            renderer.rect(dimensions.x, dimensions.y, scaleX * dimensions.width, scaleY * dimensions.height);
        }

        renderer.setColor(bgColor);
        renderer.rect(dimensions.x + outlineWidth, dimensions.y + outlineWidth,
                scaleX * dimensions.width - 2f * outlineWidth, scaleY * dimensions.height - 2f * outlineWidth);

        renderer.setColor(barColor);
        renderer.rect(bar.x + outlineWidth, bar.y + outlineWidth,
                scaleX * bar.width - 2f * outlineWidth, scaleY * bar.height - 2f * outlineWidth);

        renderer.setColor(oldColor);
    }

    /**
     * @param barColor the new color of the bar.
     */
    public void setBarColor(Color barColor) {
        this.barColor = barColor;
    }

    /**
     * @param barColor the new color of the background.
     */
    public void setBackgroundColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * @param outlineColor the new color of the outline.
     */
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    /**
     * @param barColor the new color of the bar.
     * @param bgColor  the new color of the background.
     */
    public void setColors(Color barColor, Color bgColor) {
        setBarColor(barColor);
        setBackgroundColor(bgColor);
    }

    /**
     * @param barColor     the new color of the bar.
     * @param bgColor      the new color of the background.
     * @param outlineColor the new color of the outline.
     */
    public void setColors(Color barColor, Color bgColor, Color outlineColor) {
        setBarColor(barColor);
        setBackgroundColor(bgColor);
        setOutlineColor(outlineColor);
    }

    /**
     * Sets the current value of the bar to a minimum of 0
     * and a maximum of <code>maxValue</code>.
     * <p>
     * Updates the bar width.
     * 
     * @param currentValue the new current value
     */
    public void setCurrentValue(float currentValue) {
        this.currentValue = Math.min(Math.max(0f, currentValue), maxValue);
        updateBar();
    }

    /**
     * Sets the max value of the bar to a minimum of <code>1f</code>.
     * <p>
     * If current value would become larger than <code>maxValue</code>,
     * the current value is set equal to <code>maxValue</code>.
     * <p>
     * Updates the bar width.
     * 
     * @param maxValue the new max value
     */
    public void setMaxValue(float maxValue) {
        this.maxValue = Math.max(1f, maxValue);
        this.currentValue = Math.min(currentValue, maxValue);
        updateBar();
    }

    /**
     * Sets the scaling of the bar when drawing,
     * to a minimum of {@value #MIN_SCALE}.
     * <p>
     * Note that if you're rescaling and using <code>setCenter()</code>,
     * the center must be set after the scaling.
     * 
     * @param scaleX the scalar for the bar's width
     * @param scaleY the scalar for the bar's height
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = Math.max(MIN_SCALE, scaleX);
        this.scaleY = Math.max(MIN_SCALE, scaleY);
    }

    /**
     * Sets the scaling of the bar when drawing,
     * to a minimum of {@value #MIN_SCALE}.
     * <p>
     * Note that if you're rescaling and using <code>setCenter()</code>,
     * the center must be set after the scaling.
     * 
     * @param scale the scale to set x-scale and y-scale to.
     */
    public void setScale(float scale) {
        setScale(scale, scale);
    }

    /**
     * Sets the bottom-left position of the percentage bar.
     * 
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        dimensions.x = x;
        dimensions.y = y;
        bar.x = x;
        bar.y = y;
    }

    /**
     * Sets the bottom-left position of the percentage bar.
     * 
     * @param pos a <code>FloatPair</code> holding the x and y coordinates to set
     *            the position to
     */
    public void setPosition(FloatPair pos) {
        setPosition(pos.x(), pos.y());
    }

    /**
     * Sets the center position of the percentage bar.
     * 
     * @param x
     * @param y
     */
    public void setCenter(float x, float y) {
        float renderedWidth = scaleX * dimensions.width;
        float renderedHeight = scaleY * dimensions.height;
        setPosition(x - renderedWidth / 2f, y - renderedHeight / 2f);
    }

    /**
     * Sets the center position of the percentage bar.
     * 
     * @param pos a <code>FloatPair</code> holding the x and y coordinates to set
     *            the center position to
     */
    public void setCenter(FloatPair pos) {
        setCenter(pos.x(), pos.y());
    }

    /**
     * @return a <code>FloatPair</code> holding the x and y coordinates of the
     *         percentage bar.
     */
    public FloatPair getPosition() {
        return new FloatPair(dimensions.x, dimensions.y);
    }

    /**
     * Sets the dimensions of the percentage bar.
     * Updates the percentage bar's width if necessary.
     * 
     * @param dimensions the new dimensions
     */
    public void setDimensions(Rectangle dimensions) {
        this.dimensions = dimensions;
        bar.x = dimensions.x;
        bar.y = dimensions.y;
        bar.height = dimensions.height;
        updateBar();
    }

    /**
     * @param visible boolean to set visibility of <code>this</code> to.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @param drawOutline boolean to set outline visibility to.
     */
    public void setDrawOutline(boolean drawOutline) {
        this.drawOutline = drawOutline;
    }
}
