package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for systems that process entities.
 * Core part of the component-based architecture.
 */
public interface IEntityProcessingService {
    /**
     * Process entity behavior for a game update
     *
     * @param gameData Current game state data
     * @param world Game world containing entities
     */
    void process(GameData gameData, World world);
}