package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public interface IEntityFactoryService {
    /**
     * Creates an entity of the specified type.
     *
     * @param entityType The type of entity to create
     * @param gameData Game state and configuration
     * @return The created entity
     * @throws IllegalArgumentException if entityType is invalid
     */
    Entity createEntity(String entityType, GameData gameData);
}
