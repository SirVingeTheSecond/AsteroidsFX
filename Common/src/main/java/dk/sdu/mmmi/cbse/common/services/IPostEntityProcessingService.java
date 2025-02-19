package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Post-processing interface for operations after main game logic.
 * Handles collision detection, cleanup, etc.
 */
public interface IPostEntityProcessingService {
    /**
     * Perform post-processing operations.
     *
     * @pre gameData != null
     * @pre world != null
     * @pre All entity processing complete
     * @post Post-processing effects applied
     * @post World state remains valid
     *
     * @param gameData Current game state
     * @param world Game world to process
     */
    void process(GameData gameData, World world);
}

