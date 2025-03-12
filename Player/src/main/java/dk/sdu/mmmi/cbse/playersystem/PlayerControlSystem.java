package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.components.ShootingComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.events.ShootEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;

import java.util.ServiceLoader;

public class PlayerControlSystem implements IEntityProcessingService {
    private final IGameEventService eventService;

    public PlayerControlSystem() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            // Skip entities that aren't players or miss required components
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (tagComponent == null || !tagComponent.hasTag(TagComponent.TAG_PLAYER)) {
                continue;
            }

            if (!entity.hasComponent(TransformComponent.class) ||
                    !entity.hasComponent(MovementComponent.class) ||
                    !entity.hasComponent(ShootingComponent.class)) {
                continue;
            }

            TransformComponent transform = entity.getComponent(TransformComponent.class);
            MovementComponent movement = entity.getComponent(MovementComponent.class);
            ShootingComponent shooting = entity.getComponent(ShootingComponent.class);

            // Process player input but don't apply movement
            processInput(entity, transform, movement, shooting, gameData);
        }
    }

    private void processInput(Entity entity,
                              TransformComponent transform,
                              MovementComponent movement,
                              ShootingComponent shooting,
                              GameData gameData) {
        // Update shooting cooldown
        shooting.updateCooldown();

        // Handle rotational input only
        if (gameData.getKeys().isDown(GameKeys.LEFT)) {
            transform.setRotation(transform.getRotation() - 5);
        }
        if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
            transform.setRotation(transform.getRotation() + 5);
        }

        // Set acceleration flag based on input
        movement.setAccelerating(gameData.getKeys().isDown(GameKeys.UP));

        // Handle shooting input
        if (gameData.getKeys().isPressed(GameKeys.SPACE) && shooting.canShoot()) {
            // Reset cooldown
            shooting.resetCooldown();

            // Publish shoot event
            eventService.publish(new ShootEvent(entity));
        }
    }
}