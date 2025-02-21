package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPluginLifecycle;
import dk.sdu.mmmi.cbse.common.services.IEntityFactory;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionResponseComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionLayer;
import dk.sdu.mmmi.cbse.common.collision.CollisionGroup;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;

import java.util.ServiceLoader;

public class PlayerPlugin implements IPluginLifecycle, IEntityFactory<Player> {
    private Entity player;
    private final IGameEventService eventService;

    public PlayerPlugin() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public void start(GameData gameData, World world) {
        player = createEntity(gameData);
        world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }

    @Override
    public Player createEntity(GameData gameData) {
        Player playerShip = new Player();

        // Set basic entity properties
        playerShip.setPolygonCoordinates(-5,-5, 10,0, -5,5);
        playerShip.setX(gameData.getDisplayWidth() / 2);
        playerShip.setY(gameData.getDisplayHeight() / 2);
        playerShip.setRadius(8);

        // Add movement component
        MovementComponent movement = new MovementComponent();
        movement.setPattern(MovementComponent.MovementPattern.PLAYER);
        movement.setSpeed(2.0f);
        movement.setRotationSpeed(3.0f);
        playerShip.addComponent(movement);

        // Add collision component
        CollisionComponent collision = new CollisionComponent();
        collision.setLayer(CollisionLayer.PLAYER);
        collision.addGroup(CollisionGroup.FRIENDLY);
        collision.addGroup(CollisionGroup.DESTRUCTIBLE);
        playerShip.addComponent(collision);

        // Add collision response component
        CollisionResponseComponent response = new CollisionResponseComponent();

        // Handle collision with hostile entities (enemies, enemy bullets)
        response.addGroupResponse(CollisionGroup.HOSTILE, (self, other, world) -> {
            eventService.publish(new EntityDestroyedEvent(self, "Player destroyed by hostile entity"));
            world.removeEntity(self);
            return true;
        });

        // Handle collision with obstacles (asteroids)
        response.addLayerResponse(CollisionLayer.OBSTACLE, (self, other, world) -> {
            eventService.publish(new EntityDestroyedEvent(self, "Player destroyed by obstacle"));
            world.removeEntity(self);
            return true;
        });

        playerShip.addComponent(response);

        return playerShip;
    }
}