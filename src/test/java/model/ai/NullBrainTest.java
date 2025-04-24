package model.ai;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import model.ShipComponents.ShipStructure;
import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;

public class NullBrainTest {

    @Test
    public void constructorTest() {
        NullBrain nullBrain = new NullBrain();
        assertNull(nullBrain.ship);
        assertNull(nullBrain.player);

        NullBrain nullBrain2 = new NullBrain(
                new EnemyShip(new ShipStructure(1, 1), "name", "desc", 0, 0, 0),
                new Player(new ShipStructure(1, 1), "name", "desc", 0, 0));

        assertNull(nullBrain2.ship);
        assertNull(nullBrain2.player);

        nullBrain.update(5f);
        nullBrain2.update(10f);
        // update does nothing
        assertNull(nullBrain.ship);
        assertNull(nullBrain.player);
        assertNull(nullBrain2.ship);
        assertNull(nullBrain2.player);
    }
}
