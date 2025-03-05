package dk.sdu.mmmi.cbse.rendersystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public class RenderPlugin implements IPluginLifecycle {
    @Override
    public void start(GameData gameData, World world) {
        // Init rendering here
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Clean up rendering here
    }
}
