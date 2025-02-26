package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.components.ShootingComponent;
import dk.sdu.mmmi.cbse.common.components.HealthComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionLayer;
import dk.sdu.mmmi.cbse.common.collision.CollisionGroup;
import dk.sdu.mmmi.cbse.common.collision.CollisionResponseComponent;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IEntityFactory;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Factory for creating player entities.
 * Replaces the Player subclass with a component-based approach.
 */
public class PlayerFactory implements IEntityFactory<Entity> {
    private final IGameEventService eventService;

    public PlayerFactory(IGameEventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public Entity createEntity(GameData gameData) {
        Entity player = new Entity();

        // Add transform component with player shape
        TransformComponent transform = new TransformComponent();
        transform.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        transform.setX(gameData.getDisplayWidth() / 2);
        transform.setY(gameData.getDisplayHeight() / 2);
        transform.setRadius(8);
        player.addComponent(transform);

        // Add player component with player-specific data
        PlayerComponent playerComponent = new PlayerComponent();
        playerComponent.setLives(3);
        player.addComponent(playerComponent);

        // Add movement component
        MovementComponent movement = new MovementComponent();
        movement.setPattern(MovementComponent.MovementPattern.PLAYER);
        movement.setSpeed(2.0f);
        movement.setRotationSpeed(0.0f);
        player.addComponent(movement);

        // Add shooting component
        ShootingComponent shooting = new ShootingComponent();
        shooting.setCooldownMax(20); // 20 frames between shots
        shooting.setProjectileSpeed(2.0f);
        player.addComponent(shooting);

        // Add health component
        HealthComponent health = new HealthComponent(100f);
        player.addComponent(health);

        // Add collision component
        CollisionComponent collision = new CollisionComponent();
        collision.setLayer(CollisionLayer.PLAYER);
        collision.addGroup(CollisionGroup.FRIENDLY);
        collision.addGroup(CollisionGroup.DESTRUCTIBLE);
        player.addComponent(collision);

        // Add collision response component
        CollisionResponseComponent response = new CollisionResponseComponent();

        // Handle collision with hostile entities (enemies, enemy bullets)
        response.addGroupResponse(CollisionGroup.HOSTILE, (self, other, world) -> {
            eventService.publish(new EntityDestroyedEvent(self, "Player destroyed by hostile entity"));
            playerComponent.setLives(playerComponent.getLives() - 1);

            // If lives > 0, don't destroy player but make temporarily invulnerable
            if (playerComponent.getLives() > 0) {
                playerComponent.setInvulnerable(true);
                playerComponent.setInvulnerabilityTimer(180); // 3 seconds at 60 FPS
                return false;
            } else {
                world.removeEntity(self);
                return true;
            }
        });

        // Handle collision with obstacles (asteroids)
        response.addLayerResponse(CollisionLayer.OBSTACLE, (self, other, world) -> {
            eventService.publish(new EntityDestroyedEvent(self, "Player destroyed by obstacle"));
            playerComponent.setLives(playerComponent.getLives() - 1);

            // If lives > 0, don't destroy player but make temporarily invulnerable
            if (playerComponent.getLives() > 0) {
                playerComponent.setInvulnerable(true);
                playerComponent.setInvulnerabilityTimer(180); // 3 seconds at 60 FPS
                return false;
            } else {
                world.removeEntity(self);
                return true;
            }
        });
        player.addComponent(response);

        // Add tag component to identify as player
        player.addComponent(new TagComponent(TagComponent.TAG_PLAYER));

        return player;
    }
}