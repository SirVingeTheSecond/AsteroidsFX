package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Event triggered when an entity wants to shoot.
 * This event should be handled by a dedicated ShootSystem.
 */
public class ShootEvent extends BaseGameEvent {
    private float direction; // Optional direction override
    private boolean hasDirectionOverride;

    /**
     * Create a shoot event with the entity's current direction
     * @param source Entity that is shooting
     */
    public ShootEvent(Entity source) {
        super(source);
        this.hasDirectionOverride = false;
    }

    /**
     * Create a shoot event with a specific direction
     * @param source Entity that is shooting
     * @param direction Direction to shoot in (degrees)
     */
    public ShootEvent(Entity source, float direction) {
        super(source);
        this.direction = direction;
        this.hasDirectionOverride = true;
    }

    /**
     * Check if a specific direction was provided
     * @return true if direction is overridden, false to use entity's direction
     */
    public boolean hasDirectionOverride() {
        return hasDirectionOverride;
    }

    /**
     * Get the shooting direction
     * @return Direction in degrees
     */
    public float getDirection() {
        return direction;
    }
}