package model.ai;

import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;

public class NullBrain extends Brain {

    public NullBrain() {
        super(null, null);
    }

    public NullBrain(EnemyShip ship, Player player) {
        super(null, null);
    }

    @Override
    public void update(float delta) {
    }

}
