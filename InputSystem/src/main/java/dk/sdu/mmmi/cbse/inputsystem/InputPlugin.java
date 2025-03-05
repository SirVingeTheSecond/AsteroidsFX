package dk.sdu.mmmi.cbse.inputsystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Plugin for the Input module - handles startup and shutdown
 */
public class InputPlugin implements IPluginLifecycle {

    @Override
    public void start(GameData gameData, World world) {
        // Initialize input resources if needed
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Clean up input resources if needed
    }
}