/**
package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.*;

class SpatialHashGrid {
    private final int cellSize;
    private final Map<Long, List<Entity>> grid = new HashMap<>();

    public SpatialHashGrid(int cellSize) {
        this.cellSize = cellSize;
    }

    public void clear() {
        grid.clear();
    }

    public void insertEntity(Entity entity) {
        // Get cell coordinates for entity bounds
        int minX = (int) (entity.getX() - entity.getRadius()) / cellSize;
        int maxX = (int) (entity.getX() + entity.getRadius()) / cellSize;
        int minY = (int) (entity.getY() - entity.getRadius()) / cellSize;
        int maxY = (int) (entity.getY() + entity.getRadius()) / cellSize;

        // Add entity to all cells it overlaps
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                long cellId = hashCell(x, y);
                grid.computeIfAbsent(cellId, k -> new ArrayList<>()).add(entity);
            }
        }
    }

    public List<Entity> getPotentialCollisions(Entity entity) {
        Set<Entity> potentialCollisions = new HashSet<>();

        // Check all cells that the entity overlaps
        int minX = (int) (entity.getX() - entity.getRadius()) / cellSize;
        int maxX = (int) (entity.getX() + entity.getRadius()) / cellSize;
        int minY = (int) (entity.getY() - entity.getRadius()) / cellSize;
        int maxY = (int) (entity.getY() + entity.getRadius()) / cellSize;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                long cellId = hashCell(x, y);
                List<Entity> cellEntities = grid.get(cellId);
                if (cellEntities != null) {
                    potentialCollisions.addAll(cellEntities);
                }
            }
        }

        return new ArrayList<>(potentialCollisions);
    }

    private long hashCell(int x, int y) {
        // Combine x and y coordinates into a single long for cell lookup
        return (((long) x) << 32) | (y & 0xFFFFFFFFL);
    }
}
**/