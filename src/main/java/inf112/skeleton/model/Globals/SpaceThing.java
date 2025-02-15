package inf112.skeleton.model.Globals;

/**
 * A SpaceThing is the basis for all objects in the game.
 */
public interface SpaceThing {

  /**
   * Set the name of the SpaceThing.
   *
   * @param name of SpaceThing.
   */
  void setName(String name);

  /**
   * Get name of SpaceThing.
   *
   * @return name of SpaceThing.
   */
  String getName();

  /**
   * Set description of SpaceThing.
   *
   * @param description of SpaceThing.
   */
  void setDescription(String description);

  /**
   * Get description of SpaceThing.
   *
   * @return description of SpaceThing.
   */
  String getDescription();
}
