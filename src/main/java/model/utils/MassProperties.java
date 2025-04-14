package model.utils;


/**
 * Represents the mass properties of an object.
 *
 * @param mass         The total mass of the object.
 * @param centerOfMass The center of mass represented as a {@link FloatPair}.
 */
public record MassProperties(float mass, FloatPair centerOfMass) { }
