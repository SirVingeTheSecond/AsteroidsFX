package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for game loop handling.
 */
public interface IGameLoop {
    /**
     * Initialize the game loop.
     *
     * @pre gameData != null
     * @pre world != null
     * @post Game loop ready to start
     */
    void initialize(GameData gameData, World world);

    /**
     * Start the game loop.
     *
     * @pre initialize has been called
     * @post Game loop is running
     */
    void start();

    /**
     * Stop the game loop.
     *
     * @post Game loop is stopped
     */
    void stop();
}