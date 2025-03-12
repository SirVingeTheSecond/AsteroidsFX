// DebugRenderSystem.java (new)
package dk.sdu.mmmi.cbse.rendersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.services.IDebugService;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Debug visualization system for entity information
 */
public class DebugRenderSystem implements IDebugService {
    private boolean enabled = false;

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void render(GraphicsContext gc, GameData gameData, World world) {
        if (!enabled) return;

        // Draw entity information
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        gc.setLineWidth(1);

        for (Entity entity : world.getEntities()) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            if (transform == null) continue;

            // Draw entity ID and position
            String info = "ID: " + entity.getID().substring(0, 8) + "...";
            gc.fillText(info, transform.getX() + 15, transform.getY() - 5);

            // Draw collision radius
            gc.setStroke(Color.YELLOW);
            gc.strokeOval(
                    transform.getX() - transform.getRadius(),
                    transform.getY() - transform.getRadius(),
                    transform.getRadius() * 2,
                    transform.getRadius() * 2
            );
        }

        // Draw game information
        String gameInfo = "Entities: " + world.getEntities().size();
        gc.setFill(Color.WHITE);
        gc.fillText(gameInfo, 10, 20);
    }
}