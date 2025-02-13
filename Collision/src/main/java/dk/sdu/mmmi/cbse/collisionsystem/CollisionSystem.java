package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.collisionsystem.collision.CollisionDetector;
import dk.sdu.mmmi.cbse.collisionsystem.strategy.ICollisionStrategy;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.CollisionEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.*;

public class CollisionSystem implements IPostEntityProcessingService {
    private final CollisionDetector detector;
    private final List<ICollisionStrategy> strategies;
    private final IGameEventService eventService;

    public CollisionSystem() {
        this.detector = new CollisionDetector();

        // Load required services
        ServiceLoader<IGameEventService> eventLoader = ServiceLoader.load(IGameEventService.class);
        this.eventService = eventLoader.findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));

        // Load collision strategies
        this.strategies = new ArrayList<>();
        ServiceLoader<ICollisionStrategy> strategyLoader = ServiceLoader.load(ICollisionStrategy.class);
        strategyLoader.forEach(strategy -> {
            strategies.add(strategy);
            System.out.println("Loaded collision strategy: " + strategy.getClass().getName());
        });

        if (strategies.isEmpty()) {
            System.out.println("No collision strategies were loaded!");
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        // Skip processing if no strategies are available
        if (strategies.isEmpty()) {
            return;
        }

        // Clear the spatial partitioning grid
        detector.clear();

        // Populate grid with current entities
        for (Entity entity : world.getEntities()) {
            detector.addToGrid(entity);
        }

        // Check for collisions
        Set<CollisionPair> processedPairs = new HashSet<>();

        for (Entity entity : world.getEntities()) {
            Set<Entity> potentialCollisions = detector.getPotentialCollisions(entity);

            for (Entity other : potentialCollisions) {
                if (entity == other) continue;

                CollisionPair pair = new CollisionPair(entity, other);
                if (processedPairs.contains(pair)) continue;

                if (detector.checkCollision(entity, other)) {
                    handleCollision(entity, other, world);
                    eventService.publish(new CollisionEvent(entity, entity, other));
                }

                processedPairs.add(pair);
            }
        }
    }

    private void handleCollision(Entity entity1, Entity entity2, World world) {
        for (ICollisionStrategy strategy : strategies) {
            try {
                if (strategy.canHandle(entity1, entity2)) {
                    if (strategy.handleCollision(entity1, entity2, world)) {
                        return;  // Collision handled, stop processing
                    }
                }
            } catch (Exception e) {
                System.out.println("Error in collision strategy: " + strategy.getClass().getName());
            }
        }
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
            return id1.compareTo(id2) < 0
                    ? Objects.hash(id1, id2)
                    : Objects.hash(id2, id1);
        }
    }
}