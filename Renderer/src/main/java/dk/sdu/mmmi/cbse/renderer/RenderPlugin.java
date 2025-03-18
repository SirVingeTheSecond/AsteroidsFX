package dk.sdu.mmmi.cbse.renderer;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Plugin for renderer lifecycle management.
 * Handles initialization and cleanup of rendering resources.
 */
public class RenderPlugin implements IGamePluginService {
    private static final Logger LOGGER = Logger.getLogger(RenderPlugin.class.getName());

    @Override
    public void start(GameData gameData, World world) {
        LOGGER.log(Level.INFO, "Initializing rendering system");
        // Initialize any resources needed for rendering
    }

    @Override
    public void stop(GameData gameData, World world) {
        LOGGER.log(Level.INFO, "Shutting down rendering system");
        // Cleanup rendering resources
    }
}