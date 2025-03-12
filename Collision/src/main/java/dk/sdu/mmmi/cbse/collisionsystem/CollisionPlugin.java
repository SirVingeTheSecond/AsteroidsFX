package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

/**
 * Plugin for the Collision module - handles startup and shutdown
 */
public class CollisionPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        // Initialize collision resources if needed
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Clean up collision resources if needed
    }
}