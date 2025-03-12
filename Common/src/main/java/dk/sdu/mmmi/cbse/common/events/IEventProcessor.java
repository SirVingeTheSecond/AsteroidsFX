package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public interface IEventProcessor {
    void processEvents(GameData gameData, World world);
}
