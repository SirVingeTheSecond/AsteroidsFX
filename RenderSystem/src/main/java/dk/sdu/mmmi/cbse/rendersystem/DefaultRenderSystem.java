package dk.sdu.mmmi.cbse.rendersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.PlayerComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.services.IRenderSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRenderSystem implements IRenderSystem {
    private final Map<String, Polygon> polygons = new ConcurrentHashMap<>();

    @Override
    public void render(GraphicsContext gc, GameData gameData, World world) {
        // Clear canvas
        gc.clearRect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayHeight());

        // Draw all entities
        for (Entity entity : world.getEntities()) {
            if (!entity.hasComponent(TransformComponent.class)) {
                continue;
            }

            TransformComponent transform = entity.getComponent(TransformComponent.class);

            // Get or create polygon
            Polygon polygon = polygons.computeIfAbsent(entity.getID(), id -> createPolygonForEntity(entity));

            // Update polygon position
            polygon.setTranslateX(transform.getX());
            polygon.setTranslateY(transform.getY());
            polygon.setRotate(transform.getRotation());

            // Choose color based on entity type
            Color strokeColor = determineEntityColor(entity);
            Color fillColor = Color.TRANSPARENT;

            // Special case for invulnerable player
            PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (playerComponent != null && tagComponent != null &&
                    tagComponent.hasTag(TagComponent.TAG_PLAYER) &&
                    playerComponent.isInvulnerable()) {

                // Use blinking effect for invulnerable player
                if ((playerComponent.getInvulnerabilityTimer() / 15) % 2 == 0) {
                    strokeColor = Color.BLUE;
                } else {
                    strokeColor = Color.WHITE;
                }
            }

            // Draw the polygon
            drawPolygon(gc, transform, strokeColor, fillColor);
        }
    }

    private Polygon createPolygonForEntity(Entity entity) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        if (transform == null) {
            return new Polygon();
        }

        return new Polygon(transform.getPolygonCoordinates());
    }

    private Color determineEntityColor(Entity entity) {
        TagComponent tagComponent = entity.getComponent(TagComponent.class);
        if (tagComponent == null) {
            return Color.WHITE; // Default color
        }

        if (tagComponent.hasTag(TagComponent.TAG_PLAYER)) {
            return Color.GREEN;
        } else if (tagComponent.hasTag(TagComponent.TAG_ENEMY)) {
            return Color.RED;
        } else if (tagComponent.hasTag(TagComponent.TAG_BULLET)) {
            // Check bullet component for owner
            return Color.YELLOW;
        } else if (tagComponent.hasTag(TagComponent.TAG_ASTEROID)) {
            return Color.LIGHTGRAY;
        }

        return Color.WHITE; // Default
    }

    private void drawPolygon(GraphicsContext gc, TransformComponent transform, Color strokeColor, Color fillColor) {
        double[] shape = transform.getPolygonCoordinates();
        if (shape == null || shape.length < 6) { // Need at least 3 points (6 values)
            return;
        }

        // Prepare arrays for transformed coordinates
        double[] xPoints = new double[shape.length / 2];
        double[] yPoints = new double[shape.length / 2];

        // Apply rotation and translation
        double rotation = Math.toRadians(transform.getRotation());
        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);

        for (int i = 0; i < shape.length / 2; i++) {
            // Get original point from shape
            double x = shape[i * 2];
            double y = shape[i * 2 + 1];

            // Apply rotation
            double rotatedX = x * cos - y * sin;
            double rotatedY = x * sin + y * cos;

            // Apply translation
            xPoints[i] = rotatedX + transform.getX();
            yPoints[i] = rotatedY + transform.getY();
        }

        // Draw the transformed polygon
        gc.setStroke(strokeColor);
        gc.setFill(fillColor);
        gc.strokePolygon(xPoints, yPoints, xPoints.length);
        gc.fillPolygon(xPoints, yPoints, xPoints.length);
    }
}