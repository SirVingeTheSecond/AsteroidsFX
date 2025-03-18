package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.PlayerComponent;
import dk.sdu.mmmi.cbse.common.services.IEntityRendererService;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Specialized renderer for player entities.
 * Handles player-specific rendering including invulnerability effects.
 */
public class PlayerRenderer implements IEntityRendererService {

    @Override
    public boolean canRender(Entity entity) {
        TagComponent tagComponent = entity.getComponent(TagComponent.class);
        return tagComponent != null && tagComponent.hasTag(TagComponent.TAG_PLAYER);
    }

    @Override
    public String getRenderLayer() {
        return "PLAYER";
    }

    @Override
    public void render(Entity entity, GraphicsContext gc, GameData gameData) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);

        if (transform == null || playerComponent == null) {
            return;
        }

        // Choose color based on invulnerability state
        Color strokeColor = Color.GREEN;

        if (playerComponent.isInvulnerable()) {
            // Blinking effect for invulnerability
            if ((playerComponent.getInvulnerabilityTimer() / 5) % 2 == 0) {
                strokeColor = Color.BLUE;
            } else {
                strokeColor = Color.CYAN;
            }
        }

        // Draw player shape with invulnerability effect
        drawPlayerShape(gc, transform, strokeColor);

        // Draw lives indicator
        drawLivesIndicator(gc, gameData, playerComponent);
    }

    /**
     * Draw the player ship shape
     */
    private void drawPlayerShape(GraphicsContext gc, TransformComponent transform, Color strokeColor) {
        double[] coordinates = transform.getPolygonCoordinates();
        if (coordinates == null || coordinates.length < 6) {
            return;
        }

        int numPoints = coordinates.length / 2;
        double[] xPoints = new double[numPoints];
        double[] yPoints = new double[numPoints];

        // Apply rotation and translation
        double rotation = Math.toRadians(transform.getRotation());
        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);

        for (int i = 0; i < numPoints; i++) {
            double x = coordinates[i * 2];
            double y = coordinates[i * 2 + 1];

            // Apply rotation
            double rotatedX = x * cos - y * sin;
            double rotatedY = x * sin + y * cos;

            // Apply translation
            xPoints[i] = rotatedX + transform.getX();
            yPoints[i] = rotatedY + transform.getY();
        }

        // Draw the polygon with thicker line for player
        gc.setStroke(strokeColor);
        gc.setLineWidth(2.0);
        gc.strokePolygon(xPoints, yPoints, numPoints);

        // Add engine thrust effect
        if (Math.random() > 0.3) {
            drawThrustEffect(gc, transform);
        }
    }

    /**
     * Draw engine thrust effect behind player ship
     */
    private void drawThrustEffect(GraphicsContext gc, TransformComponent transform) {
        // Calculate position behind the ship
        double backAngle = Math.toRadians(transform.getRotation() + 180);
        double thrustX = transform.getX() + Math.cos(backAngle) * transform.getRadius();
        double thrustY = transform.getY() + Math.sin(backAngle) * transform.getRadius();

        // Draw flame-like shape
        double flameSize = 5 + Math.random() * 5;

        // Use semi-transparent flame colors
        gc.setFill(Color.rgb(255, 150, 50, 0.7));

        // Create a simple flame shape
        double[] xPoints = {
                thrustX,
                thrustX + Math.cos(backAngle + 0.5) * flameSize,
                thrustX + Math.cos(backAngle) * (flameSize * 1.5),
                thrustX + Math.cos(backAngle - 0.5) * flameSize
        };

        double[] yPoints = {
                thrustY,
                thrustY + Math.sin(backAngle + 0.5) * flameSize,
                thrustY + Math.sin(backAngle) * (flameSize * 1.5),
                thrustY + Math.sin(backAngle - 0.5) * flameSize
        };

        gc.fillPolygon(xPoints, yPoints, 4);
    }

    /**
     * Draw lives indicator in the corner of the screen
     */
    private void drawLivesIndicator(GraphicsContext gc, GameData gameData, PlayerComponent playerComponent) {
        int lives = playerComponent.getLives();

        gc.setFill(Color.WHITE);
        gc.fillText("LIVES: " + lives, 10, 20);

        // Draw small ship icons
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(1.0);

        for (int i = 0; i < lives; i++) {
            double x = 80 + (i * 20);
            double y = 15;

            double[] xPoints = {x, x - 5, x + 5};
            double[] yPoints = {y - 5, y + 5, y + 5};

            gc.strokePolygon(xPoints, yPoints, 3);
        }
    }
}