package view.bars;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import model.SpaceCharacters.Ships.SpaceShip;
import model.utils.FloatPair;

public class ShipHealthBar extends PercentageBar {

    SpaceShip ship;
    FloatPair offset;

    public ShipHealthBar(SpaceShip ship, FloatPair offset) {
        super(ship.getMaxHitPoints(), ship.getHitPoints());
        this.ship = ship;
        this.offset = offset;
    }

    @Override
    public void setCenter(FloatPair pos) {
        this.setCenter(pos.x(), pos.y());
    }

    @Override
    public void setCenter(float x, float y) {
        super.setCenter(x + offset.x(), y + offset.y());
    }

    @Override
    public void draw(ShapeRenderer renderer) {
        setMaxValue(ship.getMaxHitPoints());
        setCurrentValue(ship.getHitPoints());
        this.setCenter(ship.getAbsoluteCenterOfMass());
        super.draw(renderer);
    }
}
