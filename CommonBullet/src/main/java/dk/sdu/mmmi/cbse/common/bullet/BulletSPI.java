package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Interface for bullet creation.
 */
public interface BulletSPI {
    /**
     * Creates a new bullet entity.
     *
     * @param e The entity creating the bullet (shooter)
     * @param gameData Contains game state and configuration
     * @return A new bullet entity
     */
    Entity createBullet(Entity e, GameData gameData);
}