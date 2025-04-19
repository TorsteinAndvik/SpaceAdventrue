package view.bars;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

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
    private ShipUpgrade upgrade;
    private Map<UpgradeType, Map<UpgradeStage, Sprite>> upgradeSprites;
    private FloatPair position;
    private FloatPair diffBarScales;
    private float padding = 0.05f;
    private float barPaddingTextScale = 0.5f;
    private Map<Stat, DiffBar> bars;
    private Map<Stat, GlyphLayout> glyphLayouts;
    private float spriteRadius;

    private Color outlineColor = Palette.BLACK;
    private Color displayColor = Palette.MUTED_GREEN_LIGHT;
    private BitmapFont font;

    public UpgradeStageDisplay(StatModifier max, Map<UpgradeType, Map<UpgradeStage, Sprite>> upgradeSprites,
            BitmapFont font, float spriteRadius) {

        this.upgradeSprites = upgradeSprites;
        this.position = new FloatPair(0, 0);
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
    }

    public void setPosition(FloatPair position) {
        this.position = position;
    }

    public void setComponents(Fuselage fuselage, ShipUpgrade upgrade) {
        this.fuselage = fuselage;
        this.upgrade = upgrade;

        setStatDiffs(fuselage);
    }

    public void setCurrentStats(StatModifier current) {
        for (Stat stat : Stat.values()) {
            bars.get(stat).setCurrentValue(current.getModifiers().get(stat).floatValue());
        }
    }

    private void setStatDiffs(ShipUpgrade upgrade) {
        StatModifier diff = (upgrade.getStage().isUpgradeable() ? upgrade.getUpgradeStatModifier() : new StatModifier())
                .addModifier(new Fuselage().getStatModifier());
        for (Stat stat : Stat.values()) {
            bars.get(stat).updateDiff(diff.getModifiers().get(stat).floatValue(), stat.positiveIsBeneficial);
        }
    }

    /**
     * Renders the upgrade stage display.
     * <p>
     * Assumes shape and batch do not have <code>begin()</code>
     * called before calling this method.
     * <p>
     * shape will have end() called on it. batch will NOT have end() called on it,
     * to allow for additional rendering without an extra buffer dump.
     * 
     * @param batch the <code>SpriteBatch</code> to render Sprites with
     * @param shape the <code>ShapeRenderer</code> to render the display box with
     */
    public void render(SpriteBatch batch, ShapeRenderer shape) {

        float x = position.x();
        float y = position.y();
        float width = getWidth();
        float height = getHeight();

        renderShapes(shape, x, y, width, height);
        renderSprites(batch, x, y, width, height);
    }

    private void renderShapes(ShapeRenderer shape, float x, float y, float width, float height) {
        shape.begin(ShapeType.Filled);
        Color oldColor = shape.getColor();

        shape.setColor(outlineColor);
        shape.rect(x, y, width, height);

        shape.setColor(displayColor);
        shape.rect(x + padding, y + padding, width - 2f * padding, height - 2f * padding);

        float barX = x + 4f * (padding + spriteRadius);
        renderBars(shape, barX, y, width, height);

        shape.setColor(oldColor);
        shape.end();
    }

    private void renderSprites(SpriteBatch batch, float x, float y, float width, float height) {
        batch.begin();

        // Fuselage and Upgrade sprites logic
        float fuselageNextStageOffsetX = 2f * padding + spriteRadius;
        float fuselageNextStageOffsetY = 2f * padding + spriteRadius;

        float fuselageOffsetX = fuselageNextStageOffsetX;
        float fuselageOffsetY = height - fuselageNextStageOffsetY;

        float upgradeNextStageOffsetX = fuselageNextStageOffsetX + spriteRadius + padding + spriteRadius;
        float upgradeNextStageOffsetY = fuselageNextStageOffsetY;

        float upgradeOffsetX = upgradeNextStageOffsetX;
        float upgradeOffsetY = fuselageOffsetY;

        Sprite fuselageSprite = upgradeSprites.get(UpgradeType.FUSELAGE).get(fuselage.getStage());
        fuselageSprite.setCenterX(x + fuselageOffsetX);
        fuselageSprite.setCenterY(y + fuselageOffsetY);
        fuselageSprite.draw(batch);

        Sprite fuselageNextStageSprite = upgradeSprites.get(UpgradeType.FUSELAGE).get(fuselage.getStage().nextStage());
        fuselageNextStageSprite.setCenterX(x + fuselageNextStageOffsetX);
        fuselageNextStageSprite.setCenterY(y + fuselageNextStageOffsetY);
        fuselageNextStageSprite.draw(batch);

        Sprite upgradeSprite = upgradeSprites.get(upgrade.getType()).get(upgrade.getStage());
        upgradeSprite.setCenterX(x + upgradeOffsetX);
        upgradeSprite.setCenterY(y + upgradeOffsetY);
        upgradeSprite.draw(batch);

        Sprite upgradeNextStageSprite = upgradeSprites.get(upgrade.getType()).get(upgrade.getStage().nextStage());
        upgradeNextStageSprite.setCenterX(x + upgradeNextStageOffsetX);
        upgradeNextStageSprite.setCenterY(y + upgradeNextStageOffsetY);
        upgradeNextStageSprite.draw(batch);

        // Render stat-bar text
        float barX = x + upgradeOffsetX + spriteRadius + padding;
        renderText(batch, barX, y, width, height);
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

    private void renderText(SpriteBatch batch, float barX, float y, float width, float height) {
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

    // TODO: Make this a calculate and store once? Is a resize ever necessary?
    private float getWidth() {
        float totalPadding = 6f * padding;
        float spriteWidth = 4f * spriteRadius;

        return totalPadding + spriteWidth + diffBarScales.x();
    }

    // TODO: Make this a calculate and store once? Is a resize ever necessary?
    private float getHeight() {
        float totalPadding = 4f * padding;
        float barHeights = 0f;
        for (Stat stat : Stat.values()) {
            barHeights += diffBarScales.y();
            barHeights += (1f + barPaddingTextScale) * glyphLayouts.get(stat).height;
            barHeights += padding;
        }

        float fuselageHeights = 3f * (2f * spriteRadius);
        float upgradeHeights = 3f * (2f * spriteRadius);

        return totalPadding + Math.max(barHeights, Math.max(fuselageHeights, upgradeHeights));
    }
}
