package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.components.ShootingComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.ShootEvent;
import dk.sdu.mmmi.cbse.common.input.Input;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;

public class PlayerControlSystem implements IEntityProcessingService {
    private final IGameEventService eventService;
    private final float rotationSpeed = 180.0f; // degrees per second

    public PlayerControlSystem() {
        this.eventService = ServiceLocator.getService(IGameEventService.class);
    }

    @Override
    public void process(GameData gameData, World world) {
        float deltaTime = (float) Time.getDeltaTime();

        for (Entity entity : world.getEntities()) {
            // Skip entities that aren't players or miss required components
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (tagComponent == null || !tagComponent.hasType(EntityType.PLAYER)) {
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

            // Process player input
            processInput(entity, transform, movement, shooting, deltaTime);
        }
    }

    private void processInput(Entity entity,
                              TransformComponent transform,
                              MovementComponent movement,
                              ShootingComponent shooting,
                              float deltaTime) {
        // Update shooting cooldown
        shooting.updateCooldown();

        // Handle rotation using either direct keys or horizontal axis
        float rotationInput = 0;

        // Check direct key input
        if (Input.getKey(Input.KeyCode.LEFT)) {
            rotationInput -= 1;
        }
        if (Input.getKey(Input.KeyCode.RIGHT)) {
            rotationInput += 1;
        }

        // Or use horizontal axis if available
        if (rotationInput == 0) {
            rotationInput = Input.getAxis("Horizontal");
        }

        // Apply rotation with framerate independence
        if (Math.abs(rotationInput) > 0.001f) {
            transform.rotate(rotationInput * rotationSpeed * deltaTime);
        }

        // Set acceleration flag based on input (UP key or Vertical axis)
        boolean accelerating = Input.getKey(Input.KeyCode.UP) ||
                Input.getAxis("Vertical") > 0.1f;
        movement.setAccelerating(accelerating);

        // Apply movement if accelerating
        if (accelerating) {
            float speed = movement.getSpeed();
            Vector2D velocity = transform.getForward().scale(speed * deltaTime);
            transform.translate(velocity);
        }

        // Handle shooting input (Space key)
        if (Input.getKeyDown(Input.KeyCode.SPACE) && shooting.isCanShoot()) {
            // Reset cooldown
            shooting.resetCooldown();

            // Publish shoot event
            eventService.publish(new ShootEvent(entity));
        }
    }
}