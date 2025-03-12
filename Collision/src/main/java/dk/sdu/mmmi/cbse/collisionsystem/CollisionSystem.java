package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionPair;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.ICollisionService;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * System that handles collision detection and resolution.
 * Implements separation of concerns between detection and resolution.
 */
public class CollisionSystem implements IPostEntityProcessingService, ICollisionService {
    private static final Logger LOGGER = Logger.getLogger(CollisionSystem.class.getName());

    private final CollisionDetector detector;
    private final CollisionResolver resolver;
    private final IGameEventService eventService;

    private boolean debugEnabled = false;

    public CollisionSystem() {
        // Load required services
        this.eventService = ServiceLocator.getService(IGameEventService.class);

        // Initialize collision components
        this.detector = new CollisionDetector();
        this.resolver = new CollisionResolver(eventService);
    }

    @Override
    public void process(GameData gameData, World world) {
        long startTime = System.nanoTime(); // Just call getTime?

        // Clear the detector for this frame
        detector.clear();

        // Add all entities with collision components to the detector
        for (Entity entity : world.getEntities()) {
            CollisionComponent cc = entity.getComponent(CollisionComponent.class);
            if (cc != null && cc.isActive()) {
                detector.addEntity(entity);
            }
        }

        // Detect all collisions
        Set<CollisionPair> collisions = detector.detectCollisions();

        // Resolve the collisions
        resolver.resolveCollisions(collisions, world);

        long endTime = System.nanoTime(); // Just call getTime?

        if (debugEnabled) {
            LOGGER.log(Level.INFO,
                    "Collision processing: {0} entities, {1} collisions, {2}ms",
                    new Object[]{
                            world.getEntities().size(),
                            collisions.size(),
                            (endTime - startTime) / 1_000_000.0
                    });
        }
    }

    @Override
    public void setDebugEnabled(boolean enabled) {
        this.debugEnabled = enabled;
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}