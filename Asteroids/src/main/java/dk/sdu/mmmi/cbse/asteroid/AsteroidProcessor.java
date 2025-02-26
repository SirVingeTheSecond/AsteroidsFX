package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.events.AsteroidSplitEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.events.IGameEventListener;

import java.util.ServiceLoader;

public class AsteroidProcessor implements IEntityProcessingService, IAsteroidSplitter, IGameEventListener<AsteroidSplitEvent> {
    private static final int SPLIT_COUNT = 2;  // Number of new asteroids to create on split
    private final AsteroidFactory asteroidFactory;
    private final IGameEventService eventService;
    private World world;

    public AsteroidProcessor() {
        this.asteroidFactory = new AsteroidFactory();

        // Get the event service
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));

        // Register for asteroid split events
        eventService.addListener(AsteroidSplitEvent.class, this);
    }

    @Override
    public void process(GameData gameData, World world) {
        // Store reference to world for event handling
        this.world = world;

        // Asteroid-specific processing could be added here if needed
        // Basic movement is handled by MovementSystem
        // Collision is handled by CollisionSystem
    }

    @Override
    public void createSplitAsteroid(Entity asteroid, World world) {
        // Store world reference
        this.world = world;

        // Check if entity is an asteroid with necessary components
        TagComponent tagComponent = asteroid.getComponent(TagComponent.class);
        if (tagComponent == null || !tagComponent.hasTag(TagComponent.TAG_ASTEROID)) {
            return;
        }

        if (!asteroid.hasComponent(AsteroidComponent.class) ||
                !asteroid.hasComponent(TransformComponent.class)) {
            return;
        }

        AsteroidComponent asteroidComponent = asteroid.getComponent(AsteroidComponent.class);
        TransformComponent transform = asteroid.getComponent(TransformComponent.class);

        // Check if asteroid is too small to split
        if (transform.getRadius() < 10) {
            return;
        }

        // Check if asteroid can still be split (hasn't reached max splits)
        if (!asteroidComponent.canSplit()) {
            return;
        }

        // Create new asteroids
        Entity[] newAsteroids = asteroidFactory.createSplitAsteroids(asteroid, SPLIT_COUNT, null);

        // Add new asteroids to world
        for (Entity newAsteroid : newAsteroids) {
            world.addEntity(newAsteroid);
        }

        // Remove original asteroid
        world.removeEntity(asteroid);
    }

    @Override
    public void onEvent(AsteroidSplitEvent event) {
        if (world == null) {
            return; // Not initialized yet
        }

        Entity asteroid = event.getSource();
        createSplitAsteroid(asteroid, world);
    }
}