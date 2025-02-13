package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public interface IPluginLifecycle {
    /**
     * Starts the plugin and initializes its resources.
     *
     * @param gameData Contains game state and configuration
     * @param world Contains all game entities
     * @throws IllegalStateException if plugin is already started
     */
    void start(GameData gameData, World world);

    /**
     * Stops the plugin and cleans up its resources.
     *
     * @param gameData Contains game state and configuration
     * @param world Contains all game entities
     * @throws IllegalStateException if plugin is not started
     */
    void stop(GameData gameData, World world);
}
