package inf112.skeleton.model.SpaceCharacters;


public interface SpaceBodyView {

  /**
   * @return the x-coordinate of a SpaceBody object.
   */
  int getX();


  /**
   * @return the y-coordinate of a SpaceBody object.
   */
  int getY();


  /**
   * Get the mass of a SpaceBody object.
   *
   * @return the mass of the SpaceBody.
   */
  int getMass();

  /**
   * Get the speed of a SpaceBody object.
   *
   * @return the speed of the SpaceBody.
   */
  int getSpeed();

  /**
   * The rotation of a SpaceBody object.
   *
   * @return the rotation of the SpaceBody.
   */
  float getRotationAngle();

  /**
   * The radius of a SpaceBody object
   *
   * @return the radius of the SpaceBody
   */
  int getRadius();
  
}
