package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public interface IEntityFactory<T extends Entity> {
    /**
     * Creates a new entity with initialized properties.
     *
     * @param gameData Contains game state and configuration
     * @return A new entity instance
     * @throws IllegalStateException if entity cannot be created
     */
    T createEntity(GameData gameData);
}
