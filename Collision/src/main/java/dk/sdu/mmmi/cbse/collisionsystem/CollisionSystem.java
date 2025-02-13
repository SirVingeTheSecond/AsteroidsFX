package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.collisionsystem.collision.CollisionDetector;
import dk.sdu.mmmi.cbse.collisionsystem.strategy.*;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.CollisionEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.*;
import java.util.ServiceLoader;

public class CollisionSystem implements IPostEntityProcessingService {
    private final CollisionDetector detector;
    private final List<ICollisionStrategy> strategies;
    private final IGameEventService eventService;

    public CollisionSystem() {
        this.detector = new CollisionDetector();
        this.strategies = new ArrayList<>();

        // Load required services
        ServiceLoader<IGameEventService> eventLoader = ServiceLoader.load(IGameEventService.class);
        this.eventService = eventLoader.findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));

        // Initialize strategies
        initializeStrategies();
    }

    private void initializeStrategies() {
        strategies.add(new PlayerBulletStrategy(eventService));
        strategies.add(new PlayerAsteroidStrategy(eventService));
        strategies.add(new PlayerEnemyStrategy(eventService));
        strategies.add(new EnemyBulletStrategy(eventService));
        strategies.add(new EnemyAsteroidStrategy(eventService));

        // Only try to create AsteroidBulletStrategy if we need it
        try {
            strategies.add(new AsteroidBulletStrategy(eventService));
        } catch (RuntimeException e) {
            // Log warning but continue - asteroid splitting won't work but game can continue
            System.out.println("Warning: AsteroidBulletStrategy could not be initialized: " + e.getMessage());
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        // Clear the spatial partitioning grid
        detector.clear();

        // Populate grid
        for (Entity entity : world.getEntities()) {
            detector.addToGrid(entity);
        }

        // Check collisions
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
            if (strategy.canHandle(entity1, entity2)) {
                if (strategy.handleCollision(entity1, entity2, world)) {
                    break;  // Collision handled, stop processing
                }
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
