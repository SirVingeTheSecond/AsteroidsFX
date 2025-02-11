package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Interface for bullet creation and management.
 */
public interface BulletSPI {
    /**
     * Creates a new bullet entity.
     *
     * Pre-conditions:
     * - Entity e (shooter) must not be null
     * - gameData must not be null
     * - Shooter must be in a valid state to create bullets
     *
     * Post-conditions:
     * - Created bullet must have valid position relative to shooter
     * - Bullet must have proper velocity and direction
     * - Bullet must be properly initialized with all required properties
     * - Bullet must be ready to be added to the world
     *
     * @param e The entity creating the bullet (shooter)
     * @param gameData Contains game state and configuration
     * @return A new bullet entity
     * @throws NullPointerException if e or gameData is null
     * @throws IllegalStateException if bullet cannot be created
     */
    Entity createBullet(Entity e, GameData gameData);
}
