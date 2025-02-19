package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.collisionsystem.collision.CollisionDetector;
import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionResponseComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.events.CollisionEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.*;

public class CollisionSystem implements IPostEntityProcessingService {
    private final CollisionDetector detector;
    private final IGameEventService eventService;

    public CollisionSystem() {
        this.detector = new CollisionDetector();

        // Load required services
        ServiceLoader<IGameEventService> eventLoader = ServiceLoader.load(IGameEventService.class);
        this.eventService = eventLoader.findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public void process(GameData gameData, World world) {
        // Clear the spatial partitioning grid
        detector.clear();

        // Get all entities with collision components
        List<Entity> collidableEntities = new ArrayList<>();
        for (Entity entity : world.getEntities()) {
            CollisionComponent cc = entity.getComponent(CollisionComponent.class);
            if (cc != null && cc.isActive()) {
                collidableEntities.add(entity);
                detector.addToGrid(entity);
            }
        }

        // Check for collisions
        Set<CollisionPair> processedPairs = new HashSet<>();

        for (Entity entity : collidableEntities) {
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
        CollisionResponseComponent response1 = entity1.getComponent(CollisionResponseComponent.class);
        CollisionResponseComponent response2 = entity2.getComponent(CollisionResponseComponent.class);

        // Handle responses in order if present
        if (response1 != null) {
            if (response1.handleCollision(entity1, entity2, world)) {
                return;
            }
        }

        if (response2 != null) {
            response2.handleCollision(entity2, entity1, world);
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