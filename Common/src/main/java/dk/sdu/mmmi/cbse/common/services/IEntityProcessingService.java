package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Core game processing interface for entity behavior.
 * Contract ensures proper entity state management.
 */
public interface IEntityProcessingService {
    /**
     * Process entity behavior for a game update.
     *
     * @pre gameData != null
     * @pre world != null
     * @pre world.getEntities() returns valid collection
     * @post All entities remain in valid game bounds
     * @post Entity states are updated according to game rules
     *
     * @param gameData Current game state common
     * @param world Game world containing entities
     */
    void process(GameData gameData, World world);
}