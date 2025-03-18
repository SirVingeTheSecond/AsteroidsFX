package dk.sdu.mmmi.cbse.common.collision;

/**
 * Enumeration of collision layers.
 * Used for sorting collisions by entity type.
 */
public enum CollisionLayer {
    PLAYER,     // Player entities
    ENEMY,      // Enemy entities
    PROJECTILE, // Bullets and other projectiles
    OBSTACLE;   // Asteroids and other obstacles
}