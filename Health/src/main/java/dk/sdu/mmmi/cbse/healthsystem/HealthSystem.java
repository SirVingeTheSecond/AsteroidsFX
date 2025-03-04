package dk.sdu.mmmi.cbse.healthsystem;

import dk.sdu.mmmi.cbse.common.components.HealthComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;

import java.util.ServiceLoader;

public class HealthSystem implements IEntityProcessingService {
    private final IGameEventService eventService;

    public HealthSystem() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public void process(GameData gameData, World world) {
        // Process entities with HealthComponent
        for (Entity entity : world.getEntities()) {
            if (!entity.hasComponent(HealthComponent.class)) continue;

            HealthComponent health = entity.getComponent(HealthComponent.class);

            // Check if entity died this frame
            if (!health.isAlive()) {
                // Publish entity destroyed event
                eventService.publish(new EntityDestroyedEvent(entity, "Health depleted"));
                world.removeEntity(entity);
            }
        }
    }

    /**
     * Apply damage to an entity
     * @return true if entity is still alive, false if it died
     */
    public boolean damage(Entity entity, float amount) {
        if (!entity.hasComponent(HealthComponent.class)) return false;

        HealthComponent health = entity.getComponent(HealthComponent.class);
        health.setHealth(health.getHealth() - amount);
        return health.isAlive();
    }

    /**
     * Heal an entity
     */
    public void heal(Entity entity, float amount) {
        if (!entity.hasComponent(HealthComponent.class)) return;

        HealthComponent health = entity.getComponent(HealthComponent.class);
        health.setHealth(health.getHealth() + amount);
    }
}
