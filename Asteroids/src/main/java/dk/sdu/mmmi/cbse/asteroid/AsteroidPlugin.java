package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

/**
 * Plugin for managing asteroids in the game.
 */
public class AsteroidPlugin implements IGamePluginService {
    private static final int ASTEROIDS_TO_SPAWN = 4;
    private final AsteroidFactory asteroidFactory;

    public AsteroidPlugin() {
        this.asteroidFactory = new AsteroidFactory();
    }

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("AsteroidPlugin.start() called - spawning " + ASTEROIDS_TO_SPAWN + " asteroids");

        // Spawn initial asteroids
        for (int i = 0; i < ASTEROIDS_TO_SPAWN; i++) {
            Entity asteroid = asteroidFactory.createEntity(gameData);
            world.addEntity(asteroid);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("AsteroidPlugin.stop() called - removing all asteroids");
        // Remove all asteroids
        for (Entity entity : world.getEntities()) {
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (tagComponent != null && tagComponent.hasTag(TagComponent.TAG_ASTEROID)) {
                world.removeEntity(entity);
            }
        }
    }
}