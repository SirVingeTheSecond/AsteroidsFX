package dk.sdu.mmmi.cbse.common.collision;

/**
 * Enumeration of collision groups.
 * Uses bitmasks for efficient group membership checks.
 */
public enum CollisionGroup {
    FRIENDLY(1),      // 0001 - Player and friendly units
    HOSTILE(2),       // 0010 - Enemies and their projectiles
    DESTRUCTIBLE(4),  // 0100 - Can be destroyed by collisions
    SOLID(8),         // 1000 - Solid objects that block movement
    POWERUP(16);      // 10000 - Collectible items

    private final int mask;

    /**
     * Create a collision group with a specific bitmask
     * @param mask The bitmask for this group
     */
    CollisionGroup(int mask) {
        this.mask = mask;
    }

    /**
     * Get the bitmask for this group
     * @return The bitmask
     */
    public int getMask() {
        return mask;
    }
}