package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.components.ShootingComponent;

/**
 * Interface for systems that handle shooting mechanics.
 */
public interface IShootingSystem extends IEntityProcessingService {
    /**
     * Check if an entity can shoot based on its cooldown state.
     *
     * @param shootingComponent The shooting component to check
     * @return true if the entity can shoot, false otherwise
     */
    boolean canShoot(ShootingComponent shootingComponent);

    /**
     * Update the cooldown timer of a shooting component.
     *
     * @param shootingComponent The shooting component to update
     */
    void updateCooldown(ShootingComponent shootingComponent);

    /**
     * Reset the cooldown timer of a shooting component after shooting.
     *
     * @param shootingComponent The shooting component to reset
     */
    void resetCooldown(ShootingComponent shootingComponent);
}