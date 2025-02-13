package dk.sdu.mmmi.cbse.collisionsystem.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import java.util.*;

/**
 * Handles spatial partitioning and collision detection.
 * Single responsibility: Efficiently detect potential collisions.
 */
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
        float dx = (float) (entity1.getX() - entity2.getX());
        float dy = (float) (entity1.getY() - entity2.getY());
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private List<Integer> getCellsForEntity(Entity entity) {
        List<Integer> cells = new ArrayList<>();
        int minX = (int)((entity.getX() - entity.getRadius()) / CELL_SIZE);
        int maxX = (int)((entity.getX() + entity.getRadius()) / CELL_SIZE);
        int minY = (int)((entity.getY() - entity.getRadius()) / CELL_SIZE);
        int maxY = (int)((entity.getY() + entity.getRadius()) / CELL_SIZE);

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
