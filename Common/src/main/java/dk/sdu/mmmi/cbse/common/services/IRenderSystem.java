package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for rendering game elements.
 */
public interface IRenderSystem {
    /**
     * Render all visible game elements.
     *
     * @pre gc != null
     * @pre gameData != null
     * @pre world != null
     * @post Game entities are rendered to the provided GraphicsContext
     *
     * @param gc Graphics context to render to
     * @param gameData Current game state
     * @param world Game world containing entities
     */
    void render(GraphicsContext gc, GameData gameData, World world);
}