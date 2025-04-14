package model.ai;

import model.SpaceCharacters.Ships.EnemyShip;
import model.SpaceCharacters.Ships.Player;

public abstract class Brain {

    protected final EnemyShip ship;
    protected final Player player;

    public Brain(EnemyShip ship, Player player) {
        this.ship = ship;
        this.player = player;
    }

    public abstract void update(float delta);
}
