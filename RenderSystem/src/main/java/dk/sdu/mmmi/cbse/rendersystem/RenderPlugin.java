package dk.sdu.mmmi.cbse.rendersystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class RenderPlugin implements IGamePluginService {
    @Override
    public void start(GameData gameData, World world) {
        // Init rendering here
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Clean up rendering here
    }
}
