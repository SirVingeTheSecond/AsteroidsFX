package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for systems that render game elements.
 */
public interface IRenderSystem {
    /**
     * Render game elements to the graphics context
     *
     * @param gc Graphics context to render to
     * @param gameData Current game state
     * @param world Game world containing entities
     */
    void render(GraphicsContext gc, GameData gameData, World world);

    /**
     * Get the priority of this render system.
     * Higher priority systems render later (on top).
     *
     * @return Render priority
     */
    default int getPriority() {
        return 0;
    }
}