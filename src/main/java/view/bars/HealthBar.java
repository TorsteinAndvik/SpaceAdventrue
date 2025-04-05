package view.bars;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import model.Globals.Damageable;
import model.SpaceCharacters.ViewableSpaceBody;
import model.utils.FloatPair;

public class HealthBar<T extends Damageable & ViewableSpaceBody> extends PercentageBar {
    T subject;
    FloatPair offset;

    public HealthBar(T subject, FloatPair offset) {
        super(subject.getMaxHitPoints(), subject.getHitPoints());
        this.subject = subject;
        this.offset = offset;
    }

    @Override
    public void setPosition(FloatPair pos) {
        this.setPosition(pos.x() + offset.x(), pos.y() + offset.y());
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x + offset.x(), y + offset.y());
    }

    @Override
    public void setCenter(FloatPair pos) {
        this.setCenter(pos.x() + offset.x(), pos.y() + offset.y());
    }

    @Override
    public void setCenter(float x, float y) {
        super.setCenter(x + offset.x(), y + offset.y());
    }

    @Override
    public void draw(ShapeRenderer renderer) {
        setMaxValue(subject.getMaxHitPoints());
        setCurrentValue(subject.getHitPoints());
        this.setCenter(subject.getX(), subject.getY());
        super.draw(renderer);
    }
}
