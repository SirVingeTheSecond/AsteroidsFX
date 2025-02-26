package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Interface for implementation of movement strategy.
 * Abstraction for different movement behaviors.
 */
public interface IMovementService {

    /**
     * Apply movement to an entity based on its components.
     *
     * @pre entity != null
     * @pre entity has required movement components
     * @post entity position is updated according to movement strategy
     *
     * @param entity The entity to move
     * @param gameData Current game state
     * @return true if movement was applied, false otherwise
     */
    boolean move(Entity entity, GameData gameData);

    /**
     * Get the movement type this service handles.
     *
     * @return The movement pattern type
     */
    String getMovementType();
}
