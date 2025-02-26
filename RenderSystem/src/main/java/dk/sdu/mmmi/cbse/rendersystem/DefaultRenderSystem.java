package dk.sdu.mmmi.cbse.rendersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.services.IRenderSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRenderSystem implements IRenderSystem {
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();

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
            Polygon polygon = polygons.computeIfAbsent(entity, this::createPolygonForEntity);

            // Update polygon position
            polygon.setTranslateX(transform.getX());
            polygon.setTranslateY(transform.getY());
            polygon.setRotate(transform.getRotation());

            // Draw the polygon
            drawPolygon(gc, polygon, transform);
        }
    }

    private Polygon createPolygonForEntity(Entity entity) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        if (transform == null) {
            return new Polygon();
        }

        return new Polygon(transform.getPolygonCoordinates());
    }

    private void drawPolygon(GraphicsContext gc, Polygon polygon, TransformComponent transform) {
        // Drawing implementation
        // ...
    }
}