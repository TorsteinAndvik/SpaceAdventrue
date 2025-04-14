package model.Globals;

public interface Collectable extends Collidable {

    /**
     * Set the value of the {@code collectable}
     *
     * @param value of the {@code collectable}
     */
    void setValue(int value);

    /**
     * Get the value of the {@code collectable}
     */

    int getValue();

}