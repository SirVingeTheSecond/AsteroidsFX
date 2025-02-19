package dk.sdu.mmmi.cbse.common.collision;

public enum CollisionGroup {
    FRIENDLY(1),      // 0001 - Player and friendly units
    HOSTILE(2),       // 0010 - Enemies and their projectiles
    DESTRUCTIBLE(4),  // 0100 - Can be destroyed by collisions
    SOLID(8),         // 1000 - Solid objects that block movement
    POWERUP(16);      // 10000 - Collectible items

    private final int mask;

    CollisionGroup(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }
}
