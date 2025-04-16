package model.SpaceCharacters.Ships;

import model.ShipComponents.ShipStructure;
import model.SpaceCharacters.CharacterType;
import model.ai.Brain;
import model.ai.NullBrain;

public class EnemyShip extends SpaceShip {

    private Brain brain = new NullBrain();

    public EnemyShip(ShipStructure shipStructure, String name, String description,
            float x, float y, float angle) {
        super(shipStructure, name, description, CharacterType.ENEMY_SHIP, x, y, angle);
    }

    @Override
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
        updateHealth(deltaTime);
        brain.update(deltaTime);
    }

    public void setBrain(Brain brain) {
        this.brain = brain;
    }
}
