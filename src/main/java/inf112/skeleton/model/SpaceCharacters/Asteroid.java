package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.Thing;
import inf112.skeleton.model.utils.Rotation;

import java.util.function.ToDoubleBiFunction;

public class Asteroid extends SpaceBody {

  public Asteroid(String name, String description, int x, int y, int healthPoints, int mass, int speed, float angle) {
    super(name, description, x, y, healthPoints, mass, speed, angle);
  }
  //TODO: Find out what is unique about an asteroid.

}
