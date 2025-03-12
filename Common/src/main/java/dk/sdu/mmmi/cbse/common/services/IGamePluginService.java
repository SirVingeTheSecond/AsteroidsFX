package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Plugin lifecycle management interface.
 * Ensures proper component initialization and cleanup.
 */
public interface IGamePluginService {
    /**
     * Initialize plugin and its resources.
     *
     * @pre gameData != null
     * @pre world != null
     * @pre Plugin not already started
     * @post All plugin resources initialized
     * @post Required entities added to world
     *
     * @param gameData Current game state
     * @param world Game world to populate
     */
    void start(GameData gameData, World world);

    /**
     * Clean up plugin resources.
     *
     * @pre gameData != null
     * @pre world != null
     * @pre Plugin in started state
     * @post All plugin resources released
     * @post Plugin entities removed from world
     *
     * @param gameData Current game state
     * @param world Game world to clean up
     */
    void stop(GameData gameData, World world);
}
