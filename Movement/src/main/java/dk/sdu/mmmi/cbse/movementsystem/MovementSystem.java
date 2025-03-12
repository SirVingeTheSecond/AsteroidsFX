package dk.sdu.mmmi.cbse.movementsystem;

import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

/**
 * System that handles movement for all entities with movement components.
 * Centralizes movement logic to avoid duplication across entity-specific systems.
 */
public class MovementSystem implements IEntityProcessingService {
    private final Random random = new Random();
    private static final long DIRECTION_CHANGE_DELAY = 120; // frames

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            // Skip entities without required components
            if (!entity.hasComponent(TransformComponent.class) ||
                    !entity.hasComponent(MovementComponent.class)) {
                continue;
            }

            TransformComponent transform = entity.getComponent(TransformComponent.class);
            MovementComponent movement = entity.getComponent(MovementComponent.class);


            TagComponent tag = entity.getComponent(TagComponent.class);
            if (tag != null && tag.hasTag(TagComponent.TAG_PLAYER)) {
                continue;
            }

            // Process based on movement pattern
            switch (movement.getPattern()) {
                case LINEAR:
                    processLinearMovement(transform, movement);
                    break;
                case RANDOM:
                    processRandomMovement(transform, movement);
                    break;
                case HOMING:
                    // Homing movement is handled by AI systems that set rotation directly
                    // Just apply linear movement based on current rotation
                    processLinearMovement(transform, movement);
                    break;
                case PLAYER:
                    // Player movement is handled by PlayerControlSystem
                    // This prevents duplicate movement processing
                    break;
            }

            // Apply rotation
            if (Math.abs(movement.getRotationSpeed()) > 0.0001f) {
                transform.setRotation(transform.getRotation() + movement.getRotationSpeed());
            }
        }
    }

    private void processLinearMovement(TransformComponent transform, MovementComponent movement) {
        if (movement.getSpeed() <= 0) {
            return; // No movement
        }

        // Calculate movement based on rotation and speed
        double radians = Math.toRadians(transform.getRotation());
        double deltaX = Math.cos(radians) * movement.getSpeed();
        double deltaY = Math.sin(radians) * movement.getSpeed();

        // Update position
        transform.setX(transform.getX() + deltaX);
        transform.setY(transform.getY() + deltaY);
    }

    private void processRandomMovement(TransformComponent transform, MovementComponent movement) {
        // Check if it's time to change direction
        long lastChange = movement.getLastDirectionChange();
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastChange > DIRECTION_CHANGE_DELAY) {
            // Randomly adjust rotation between fixed time intervals
            if (random.nextFloat() < 0.1f) {
                double rotation = transform.getRotation();
                rotation += (random.nextFloat() * 60 - 30); // +/- 30 degrees
                transform.setRotation(rotation);
                movement.setLastDirectionChange(currentTime);
            }
        }

        // Apply linear movement in current direction
        processLinearMovement(transform, movement);
    }
}