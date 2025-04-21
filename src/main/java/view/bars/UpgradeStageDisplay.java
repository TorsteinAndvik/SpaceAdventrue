package view.bars;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import model.ShipComponents.UpgradeStage;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.Fuselage;
import model.ShipComponents.Components.ShipUpgrade;
import model.ShipComponents.Components.stats.Stat;
import model.ShipComponents.Components.stats.StatModifier;
import model.utils.FloatPair;
import view.Palette;

public class UpgradeStageDisplay {

    private Fuselage fuselage;
    private Map<UpgradeType, Map<UpgradeStage, Sprite>> upgradeSprites;
    private FloatPair position;
    private FloatPair diffBarScales;
    private float padding = 0.05f;
    private float barPaddingTextScale = 0.5f;
    private Map<Stat, DiffBar> bars;
    private Map<Stat, GlyphLayout> glyphLayouts;
    private float spriteRadius;

    private final FloatPair displayDimensions;

    private Rectangle fuselageHitbox;
    private Rectangle upgradeHitbox;

    private GlyphLayout priceGlyphLayout = new GlyphLayout();
    private int fuselagePrice = 25;
    private int upgradePrice = 50;

    private boolean fuselageMode;
    private boolean fuselageUpgradeEligible;
    private boolean upgradeUpgradeEligible;

    private boolean visible = true;

    private Color outlineColor = Palette.BLACK;
    private Color displayColor = Palette.MUTED_GREEN_LIGHT;
    private BitmapFont font;

    public UpgradeStageDisplay(StatModifier max, Map<UpgradeType, Map<UpgradeStage, Sprite>> upgradeSprites,
            BitmapFont font, float spriteRadius) {

        this.upgradeSprites = upgradeSprites;
        this.diffBarScales = new FloatPair(3f, 0.1f);
        this.spriteRadius = spriteRadius;
        this.font = font;

        Map<Stat, DiffBar> bars = new HashMap<>();
        Map<Stat, GlyphLayout> glyphLayouts = new HashMap<>();
        float largestWidth = 0f;
        for (Stat stat : Stat.values()) {
            DiffBar statBar = new DiffBar();
            statBar.setScale(diffBarScales.x(), diffBarScales.y());
            statBar.setMaxValue(max.getModifiers().get(stat).floatValue());
            bars.put(stat, statBar);

            GlyphLayout glyphLayout = new GlyphLayout();
            glyphLayout.setText(font, stat.toString());
            glyphLayouts.put(stat, glyphLayout);
            if (glyphLayout.width > largestWidth) {
                largestWidth = glyphLayout.width;
            }
        }

        if (largestWidth > diffBarScales.x()) {
            diffBarScales = new FloatPair(largestWidth, diffBarScales.y());
        }

        this.bars = bars;
        this.glyphLayouts = glyphLayouts;

        this.position = new FloatPair(0f, 0f);

        displayDimensions = new FloatPair(getWidth(), getHeight());
        fuselageHitbox = new Rectangle(
                position.x() + 2f * padding,
                position.y() + 2f * padding,
                2f * spriteRadius,
                displayDimensions.y() - 4f * padding);

        upgradeHitbox = new Rectangle(
                position.x() + 3f * padding + 2f * spriteRadius,
                position.y() + 2f * padding,
                2f * spriteRadius,
                displayDimensions.y() - 4f * padding);
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisibility() {
        return visible;
    }

    public void setPosition(FloatPair position) {
        float deltaX = position.x() - this.position.x();
        float deltaY = position.y() - this.position.y();

        fuselageHitbox.setPosition(fuselageHitbox.x + deltaX, fuselageHitbox.y + deltaY);
        upgradeHitbox.setPosition(upgradeHitbox.x + deltaX, upgradeHitbox.y + deltaY);

        this.position = position;
    }

    public void setComponents(Fuselage fuselage) {
        this.fuselage = fuselage;

        if (fuselageMode || fuselage.getUpgrade() == null) {
            setStatDiffs(fuselage);
        } else {
            setStatDiffs(fuselage.getUpgrade());
        }
    }

    public Fuselage getFuselage() {
        return fuselage;
    }

    public void setFuselageMode(boolean fuselageMode) {
        this.fuselageMode = fuselageMode;
        if (fuselageMode) {
            setStatDiffs(fuselage);
        } else {
            setStatDiffs(fuselage.getUpgrade());
        }
    }

    public void setCurrentStats(StatModifier current) {
        for (Stat stat : Stat.values()) {
            bars.get(stat).setCurrentValue(current.getModifiers().get(stat).floatValue());
        }
    }

    private void setStatDiffs(ShipUpgrade upgrade) {
        StatModifier diff;
        if (upgrade != null && upgrade.getStage().isUpgradeable()) {
            diff = upgrade.getUpgradeStatModifier();
        } else {
            diff = new StatModifier();
        }

        for (Stat stat : Stat.values()) {
            bars.get(stat).updateDiff(diff.getModifiers().get(stat).floatValue(), stat.positiveIsBeneficial);
        }
    }

    /**
     * Renders the upgrade stage display if it is set to be visible.
     * <p>
     * Assumes <code>shape</code> and <code>batch</code> do not have
     * <code>begin()</code> called on them before calling this method.
     * <p>
     * It is up to the caller to ensure the proper projection matrices
     * have been set before calling this method.
     * <p>
     * This method will call <code>shape.end()</code>.
     * <code>shape</code> will retain its originally set <code>Color</code>.
     * <p>
     * <code>batch</code> will have <code>end()</code> called on it if
     * <code>endBatch</code> is <code>true</code>, otherwise it will not.
     * The option to not call <code>batch.end()</code> is to allow
     * for additional batch-rendering without an extra buffer dump.
     * 
     * @param batch    the <code>SpriteBatch</code> to render Sprites with.
     * @param shape    the <code>ShapeRenderer</code> to render the display box
     *                 with.
     * @param endBatch whether this method should call <code>batch.end()</code>
     *                 before returning.
     */
    public void render(SpriteBatch batch, ShapeRenderer shape, boolean endBatch) {
        if (!visible) {
            return;
        }

        float x = position.x();
        float y = position.y();
        float width = displayDimensions.x();
        float height = displayDimensions.y();

        Gdx.gl.glEnable(GL20.GL_BLEND); // enables alpha blending for ShapeRenderer
        shape.begin(ShapeType.Filled);
        renderShapes(shape, x, y, width, height);
        shape.end();

        batch.begin();
        renderSprites(batch, x, y, width, height);
        if (endBatch) {
            batch.end();
        }
    }

    private void renderShapes(ShapeRenderer shape, float x, float y, float width, float height) {
        Color oldColor = shape.getColor();

        shape.setColor(outlineColor); // outline
        shape.rect(x, y, width, height);

        shape.setColor(displayColor); // display background
        shape.rect(x + padding, y + padding, width - 2f * padding, height - 2f * padding);

        float barX = x + 4f * (padding + spriteRadius); // diffbars
        renderBars(shape, barX, y, width, height);

        shape.setColor(Palette.ORANGE); // draw selected upgrade comparison
        if (fuselageMode) {
            // draw orange highlights in corners
            shape.rect(fuselageHitbox.x - padding, fuselageHitbox.y - padding,
                    2f * padding, 2f * padding);

            shape.rect(fuselageHitbox.x + fuselageHitbox.width - padding, fuselageHitbox.y - padding,
                    2f * padding, 2f * padding);

            shape.rect(fuselageHitbox.x - padding, fuselageHitbox.y + fuselageHitbox.height - padding,
                    2f * padding, 2f * padding);

            shape.rect(fuselageHitbox.x + fuselageHitbox.width - padding,
                    fuselageHitbox.y + fuselageHitbox.height - padding,
                    2f * padding, 2f * padding);

            // highlight selection
            shape.setColor(Palette.MUTED_GREEN_LIGHT_HIGHLIGHT);
            shape.rect(fuselageHitbox.x, fuselageHitbox.y, fuselageHitbox.width, fuselageHitbox.height);

        } else {
            // draw orange highlights in corners
            shape.rect(upgradeHitbox.x - padding, upgradeHitbox.y - padding,
                    2f * padding, 2f * padding);

            shape.rect(upgradeHitbox.x + upgradeHitbox.width - padding, upgradeHitbox.y - padding,
                    2f * padding, 2f * padding);

            shape.rect(upgradeHitbox.x - padding, upgradeHitbox.y + upgradeHitbox.height - padding,
                    2f * padding, 2f * padding);

            shape.rect(upgradeHitbox.x + upgradeHitbox.width - padding,
                    upgradeHitbox.y + upgradeHitbox.height - padding,
                    2f * padding, 2f * padding);

            // highlight selection

            shape.setColor(Palette.MUTED_GREEN_LIGHT_HIGHLIGHT);
            shape.rect(upgradeHitbox.x, upgradeHitbox.y, upgradeHitbox.width, upgradeHitbox.height);
        }

        if (!fuselageUpgradeEligible) {
            shape.setColor(Palette.MUTED_RED_HIGHLIGHT);
            shape.rect(fuselageHitbox.x, fuselageHitbox.y, fuselageHitbox.width, fuselageHitbox.height);
        }

        if (!upgradeUpgradeEligible) {
            shape.setColor(Palette.MUTED_RED_HIGHLIGHT);
            shape.rect(upgradeHitbox.x, upgradeHitbox.y, upgradeHitbox.width, upgradeHitbox.height);
        }

        shape.setColor(oldColor);
    }

    private void renderSprites(SpriteBatch batch, float x, float y, float width, float height) {
        // Fuselage and Upgrade sprites logic
        float fuselageNextStageOffsetX = 2f * padding + spriteRadius;
        float fuselageNextStageOffsetY = 2f * padding + spriteRadius;

        float fuselageOffsetX = fuselageNextStageOffsetX;
        float fuselageOffsetY = height - fuselageNextStageOffsetY;

        Sprite fuselageSprite = upgradeSprites.get(UpgradeType.FUSELAGE).get(fuselage.getStage());
        fuselageSprite.setCenterX(x + fuselageOffsetX);
        fuselageSprite.setCenterY(y + fuselageOffsetY);
        fuselageSprite.draw(batch);

        Sprite fuselageNextStageSprite = upgradeSprites.get(UpgradeType.FUSELAGE).get(fuselage.getStage().nextStage());
        fuselageNextStageSprite.setCenterX(x + fuselageNextStageOffsetX);
        fuselageNextStageSprite.setCenterY(y + fuselageNextStageOffsetY);
        fuselageNextStageSprite.draw(batch);

        if (fuselage.hasUpgrade()) {

            float upgradeNextStageOffsetX = fuselageNextStageOffsetX + spriteRadius + padding + spriteRadius;
            float upgradeNextStageOffsetY = fuselageNextStageOffsetY;

            float upgradeOffsetX = upgradeNextStageOffsetX;
            float upgradeOffsetY = fuselageOffsetY;

            Sprite upgradeSprite = upgradeSprites.get(fuselage.getUpgrade().getType())
                    .get(fuselage.getUpgrade().getStage());
            upgradeSprite.setCenterX(x + upgradeOffsetX);
            upgradeSprite.setCenterY(y + upgradeOffsetY);
            upgradeSprite.draw(batch);

            Sprite upgradeNextStageSprite = upgradeSprites.get(fuselage.getUpgrade().getType())
                    .get(fuselage.getUpgrade().getStage().nextStage());
            upgradeNextStageSprite.setCenterX(x + upgradeNextStageOffsetX);
            upgradeNextStageSprite.setCenterY(y + upgradeNextStageOffsetY);
            upgradeNextStageSprite.draw(batch);
        }

        // Render price text
        renderPriceText(batch, x, y, width, height);

        // Render stat-bar text
        float barX = x + fuselageNextStageOffsetX + 3f * spriteRadius + 2f * padding;
        renderBarText(batch, barX, y, width, height);
    }

    private void renderBars(ShapeRenderer shape, float barX, float y, float width, float height) {
        float barY = y + height - 2f * padding;
        for (Stat stat : Stat.values()) {
            DiffBar statBar = bars.get(stat);
            GlyphLayout glyphLayout = glyphLayouts.get(stat);

            barY -= glyphLayout.height;
            barY -= padding + diffBarScales.y();

            statBar.setPosition(barX, barY);
            statBar.draw(shape);

            barY -= barPaddingTextScale * glyphLayout.height;
        }
    }

    private void renderPriceText(SpriteBatch batch, float x, float y, float width, float height) {
        // Draw fuselage price
        if (fuselageUpgradeEligible) {
            font.setColor(Palette.WHITE);
        } else {
            font.setColor(Palette.MUTED_RED_LIGHT);
        }
        priceGlyphLayout.setText(font, Integer.toString(fuselagePrice));

        float fuselagePriceX = x + 2f * padding + spriteRadius - 0.5f * priceGlyphLayout.width;
        float priceY = y + height - 2f * padding - 3f * spriteRadius;
        font.draw(batch, priceGlyphLayout, fuselagePriceX, priceY);

        // Draw upgrade price
        if (upgradeUpgradeEligible) {
            font.setColor(Palette.WHITE);
        } else {
            font.setColor(Palette.MUTED_RED_LIGHT);
        }
        priceGlyphLayout.setText(font, Integer.toString(upgradePrice));

        float upgradePriceX = fuselagePriceX + 2f * spriteRadius + padding;
        font.draw(batch, priceGlyphLayout, upgradePriceX, priceY);
    }

    private void renderBarText(SpriteBatch batch, float barX, float y, float width, float height) {
        float barY = y + height - 2f * padding;
        for (Stat stat : Stat.values()) {
            GlyphLayout glyphLayout = glyphLayouts.get(stat);

            font.draw(batch, glyphLayout,
                    barX, barY);

            barY -= glyphLayout.height;
            barY -= padding + diffBarScales.y();
            barY -= barPaddingTextScale * glyphLayout.height;
        }
    }

    private float getWidth() {
        float totalPadding = 6f * padding; // 2+2 for outline and inner borders, 1 between fuselage and upgrade,
                                           // 1 between upgrade and bars
        float spriteWidth = 2f * 2f * spriteRadius; // 2 radii for each Sprite (fuselage + upgrade)

        return totalPadding + spriteWidth + diffBarScales.x();
    }

    private float getHeight() {
        float totalPadding = 4f * padding; // 2+2 for outline and inner borders
        // height of bars + height of text + a text offset + additional padding
        float barHeights = 0f;
        for (Stat stat : Stat.values()) {
            barHeights += diffBarScales.y();
            barHeights += (1f + barPaddingTextScale) * glyphLayouts.get(stat).height;
            barHeights += padding;
        }

        float fuselageHeights = 3f * (2f * spriteRadius); // 2 for each sprite +1 for extra space
        float upgradeHeights = 3f * (2f * spriteRadius);

        // space for bars may be more or less than the space for sprites,
        // so we use the maximum of the two
        return totalPadding + Math.max(barHeights, Math.max(fuselageHeights, upgradeHeights));
    }

    public boolean clickedOnUpgradeStageDisplay(float x, float y) {
        if (!visible) {
            return false;
        }

        return x >= position.x() && x <= position.x() + displayDimensions.x()
                && y >= position.y() && y <= position.y() + displayDimensions.y();
    }

    public ShipUpgrade getClickedUpgrade(float x, float y, boolean onlyReturnIfAlreadySelected) {
        if (!visible) {
            return null;
        }

        if (fuselageHitbox.contains(x, y)) {
            if (onlyReturnIfAlreadySelected && !fuselageMode) {
                setFuselageMode(true);
                return null;
            }
            return fuselage;

        } else if (upgradeHitbox.contains(x, y)) {
            if (onlyReturnIfAlreadySelected && fuselageMode) {
                setFuselageMode(false);
                return null;
            }
            return fuselage.getUpgrade();

        } else {
            return null;
        }
    }

    // TODO: Use these in render to communicate eligibility to the player.
    public void setUpgradeEligibility(boolean fuselageUpgradeEligible, boolean upgradeUpgradeEligible) {
        this.fuselageUpgradeEligible = fuselageUpgradeEligible;
        this.upgradeUpgradeEligible = upgradeUpgradeEligible;
    }
}
