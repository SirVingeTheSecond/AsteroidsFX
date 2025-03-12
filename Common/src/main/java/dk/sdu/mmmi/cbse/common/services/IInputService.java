package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Interface for handling input from different sources.
 * Abstracts input handling to allow different input methods.
 */
public interface IInputService {
    /**
     * Process inputs from the source.
     *
     * @pre gameData != null
     * @post Input state is updated in gameData
     *
     * @param gameData Current game state containing input state
     */
    void processInput(GameData gameData);

    /**
     * Get the input type this service handles.
     *
     * @return String identifier for the input type
     */
    String getInputType();
}