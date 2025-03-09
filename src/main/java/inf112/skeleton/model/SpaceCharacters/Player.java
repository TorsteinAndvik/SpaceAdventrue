package inf112.skeleton.model.SpaceCharacters;

public class Player extends SpaceShip {

  public Player(String name, String description, int hitPoints, float x, float y, float radius) {
    super(name, description, CharacterType.PLAYER, x, y, hitPoints, 0, radius);
  }
}
