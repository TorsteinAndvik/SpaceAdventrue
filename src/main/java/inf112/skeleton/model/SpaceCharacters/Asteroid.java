package inf112.skeleton.model.SpaceCharacters;

import inf112.skeleton.model.Globals.Damageble;

public class Asteroid extends SpaceBody implements Damageble {

  public Asteroid(String name, String description, int x, int y, int healthPoints, int mass, int speed, float angle) {
    super(name, description, x, y, healthPoints, mass, speed, angle);
  }

  @Override
  public void takeDamage(Integer v) {
  }

  @Override
  public void dealDamage(Damageble d, Integer v) {
    int damage = getHealthPoints();
    d.takeDamage(damage);


  }
  //TODO: Find out what is unique about an asteroid.

}
