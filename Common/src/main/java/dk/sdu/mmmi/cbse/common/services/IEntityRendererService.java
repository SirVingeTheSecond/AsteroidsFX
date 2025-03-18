package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for entity-specific renderers.
 * Allows for extensible rendering of different entity types.
 */
public interface IEntityRendererService {
    /**
     * Check if this renderer can render the given entity
     *
     * @param entity Entity to check
     * @return true if this renderer can handle the entity
     */
    boolean canRender(Entity entity);

    /**
     * Get the render layer for this entity type
     *
     * @return The layer this entity type should be rendered on
     */
    String getRenderLayer();

    /**
     * Render the entity to the graphics context
     *
     * @param entity Entity to render
     * @param gc Graphics context to render to
     * @param gameData Current game state
     */
    void render(Entity entity, GraphicsContext gc, GameData gameData);
}