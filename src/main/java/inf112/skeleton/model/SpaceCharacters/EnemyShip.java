package inf112.skeleton.model.SpaceCharacters;

public class EnemyShip extends SpaceShip {

    public EnemyShip(String name, String description, float x, float y, int hitPoints, float angle,
            int radius) {
        super(name, description, CharacterType.ENEMY_SHIP, x, y, hitPoints, angle, radius);
    }
}
