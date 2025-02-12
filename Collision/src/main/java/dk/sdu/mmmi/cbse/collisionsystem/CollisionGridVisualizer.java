package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IDebugService;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CollisionGridVisualizer implements IDebugService {
    private static final int CELL_SIZE = 64; // Must match CollisionSystem's CELL_SIZE
    private boolean enabled = false;
    private final CollisionSystem collisionSystem;
    private final World world;

    public CollisionGridVisualizer() {
        // Get references through ServiceLoader
        this.collisionSystem = new CollisionSystem(); // We might want to get this through ServiceLoader too
        this.world = new World();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void render(GraphicsContext gc, GameData gameData) {
        if (!enabled) return;

        // Draw grid
        drawGrid(gc, gameData);

        // Draw entity collision circles
        drawCollisionCircles(gc);
    }

    private void drawGrid(GraphicsContext gc, GameData gameData) {
        // Draw grid lines
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);

        // Vertical lines
        for (int x = 0; x <= gameData.getDisplayWidth(); x += CELL_SIZE) {
            gc.strokeLine(x, 0, x, gameData.getDisplayHeight());
        }

        // Horizontal lines
        for (int y = 0; y <= gameData.getDisplayHeight(); y += CELL_SIZE) {
            gc.strokeLine(0, y, gameData.getDisplayWidth(), y);
        }

        // Highlight cells with entities
        gc.setFill(Color.rgb(0, 255, 0, 0.1)); // Semi-transparent green
        for (Entity entity : world.getEntities()) {
            int cellX = (int) (entity.getX() / CELL_SIZE);
            int cellY = (int) (entity.getY() / CELL_SIZE);
            gc.fillRect(
                    cellX * CELL_SIZE,
                    cellY * CELL_SIZE,
                    CELL_SIZE,
                    CELL_SIZE
            );
        }
    }

    private void drawCollisionCircles(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        for (Entity entity : world.getEntities()) {
            gc.strokeOval(
                    entity.getX() - entity.getRadius(),
                    entity.getY() - entity.getRadius(),
                    entity.getRadius() * 2,
                    entity.getRadius() * 2
            );
        }
    }
}
