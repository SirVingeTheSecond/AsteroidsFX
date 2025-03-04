package dk.sdu.mmmi.cbse.collisionsystem.collision;

import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import java.util.*;

// Handles spatial partitioning and collision detection.
// Is this efficient for detecting potential collision?
public class CollisionDetector {
    private static final int CELL_SIZE = 64;
    private final Map<Integer, Set<Entity>> grid = new HashMap<>();

    public void clear() {
        grid.clear();
    }

    public void addToGrid(Entity entity) {
        List<Integer> cells = getCellsForEntity(entity);
        for (Integer cell : cells) {
            grid.computeIfAbsent(cell, k -> new HashSet<>()).add(entity);
        }
    }

    public Set<Entity> getPotentialCollisions(Entity entity) {
        Set<Entity> result = new HashSet<>();
        List<Integer> cells = getCellsForEntity(entity);

        for (Integer cell : cells) {
            Set<Entity> entitiesInCell = grid.get(cell);
            if (entitiesInCell != null) {
                result.addAll(entitiesInCell);
            }
        }

        return result;
    }

    public boolean checkCollision(Entity entity1, Entity entity2) {
        TransformComponent transform1 = entity1.getComponent(TransformComponent.class);
        TransformComponent transform2 = entity2.getComponent(TransformComponent.class);

        float dx = (float) (transform1.getX() - transform2.getX());
        float dy = (float) (transform1.getY() - transform2.getY());
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (transform1.getRadius() + transform2.getRadius());
    }

    private List<Integer> getCellsForEntity(Entity entity) {
        List<Integer> cells = new ArrayList<>();
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        int minX = (int)((transform.getX() - transform.getRadius()) / CELL_SIZE);
        int maxX = (int)((transform.getX() + transform.getRadius()) / CELL_SIZE);
        int minY = (int)((transform.getY() - transform.getRadius()) / CELL_SIZE);
        int maxY = (int)((transform.getY() + transform.getRadius()) / CELL_SIZE);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                cells.add(getKey(x, y));
            }
        }
        return cells;
    }

    private int getKey(int x, int y) {
        return x * 73856093 ^ y * 19349663; // Spatial hash function
    }
}