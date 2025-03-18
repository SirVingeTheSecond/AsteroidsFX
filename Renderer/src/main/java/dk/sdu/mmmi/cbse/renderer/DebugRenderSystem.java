package dk.sdu.mmmi.cbse.renderer;

import dk.sdu.mmmi.cbse.common.Vector2D;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.HealthComponent;
import dk.sdu.mmmi.cbse.common.services.IDebugService;
import dk.sdu.mmmi.cbse.common.services.IRenderSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Debug visualization for entity information using Vector2D.
 */
public class DebugRenderSystem implements IRenderSystem, IDebugService {
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
        if (!enabled && !gameData.isDebugMode()) {
            return;
        }

        // Draw entity debug info
        for (Entity entity : world.getEntities()) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            if (transform == null) continue;

            // Get position from Vector2D
            Vector2D position = transform.getPosition();
            float radius = transform.getRadius();

            // Draw collision radius
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(0.5);
            gc.strokeOval(
                    position.getX() - radius,
                    position.getY() - radius,
                    radius * 2,
                    radius * 2
            );

            // Draw entity ID
            gc.setFill(Color.WHITE);
            String shortId = entity.getID().substring(0, 8) + "...";
            gc.fillText(shortId, position.getX() + 15, position.getY() - 10);

            // Draw forward vector using Vector2D
            Vector2D forward = transform.getForward();
            Vector2D endpoint = position.add(forward.scale(20));

            gc.setStroke(Color.CYAN);
            gc.strokeLine(
                    position.getX(),
                    position.getY(),
                    endpoint.getX(),
                    endpoint.getY()
            );

            // Draw health bar if applicable
            HealthComponent health = entity.getComponent(HealthComponent.class);
            if (health != null) {
                drawHealthBar(gc, position, radius, health);
            }
        }

        // Draw world bounds
        gc.setStroke(Color.RED);
        gc.setLineWidth(1.0);
        gc.strokeRect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayHeight());

        // Draw performance metrics
        drawPerformanceMetrics(gc, gameData, world);
    }

    private void drawHealthBar(GraphicsContext gc, Vector2D position, float radius, HealthComponent health) {
        double barWidth = 30;
        double barHeight = 4;
        double x = position.getX() - barWidth / 2;
        double y = position.getY() - radius - 10;

        // Background
        gc.setFill(Color.RED);
        gc.fillRect(x, y, barWidth, barHeight);

        // Health amount
        gc.setFill(Color.GREEN);
        gc.fillRect(x, y, barWidth * health.getHealthPercentage(), barHeight);

        // Border
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.5);
        gc.strokeRect(x, y, barWidth, barHeight);
    }

    private void drawPerformanceMetrics(GraphicsContext gc, GameData gameData, World world) {
        gc.setFill(Color.WHITE);
        double y = 40;
        double lineHeight = 15;

        gc.fillText("FPS: " + String.format("%.1f", gameData.getCurrentFps()), 10, y);
        y += lineHeight;

        gc.fillText("Entities: " + world.getEntities().size(), 10, y);
        y += lineHeight;
    }

    @Override
    public int getPriority() {
        return 1000; // Debug renderer should be last (on top)
    }
}