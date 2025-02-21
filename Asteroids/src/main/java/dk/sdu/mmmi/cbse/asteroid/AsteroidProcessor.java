package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.Random;

public class AsteroidProcessor implements IEntityProcessingService, IAsteroidSplitter {
    private static final float MIN_SPLIT_SIZE = 10;  // Minimum size for an asteroid to split
    private static final float SPLIT_SIZE_RATIO = 0.5f;  // New asteroids are 50% of original size
    private final Random rnd = new Random();

    @Override
    public void process(GameData gameData, World world) {
        // Asteroid-specific processing could be added here
        // Basic movement and collision are handled by their respective systems
    }

    @Override
    public void createSplitAsteroid(Entity asteroid, World world) {
        if (!(asteroid instanceof Asteroid) || asteroid.getRadius() < MIN_SPLIT_SIZE) {
            return;
        }

        float newSize = asteroid.getRadius() * SPLIT_SIZE_RATIO;
        MovementComponent oldMovement = asteroid.getComponent(MovementComponent.class);

        // Create two new smaller asteroids
        for (int i = 0; i < 2; i++) {
            Asteroid newAsteroid = new Asteroid();

            // Set size and shape
            newAsteroid.setRadius(newSize);
            newAsteroid.setPolygonCoordinates(generateAsteroidShape(newSize));

            // Position slightly offset from original
            double angle = rnd.nextDouble() * 2 * Math.PI;
            double offset = asteroid.getRadius();
            newAsteroid.setX(asteroid.getX() + Math.cos(angle) * offset);
            newAsteroid.setY(asteroid.getY() + Math.sin(angle) * offset);

            // Set split count
            Asteroid oldAsteroid = (Asteroid) asteroid;
            newAsteroid.setSplitCount(oldAsteroid.getSplitCount() + 1);

            // Setup movement with some inherited momentum
            MovementComponent movement = new MovementComponent();
            movement.setPattern(MovementComponent.MovementPattern.RANDOM);
            if (oldMovement != null) {
                movement.setSpeed(oldMovement.getSpeed() * (0.8f + rnd.nextFloat() * 0.4f));
            } else {
                movement.setSpeed(1.0f + rnd.nextFloat());
            }
            movement.setRotationSpeed(-2 + rnd.nextFloat() * 4);
            newAsteroid.addComponent(movement);

            // Add to world
            world.addEntity(newAsteroid);
        }

        // Remove original asteroid
        world.removeEntity(asteroid);
    }

    private double[] generateAsteroidShape(float size) {
        int vertices = 8;
        double[] shape = new double[vertices * 2];
        double angleStep = 360.0 / vertices;

        for (int i = 0; i < vertices; i++) {
            double angle = Math.toRadians(i * angleStep);
            // Randomize radius slightly for more natural shape
            double radius = size * (0.8 + rnd.nextDouble() * 0.4);
            shape[i * 2] = Math.cos(angle) * radius;
            shape[i * 2 + 1] = Math.sin(angle) * radius;
        }

        return shape;
    }
}