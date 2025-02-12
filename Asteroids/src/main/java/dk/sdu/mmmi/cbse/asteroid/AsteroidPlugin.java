package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {
    private static final int ASTEROIDS_TO_SPAWN = 4;
    private final Random rnd = new Random();

    @Override
    public void start(GameData gameData, World world) {
        // Spawn multiple asteroids
        for (int i = 0; i < ASTEROIDS_TO_SPAWN; i++) {
            Entity asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    private Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();

        // Random size between 5 and 15
        int size = rnd.nextInt(10) + 5;
        asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);

        // Random position within game bounds
        asteroid.setX(rnd.nextDouble() * gameData.getDisplayWidth());
        asteroid.setY(rnd.nextDouble() * gameData.getDisplayHeight());

        asteroid.setRadius(size);
        asteroid.setRotation(rnd.nextInt(360)); // Random rotation

        return asteroid;
    }
}