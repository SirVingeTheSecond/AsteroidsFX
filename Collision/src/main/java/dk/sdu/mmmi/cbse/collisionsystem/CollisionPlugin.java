package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Plugin for the Collision module - handles startup and shutdown
 */
public class CollisionPlugin implements IPluginLifecycle {

    @Override
    public void start(GameData gameData, World world) {
        // Initialize collision resources if needed
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Clean up collision resources if needed
    }
}