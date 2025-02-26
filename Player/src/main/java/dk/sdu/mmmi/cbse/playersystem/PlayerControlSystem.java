package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.components.ShootingComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.data.World;
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
            // Skip entities that aren't players or are missing required components
            if (!hasRequiredComponents(entity)) {
                continue;
            }

            TransformComponent transform = entity.getComponent(TransformComponent.class);
            MovementComponent movement = entity.getComponent(MovementComponent.class);
            ShootingComponent shooting = entity.getComponent(ShootingComponent.class);
            PlayerComponent player = entity.getComponent(PlayerComponent.class);

            // Process player input and update components
            processInput(entity, transform, movement, shooting, player, gameData);

            // Handle invulnerability timer
            if (player.isInvulnerable()) {
                int timer = player.getInvulnerabilityTimer();
                if (timer > 0) {
                    player.setInvulnerabilityTimer(timer - 1);
                } else {
                    player.setInvulnerable(false);
                }
            }

            // Handle screen boundaries
            handleScreenBoundaries(transform, gameData);
        }
    }

    private boolean hasRequiredComponents(Entity entity) {
        TagComponent tagComponent = entity.getComponent(TagComponent.class);
        if (tagComponent == null || !tagComponent.hasTag(TagComponent.TAG_PLAYER)) {
            return false;
        }

        return entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(MovementComponent.class) &&
                entity.hasComponent(ShootingComponent.class) &&
                entity.hasComponent(PlayerComponent.class);
    }

    private void processInput(Entity entity,
                              TransformComponent transform,
                              MovementComponent movement,
                              ShootingComponent shooting,
                              PlayerComponent player,
                              GameData gameData) {
        // Update shooting cooldown
        shooting.updateCooldown();

        // Movement controls
        if (gameData.getKeys().isDown(GameKeys.LEFT)) {
            transform.setRotation(transform.getRotation() - 5);
        }
        if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
            transform.setRotation(transform.getRotation() + 5);
        }
        if (gameData.getKeys().isDown(GameKeys.UP)) {
            // Calculate movement based on rotation
            double radians = Math.toRadians(transform.getRotation());
            double deltaX = Math.cos(radians) * movement.getSpeed();
            double deltaY = Math.sin(radians) * movement.getSpeed();

            // Update position
            transform.setX(transform.getX() + deltaX);
            transform.setY(transform.getY() + deltaY);
        }

        // Shooting control
        if (gameData.getKeys().isPressed(GameKeys.SPACE) && shooting.canShoot()) {
            // Reset cooldown
            shooting.resetCooldown();

            // Publish shoot event
            eventService.publish(new ShootEvent(entity));
        }
    }

    private void handleScreenBoundaries(TransformComponent transform, GameData gameData) {
        // Screen wrapping
        if (transform.getX() < 0) {
            transform.setX(gameData.getDisplayWidth());
        } else if (transform.getX() > gameData.getDisplayWidth()) {
            transform.setX(0);
        }

        if (transform.getY() < 0) {
            transform.setY(gameData.getDisplayHeight());
        } else if (transform.getY() > gameData.getDisplayHeight()) {
            transform.setY(0);
        }
    }
}