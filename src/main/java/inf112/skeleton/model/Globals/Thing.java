package inf112.skeleton.model.Globals;


/**
 * Everything in game is also a Thing. A thing maintains just a name and a description of an
 * object.
 */
public class Thing implements SpaceThing {

  private String name;
  private String description;

  /**
   * Creates a thing.
   *
   * @param name        the name of the thing.
   * @param description the description of the thing.
   */
  public Thing(String name, String description) {
    this.name = name;
    this.description = description;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
