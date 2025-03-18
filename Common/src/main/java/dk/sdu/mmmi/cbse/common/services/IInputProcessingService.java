package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 * Interface for systems that process input.
 * Separate from entity processing to allow input handling first in the frame.
 */
public interface IInputProcessingService {
    /**
     * Process input and update game state accordingly
     *
     * @param gameData Current game state with input information
     */
    void process(GameData gameData);
}