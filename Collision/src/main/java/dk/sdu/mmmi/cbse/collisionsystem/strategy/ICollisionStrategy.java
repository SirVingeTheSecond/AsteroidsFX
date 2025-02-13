package dk.sdu.mmmi.cbse.collisionsystem.strategy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Strategy interface for handling specific collision types.
 * Single responsibility: Define contract for collision response.
 */
public interface ICollisionStrategy {
    boolean handleCollision(Entity entity1, Entity entity2, World world);
    boolean canHandle(Entity entity1, Entity entity2);
}
