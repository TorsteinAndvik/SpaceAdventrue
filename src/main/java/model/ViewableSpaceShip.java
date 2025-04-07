package model;

import grid.CellPosition;
import java.util.List;
import model.Globals.Collidable;
import model.ShipComponents.UpgradeType;
import model.SpaceCharacters.ViewableSpaceBody;
import model.utils.FloatPair;

public interface ViewableSpaceShip extends ViewableSpaceBody, Collidable {

    boolean isPlayerShip();

    List<CellPosition> getUpgradeTypePositions(UpgradeType type);

    /**
     * @return absolute center point of the ship grid.
     *         Shifts the relative center by the ship's global X and Y coordinates.
     */
    FloatPair getAbsoluteCenter();

    /**
     * @return center point of the ship grid relative to its bottom left corner.
     */
    FloatPair getRelativeCenter();

    /**
     * @return absolute center of mass of the ship structure.
     *         Shifts the relative center of mass by the ship's global X and Y
     *         coordinates.
     */
    FloatPair getAbsoluteCenterOfMass();

    /**
     * @return center of mass of the ship structure relative to its bottom left
     *         corner.
     */
    FloatPair getRelativeCenterOfMass();

    /**
     * Get the unique ID for this {@code SpaceBody}
     *
     * @return The ID as a {@code String}
     */
    String getID();


    /**
     * Returns whether the entity is currently attempting to shoot.
     *
     * @return {@code true} if the entity is actively shooting; {@code false} otherwise
     */
    boolean isShooting();

    /**
     * Signals that the entity has performed a shooting action.
     */
    void hasShot();

    /**
     * Returns whether the entity is currently accelerating.
     *
     * @return {@code true} if the entity is accelerating; {@code false} otherwise
     */
    boolean isAccelerating();
}
