package model.SpaceCharacters.Ships;

import java.util.List;

import grid.CellPosition;
import grid.GridCell;
import model.Globals.DamageDealer;
import model.Globals.Damageable;
import model.Globals.Repairable;
import model.ShipComponents.UpgradeType;
import model.ShipComponents.Components.Turret;
import model.ShipComponents.Components.stats.Stat;
import model.ShipComponents.ShipStructure;
import model.SpaceCharacters.CharacterType;
import model.SpaceCharacters.SpaceBody;
import model.ViewableSpaceShip;
import model.constants.PhysicsParameters;
import model.utils.FloatPair;
import model.utils.SpaceCalculator;
import view.Palette;
import view.bars.ShipHealthBar;

import java.util.UUID;

public abstract class SpaceShip extends SpaceBody implements DamageDealer, Damageable, Repairable, ViewableSpaceShip {

    private final String id;
    private int damageTaken = 0;

    private ShipHealthBar healthBar;

    protected final ShipStructure shipStructure;

    // health regeneration logic
    private float healthRegeneration = 0f;

    public SpaceShip(ShipStructure shipStructure, String name, String description,
            CharacterType characterType, float x, float y, float angle) {

        super(name, description, characterType, x, y, angle, 0f);

        this.shipStructure = shipStructure;

        this.shipStructure.normalize();
        radius = this.shipStructure.getRadius();

        id = UUID.randomUUID().toString();

        makeHealthBar();
    }

    private void makeHealthBar() {
        healthBar = new ShipHealthBar(this, new FloatPair(0, 0.2f));
        healthBar.setScale(0.9f, 0.13f);
        if (isPlayerShip()) {
            healthBar.setBarColor(Palette.MUTED_GREEN_LIGHT);
            healthBar.setBackgroundColor(Palette.MUTED_GREEN_DARK);
        } else {
            healthBar.setBarColor(Palette.MUTED_RED_LIGHT);
            healthBar.setBackgroundColor(Palette.MUTED_RED_DARK);
        }
        healthBar.setDrawOutline(true);
        healthBar.setNumNotches(4);
        healthBar.setHalfNotch(true);
    }

    public ShipHealthBar getHealthBar() {
        return healthBar;
    }

    public ShipStructure getShipStructure() {
        return shipStructure;
    }

    public boolean isPlayerShip() {
        return false;
    }

    public List<CellPosition> getUpgradeTypePositions(UpgradeType type) {
        return shipStructure.getUpgradeTypePositions(type);
    }

    public List<GridCell<Turret>> getTurretGridCells() {
        return shipStructure.getTurretGridCells();
    }

    public void setToShoot(boolean setToShoot) {
        for (Turret turret : shipStructure.getTurrets()) {
            turret.setToShoot(setToShoot);
        }
    }

    @Override
    public void update(float deltaTime) {
        updateTurrets(deltaTime);
        updateHealth(deltaTime);
    }

    private void updateTurrets(float deltaTime) {
        for (Turret turret : shipStructure.getTurrets()) {
            turret.update(deltaTime);
        }
    }

    private void updateHealth(float deltaTime) {
        healthRegeneration += deltaTime * shipStructure.getCombinedStatModifier().getModifiers()
                .get(Stat.HEALTH_REGENERATION_RATE).floatValue();

        int healthToRestore = (int) Math.floor(healthRegeneration);
        healthRegeneration -= healthToRestore;
        repair(healthToRestore);
    }

    @Override
    public FloatPair getAbsoluteCenter() {
        return new FloatPair(getX() + shipStructure.getWidth() / 2f - PhysicsParameters.fuselageRadius,
                getY() + shipStructure.getHeight() / 2f - PhysicsParameters.fuselageRadius);
    }

    @Override
    public FloatPair getRelativeCenter() {
        return new FloatPair(shipStructure.getWidth() / 2f, shipStructure.getHeight() / 2f);
    }

    @Override
    public FloatPair getAbsoluteCenterOfMass() {
        return new FloatPair(getX() + shipStructure.getCenterOfMass().x(),
                getY() + shipStructure.getCenterOfMass().y());
    }

    @Override
    public FloatPair getRelativeCenterOfMass() {
        return shipStructure.getCenterOfMass();
    }

    @Override
    public int getMaxHitPoints() {
        return shipStructure.getCombinedStatModifier().getModifiers().get(Stat.HEALTH_VALUE).intValue();
    }

    @Override
    public void dealDamage(Damageable target, int damage) {
        target.takeDamage(damage);
    }

    @Override
    public void takeDamage(int hitPoints) {
        if (hitPoints < 0) {
            throw new IllegalArgumentException("Damage can't be negative");
        }
        damageTaken = Math.min(getMaxHitPoints(), damageTaken + hitPoints);
    }

    @Override
    public int getHitPoints() {
        return getMaxHitPoints() - damageTaken;
    }

    @Override
    public boolean isDestroyed() {
        return damageTaken >= getMaxHitPoints();
    }

    @Override
    public void repair(int hitPoints) {
        if (isDestroyed()) {
            return;
        }
        damageTaken = Math.max(0, damageTaken - hitPoints);
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public int getResourceValue() {
        return getShipStructure().getResourceValue();
    }

    @Override
    public int getDamage() {
        return getHitPoints();
    }

    @Override
    public float getMass() {
        return shipStructure.getMass();
    }

    @Override
    public float getRadius() {
        return shipStructure.getRadius();
    }

    public int getNumFuselage() {
        return shipStructure.getNumFuselage();
    }

    /**
     * Get the radius of the ship for proximity detection.
     * This radius takes into consideration the fact that
     * the ship rotates about its center of mass, not the
     * center of its <code>ShipStructure</code>.
     * 
     * <p>
     * This provides a radius for which it is safe to define
     * the minimal proximity / hit-detection radius of a ship.
     * It defines the radius of the circle centered at the ship's
     * center of mass which is guaranteed to contain the ship
     * no matter what its angle of rotation about the center of mass is.
     * 
     * @return a proximity radius of the circle centred at the ship's center of mass
     */
    public float getProximityRadius() {
        return SpaceCalculator.distance(getRelativeCenter(), getRelativeCenterOfMass()) + getRadius()
                + PhysicsParameters.fuselageRadius / 2f;
    }
}
