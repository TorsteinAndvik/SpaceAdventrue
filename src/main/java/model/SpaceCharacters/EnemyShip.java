package model.SpaceCharacters;

import model.ShipComponents.Components.ShipStructure;
import model.ai.Brain;
import model.ai.NullBrain;

public class EnemyShip extends SpaceShip {

    private Brain brain = new NullBrain();

    public EnemyShip(ShipStructure shipStructure, String name, String description,
            float x, float y, int hitPoints, float angle) {
        super(shipStructure, name, description, CharacterType.ENEMY_SHIP, x, y, hitPoints, angle);
    }

    @Override
    public void update(float delta) {
        brain.update(delta);
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }
}
