package model.SpaceCharacters;

import com.badlogic.gdx.math.Vector2;

public interface ViewableSpaceBody {

    /**
     * @return the x-coordinate of a SpaceBody object.
     */
    float getX();

    /**
     * @return the y-coordinate of a SpaceBody object.
     */
    float getY();

    /**
     * Get the mass of a SpaceBody object.
     *
     * @return the mass of the SpaceBody.
     */
    float getMass();

    Vector2 getVelocity();

    /**
     * Get the speed of a SpaceBody object.
     *
     * @return the speed of the SpaceBody.
     */
    float getSpeed();

    /**
     * Get the rotation of a SpaceBody object.
     *
     * @return the rotation of the SpaceBody.
     */
    float getRotationAngle();

    /**
     * Get the radius of a SpaceBody object
     *
     * @return the radius of the SpaceBody
     */
    float getRadius();

    /**
     * Get the CharacterType of a SpaceBody object
     *
     * @return the character type.
     */
    CharacterType getCharacterType();

    /**
     * The resources to loot from this {@code SpaceBody}
     *
     * @return the amount of resources to loot.
     */
    int getResourceValue();

}
