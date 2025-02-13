package dk.sdu.mmmi.cbse.collisionsystem.strategy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Strategy interface for handling specific collision types.
 */
public interface ICollisionStrategy {
    /**
     * Check if this strategy can handle collision between given entities
     */
    boolean canHandle(Entity entity1, Entity entity2);

    /**
     * Handle collision between two entities
     * @return true if collision was handled, false otherwise
     */
    boolean handleCollision(Entity entity1, Entity entity2, World world);
}
