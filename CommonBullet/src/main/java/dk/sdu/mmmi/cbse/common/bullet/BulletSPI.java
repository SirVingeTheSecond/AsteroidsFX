package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Service Provider Interface for bullet creation.
 * Allows bullet creation to be implemented by different modules.
 */
public interface BulletSPI {
    /**
     * Create a new bullet entity
     *
     * @param shooter The entity that is shooting
     * @param gameData Current game state
     * @return New bullet entity
     */
    Entity createBullet(Entity shooter, GameData gameData);
}