package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Factory interface for creating game entities.
 * Ensures consistent entity creation across components.
 */
public interface IEntityFactory<T extends Entity> {
    /**
     * Create a new entity instance.
     *
     * @pre gameData != null
     * @post Created entity has valid properties
     * @post Entity ready for world addition
     *
     * @param gameData Current game state
     * @return New entity instance
     * @throws IllegalStateException if entity cannot be created
     */
    T createEntity(GameData gameData);
}
