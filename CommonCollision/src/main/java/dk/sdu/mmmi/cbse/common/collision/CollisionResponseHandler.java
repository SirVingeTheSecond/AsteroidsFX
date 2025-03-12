package dk.sdu.mmmi.cbse.common.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Functional interface for collision response handlers
 */
@FunctionalInterface
public interface CollisionResponseHandler {
    /**
     * Handle a collision between two entities
     * @return true if collision was handled, false to continue checking other handlers
     */
    boolean onCollision(Entity self, Entity other, World world);
}
