package model.ShipComponents.Components;

import model.utils.FloatPair;

/**
 * Represents the mass properties of a ship, including total mass and center of mass.
 *
 * @param mass         The total mass of the ship.
 * @param centerOfMass The center of mass represented as a {@link FloatPair}.
 */
public record MassProperties(float mass, FloatPair centerOfMass) { }
