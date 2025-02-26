package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IPluginLifecycle;

import java.util.ServiceLoader;

public class PlayerPlugin implements IGamePluginService, IPluginLifecycle {
    private Entity player;
    private final PlayerFactory playerFactory;

    public PlayerPlugin() {
        IGameEventService eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));

        this.playerFactory = new PlayerFactory(eventService);
    }

    @Override
    public void start(GameData gameData, World world) {
        // Create a player entity
        player = playerFactory.createEntity(gameData);
        world.addEntity(player);

        System.out.println("Player created with ID: " + player.getID());
        System.out.println("Player position: X=" + player.getX() + ", Y=" + player.getY());
        System.out.println("Player has TransformComponent: " + player.hasComponent(TransformComponent.class));
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove the player
        world.removeEntity(player);
    }
}