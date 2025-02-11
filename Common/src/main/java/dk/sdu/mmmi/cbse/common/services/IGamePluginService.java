package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for game plugins that can be dynamically loaded/unloaded.
 * Handles the lifecycle of game components.
 */
public interface IGamePluginService {
    /**
     * Starts the plugin and initializes its resources.
     *
     * Pre-conditions:
     * - gameData must not be null
     * - world must not be null
     * - Plugin must not already be started
     *
     * Post-conditions:
     * - All plugin resources must be properly initialized
     * - Required entities must be created and added to world
     * - Plugin must be in a ready state
     *
     * @param gameData Contains game state and configuration
     * @param world Contains all game entities
     * @throws NullPointerException if gameData or world is null
     * @throws IllegalStateException if plugin is already started
     */
    void start(GameData gameData, World world);

    /**
     * Stops the plugin and cleans up its resources.
     *
     * Pre-conditions:
     * - gameData must not be null
     * - world must not be null
     * - Plugin must be in started state
     *
     * Post-conditions:
     * - All plugin resources must be properly released
     * - Plugin entities must be removed from world
     * - Plugin must be in a stopped state
     * - No memory leaks should occur
     *
     * @param gameData Contains game state and configuration
     * @param world Contains all game entities
     * @throws NullPointerException if gameData or world is null
     * @throws IllegalStateException if plugin is not started
     */
    void stop(GameData gameData, World world);
}
