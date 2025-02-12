package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.CollisionEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.*;
import java.util.ServiceLoader;

public class CollisionSystem implements IPostEntityProcessingService {
    private static final int CELL_SIZE = 64;
    private final Map<Integer, Set<Entity>> grid = new HashMap<>();
    private IGameEventService eventService;

    public CollisionSystem() {
        // Load event service through ServiceLoader
        ServiceLoader<IGameEventService> loader = ServiceLoader.load(IGameEventService.class);
        Optional<IGameEventService> eventServiceOptional = loader.findFirst();
        if (eventServiceOptional.isPresent()) {
            this.eventService = eventServiceOptional.get();
        } else {
            throw new RuntimeException("No IGameEventService implementation found");
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        // Clear the grid
        grid.clear();

        // Broad phase - populate grid
        for (Entity entity : world.getEntities()) {
            addToGrid(entity);
        }

        // Narrow phase - check collisions
        Set<CollisionPair> processedPairs = new HashSet<>();

        for (Entity entity : world.getEntities()) {
            Set<Entity> potentialCollisions = getPotentialCollisions(entity);

            for (Entity other : potentialCollisions) {
                if (entity == other) continue;

                CollisionPair pair = new CollisionPair(entity, other);
                if (processedPairs.contains(pair)) continue;

                if (checkCollision(entity, other)) {
                    eventService.publish(new CollisionEvent(entity, entity, other));
                }

                processedPairs.add(pair);
            }
        }
    }

    private void addToGrid(Entity entity) {
        List<Integer> cells = getCellsForEntity(entity);
        for (Integer cell : cells) {
            grid.computeIfAbsent(cell, k -> new HashSet<>()).add(entity);
        }
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

    private Set<Entity> getPotentialCollisions(Entity entity) {
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

    private boolean checkCollision(Entity entity1, Entity entity2) {
        float dx = (float) (entity1.getX() - entity2.getX());
        float dy = (float) (entity1.getY() - entity2.getY());
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private int getKey(int x, int y) {
        return x * 73856093 ^ y * 19349663; // Spatial hash function
    }

    private static class CollisionPair {
        private final String id1;
        private final String id2;

        public CollisionPair(Entity e1, Entity e2) {
            id1 = e1.getID();
            id2 = e2.getID();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CollisionPair that = (CollisionPair) o;
            return (Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2)) ||
                    (Objects.equals(id1, that.id2) && Objects.equals(id2, that.id1));
        }

        @Override
        public int hashCode() {
            // Order-independent hash
            return id1.compareTo(id2) < 0
                    ? Objects.hash(id1, id2)
                    : Objects.hash(id2, id1);
        }
    }
}