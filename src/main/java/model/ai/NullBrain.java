package model.ai;

import model.SpaceCharacters.EnemyShip;
import model.SpaceCharacters.Player;

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
