package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for post-processing of entities after main game logic.
 * Handles operations that should occur after all entity processing is complete.
 */
public interface IPostEntityProcessingService {
    /**
     * Performs post-processing operations on game entities.
     *
     * Pre-conditions:
     * - gameData must not be null
     * - world must not be null
     * - All entity processing must be complete
     * - World must be in a consistent state
     *
     * Post-conditions:
     * - Post-processing effects must be applied consistently
     * - World state must remain valid
     * - All entities must maintain valid states
     * - No side effects should occur outside of intended post-processing
     *
     * @param gameData Contains game state and configuration
     * @param world Contains all game entities
     * @throws NullPointerException if gameData or world is null
     * @throws IllegalStateException if post-processing fails
     */
    void process(GameData gameData, World world);
}

