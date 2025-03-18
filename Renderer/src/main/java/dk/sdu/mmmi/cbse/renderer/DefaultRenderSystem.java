package dk.sdu.mmmi.cbse.renderer;

import dk.sdu.mmmi.cbse.common.Vector2D;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.RendererComponent;
import dk.sdu.mmmi.cbse.common.services.IRenderSystem;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main rendering system that renders all entities with RendererComponent.
 * Uses a Unity-like approach with layer-based rendering and Vector2D for transformations.
 */
public class DefaultRenderSystem implements IRenderSystem {
    private static final Logger LOGGER = Logger.getLogger(DefaultRenderSystem.class.getName());

    // Cache for entity layers to avoid sorting every frame
    private final Map<Integer, List<Entity>> layerMap = new HashMap<>();
    private final Map<String, List<Vector2D>> transformedPointsCache = new ConcurrentHashMap<>();

    @Override
    public void render(GraphicsContext gc, GameData gameData, World world) {
        // Update layer map with current entities
        updateLayerMap(world);

        // Get sorted layer indices
        List<Integer> sortedLayers = new ArrayList<>(layerMap.keySet());
        Collections.sort(sortedLayers);

        // Get the interpolation factor for smooth rendering
        float alpha = gameData.getInterpolation();

        // Render each layer in order
        for (Integer layer : sortedLayers) {
            List<Entity> entities = layerMap.get(layer);

            // Skip empty layers
            if (entities == null || entities.isEmpty()) {
                continue;
            }

            // Render all entities in this layer
            for (Entity entity : entities) {
                renderEntity(gc, entity, alpha);
            }
        }

        // Render FPS counter if in debug mode
        if (gameData.isDebugMode()) {
            renderDebugInfo(gc, gameData);
        }
    }

    /**
     * Update the layer map with current entities from the world.
     */
    private void updateLayerMap(World world) {
        // Clear previous frame's layer mappings
        layerMap.clear();
        transformedPointsCache.clear();

        // Process all entities
        for (Entity entity : world.getEntities()) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            RendererComponent renderer = entity.getComponent(RendererComponent.class);

            // Skip entities without required components or invisible entities
            if (transform == null || renderer == null || !renderer.isVisible()) {
                continue;
            }

            // Get the render layer
            int layer = renderer.getRenderLayer();

            // Add to the layer map
            layerMap.computeIfAbsent(layer, k -> new ArrayList<>())
                    .add(entity);
        }
    }

    /**
     * Render a single entity using its RendererComponent and TransformComponent.
     */
    private void renderEntity(GraphicsContext gc, Entity entity, float alpha) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        RendererComponent renderer = entity.getComponent(RendererComponent.class);

        // This should never happen due to filtering in updateLayerMap
        if (transform == null || renderer == null) {
            return;
        }

        // Apply renderer properties
        gc.setStroke(renderer.getStrokeColor());
        gc.setFill(renderer.getFillColor());
        gc.setLineWidth(renderer.getStrokeWidth());
        gc.setGlobalAlpha(renderer.getOpacity());

        // Draw the shape
        drawTransformedShape(gc, entity, transform);

        // Reset opacity
        gc.setGlobalAlpha(1.0);
    }

    /**
     * Draw a transformed shape based on the entity's transform component.
     */
    private void drawTransformedShape(GraphicsContext gc, Entity entity, TransformComponent transform) {
        double[] coordinates = transform.getPolygonCoordinates();
        if (coordinates == null || coordinates.length < 6) {
            // Fall back to circle if no valid polygon
            Vector2D position = transform.getPosition();
            float radius = transform.getRadius();

            gc.strokeOval(
                    position.getX() - radius,
                    position.getY() - radius,
                    radius * 2,
                    radius * 2
            );
            return;
        }

        // Use cached transformed coordinates if available or calculate new ones
        String cacheKey = entity.getID() + "_" + transform.getRotation() + "_" + transform.getPosition().getX() + "_" + transform.getPosition().getY();
        List<Vector2D> transformedPoints = transformedPointsCache.computeIfAbsent(cacheKey, k -> {
            return calculateTransformedPoints(coordinates, transform);
        });

        // Convert to arrays for JavaFX
        double[] xPoints = new double[transformedPoints.size()];
        double[] yPoints = new double[transformedPoints.size()];

        for (int i = 0; i < transformedPoints.size(); i++) {
            Vector2D point = transformedPoints.get(i);
            xPoints[i] = point.getX();
            yPoints[i] = point.getY();
        }

        // Draw the polygon
        gc.strokePolygon(xPoints, yPoints, transformedPoints.size());
        gc.fillPolygon(xPoints, yPoints, transformedPoints.size());
    }

    /**
     * Calculate transformed points for a polygon using Vector2D.
     */
    private List<Vector2D> calculateTransformedPoints(double[] coordinates, TransformComponent transform) {
        List<Vector2D> points = new ArrayList<>();
        Vector2D position = transform.getPosition();
        float rotation = transform.getRotation();

        // Create original points
        for (int i = 0; i < coordinates.length; i += 2) {
            float x = (float) coordinates[i];
            float y = (float) coordinates[i + 1];
            Vector2D point = new Vector2D(x, y);

            // Rotate the point
            Vector2D rotatedPoint = point.rotate((float) Math.toRadians(rotation));

            // Translate to entity position
            Vector2D transformedPoint = rotatedPoint.add(position);
            points.add(transformedPoint);
        }

        return points;
    }

    /**
     * Render debug information on screen.
     */
    private void renderDebugInfo(GraphicsContext gc, GameData gameData) {
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillText(String.format("FPS: %.1f", gameData.getCurrentFps()), 10, 20);
    }

    @Override
    public int getPriority() {
        return 0; // Base render system has default priority
    }
}