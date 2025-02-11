package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for processing game entities.
 * Implementations should handle the core game logic for specific entity types.
 */
public interface IEntityProcessingService {
    /**
     * Processes game entities in the world.
     *
     * Pre-conditions:
     * - gameData must not be null
     * - world must not be null
     * - world.getEntities() must return a valid collection (can be empty)
     * - All entities in world must have valid positions and states
     *
     * Post-conditions:
     * - All processed entities must remain within game boundaries
     * - Entity states must be updated according to game rules
     * - No entities should be in an invalid state
     * - World state must remain consistent
     *
     * @param gameData Contains game state and configuration
     * @param world Contains all game entities
     * @throws NullPointerException if gameData or world is null
     * @throws IllegalStateException if entity processing fails
     */
    void process(GameData gameData, World world);
}
