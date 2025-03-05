package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.components.HealthComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Interface for systems that handle health-related logic.
 */
public interface IHealthSystem extends IEntityProcessingService {
    /**
     * Apply damage to an entity with bounds checking.
     *
     * @param entity The entity to damage
     * @param healthComponent The health component to modify
     * @param amount The amount of damage to apply
     * @return true if the entity is still alive, false if it died
     */
    boolean applyDamage(Entity entity, HealthComponent healthComponent, float amount);

    /**
     * Heal an entity with bounds checking.
     *
     * @param entity The entity to heal
     * @param healthComponent The health component to modify
     * @param amount The amount of healing to apply
     */
    void applyHealing(Entity entity, HealthComponent healthComponent, float amount);
}