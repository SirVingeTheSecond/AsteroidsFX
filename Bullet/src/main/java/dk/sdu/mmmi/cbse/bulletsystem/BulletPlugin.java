package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;

import java.util.ServiceLoader;

public class BulletPlugin implements IGamePluginService {
    private final IGameEventService eventService;

    public BulletPlugin() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public void start(GameData gameData, World world) {
        // Register the ShootSystem to listen for ShootEvents
        // Is this a smart approach?
        ShootSystem shootSystem = new ShootSystem();
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove all bullets
        for (Entity entity : world.getEntities()) {
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (tagComponent != null && tagComponent.hasTag(TagComponent.TAG_BULLET)) {
                world.removeEntity(entity);
            }
        }
    }
}