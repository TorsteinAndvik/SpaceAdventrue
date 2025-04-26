package view.bars;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.ShipComponents.ShipFactory;
import model.SpaceCharacters.Ships.Player;
import model.SpaceCharacters.Ships.SpaceShip;
import model.utils.FloatPair;

public class ShipHealthBarTest {
    private SpaceShip ship;
    private FloatPair offset;

    private ShipHealthBar shipHealthBar;

    @BeforeEach
    public void setup() {
        ship = new Player(ShipFactory.simpleShip(), "name", "desc", 1f, 0);
        offset = new FloatPair(-1f, 0);
        shipHealthBar = new ShipHealthBar(ship, offset);
    }

    @Test
    public void setCenterTest() {
        shipHealthBar.setCenter(new FloatPair(1f, 2f));
        float x = 1f + offset.x() - shipHealthBar.dimensions.width / 2f;
        float y = 2f + offset.y() - shipHealthBar.dimensions.height / 2f;
        assertEquals(new FloatPair(x, y), shipHealthBar.getPosition());
    }
}
