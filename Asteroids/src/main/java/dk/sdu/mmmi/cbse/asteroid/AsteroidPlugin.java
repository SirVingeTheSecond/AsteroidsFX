package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionLayer;
import dk.sdu.mmmi.cbse.common.collision.CollisionGroup;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {
    private static final int ASTEROIDS_TO_SPAWN = 4;
    private static final float MIN_SPEED = 0.5f;
    private static final float MAX_SPEED = 1.5f;
    private static final float MIN_ROTATION_SPEED = -2.0f;
    private static final float MAX_ROTATION_SPEED = 2.0f;

    private final Random rnd = new Random();

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("AsteroidPlugin.start() called - spawning " + ASTEROIDS_TO_SPAWN + " asteroids");

        // Spawn initial asteroids
        for (int i = 0; i < ASTEROIDS_TO_SPAWN; i++) {
            Entity asteroid = createAsteroid(gameData);
            world.addEntity(asteroid);
            System.out.println("Created asteroid at position: " + asteroid.getX() + "," + asteroid.getY());
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("AsteroidPlugin.stop() called - removing all asteroids");
        // Remove all asteroids
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    private Entity createAsteroid(GameData gameData) {
        Asteroid asteroid = new Asteroid();

        // Initialize movement component
        MovementComponent movement = new MovementComponent();
        movement.setPattern(MovementComponent.MovementPattern.RANDOM);
        movement.setSpeed(MIN_SPEED + rnd.nextFloat() * (MAX_SPEED - MIN_SPEED));
        movement.setRotationSpeed(MIN_ROTATION_SPEED + rnd.nextFloat() * (MAX_ROTATION_SPEED - MIN_ROTATION_SPEED));
        asteroid.addComponent(movement);

        // Initialize collision component
        CollisionComponent collision = new CollisionComponent();
        collision.setLayer(CollisionLayer.OBSTACLE);
        collision.addGroup(CollisionGroup.SOLID);
        collision.addGroup(CollisionGroup.DESTRUCTIBLE);
        asteroid.addComponent(collision);

        // Set basic entity properties
        float size = rnd.nextInt(10) + 20; // Made asteroids bigger for easier visibility
        asteroid.setRadius(size);
        asteroid.setPolygonCoordinates(generateAsteroidShape(size));

        // Random starting position (ensuring they're visible)
        asteroid.setX(100 + rnd.nextDouble() * (gameData.getDisplayWidth() - 200));  // Keep away from edges
        asteroid.setY(100 + rnd.nextDouble() * (gameData.getDisplayHeight() - 200)); // Keep away from edges
        asteroid.setRotation(rnd.nextInt(360));

        // Initialize split count
        asteroid.setSplitCount(0);

        return asteroid;
    }

    private double[] generateAsteroidShape(float size) {
        int vertices = 8;
        double[] shape = new double[vertices * 2];
        double angleStep = 360.0 / vertices;

        for (int i = 0; i < vertices; i++) {
            double angle = Math.toRadians(i * angleStep);
            double radius = size * (0.8 + rnd.nextDouble() * 0.4);
            shape[i * 2] = Math.cos(angle) * radius;
            shape[i * 2 + 1] = Math.sin(angle) * radius;
        }

        return shape;
    }
}