package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.MovementComponent;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.EnemyBehavior;
import dk.sdu.mmmi.cbse.common.enemy.IEnemyShip;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {
    private final Random random = new Random();
    private static final float EDGE_MARGIN = 50.0f;

    @Override
    public void process(GameData gameData, World world) {
        // Get player position for targeting
        Entity player = findPlayer(world);

        for (Entity entity : world.getEntities(EnemyShip.class)) {
            IEnemyShip enemy = (IEnemyShip) entity;
            MovementComponent movement = entity.getComponent(MovementComponent.class);

            if (movement == null) continue;

            // Process behavior
            switch (enemy.getBehavior()) {
                case PATROL:
                    processPatrolBehavior(entity, movement, gameData);
                    break;
                case AGGRESSIVE:
                    if (player != null) {
                        processAggressiveBehavior(entity, movement, player, enemy.getProperties().getDetectionRange());
                    }
                    break;
                case DEFENSIVE:
                    if (player != null) {
                        processDefensiveBehavior(entity, movement, player, enemy.getProperties());
                    }
                    break;
                case SNIPER:
                    if (player != null) {
                        processSniperBehavior(entity, movement, player, enemy.getProperties(), world);
                    }
                    break;
            }

            // Check for shooting
            if (shouldShoot(entity, player, enemy)) {
                shoot(entity, gameData, world);
            }

            // Handle screen wrapping
            handleScreenWrap(entity, gameData);
        }
    }

    private Entity findPlayer(World world) {
        // Find first player entity - could be enhanced to handle multiple players
        return world.getEntities().stream()
                .filter(e -> e.getClass().getSimpleName().equals("Player"))
                .findFirst()
                .orElse(null);
    }

    private void processPatrolBehavior(Entity entity, MovementComponent movement, GameData gameData) {
        // Patrol follows a simple pattern and changes direction near edges
        if (isNearEdge(entity, gameData)) {
            // Turn to avoid edge
            double currentRotation = entity.getRotation();
            currentRotation += 90 + random.nextInt(90); // Turn 90-180 degrees
            entity.setRotation(currentRotation % 360);
        }

        // Move in current direction
        movement.setPattern(MovementComponent.MovementPattern.LINEAR);
    }

    private void processAggressiveBehavior(Entity entity, MovementComponent movement, Entity player, float detectionRange) {
        if (isInRange(entity, player, detectionRange)) {
            // Direct pursuit of player
            double angle = calculateAngleToTarget(entity, player);
            entity.setRotation(angle);
            movement.setPattern(MovementComponent.MovementPattern.LINEAR);
        } else {
            // Search pattern when player out of range
            movement.setPattern(MovementComponent.MovementPattern.RANDOM);
        }
    }

    private void processDefensiveBehavior(Entity entity, MovementComponent movement, Entity player,
                                          dk.sdu.mmmi.cbse.common.enemy.EnemyProperties properties) {
        float optimalRange = properties.getShootingRange() * 0.7f; // Stay at 70% of max range
        float currentRange = calculateDistance(entity, player);

        if (currentRange < optimalRange * 0.8f) { // Too close
            // Move away from player
            double angle = calculateAngleToTarget(entity, player);
            entity.setRotation((angle + 180) % 360); // Opposite direction
            movement.setPattern(MovementComponent.MovementPattern.LINEAR);
        } else if (currentRange > optimalRange * 1.2f) { // Too far
            // Move closer to player
            double angle = calculateAngleToTarget(entity, player);
            entity.setRotation(angle);
            movement.setPattern(MovementComponent.MovementPattern.LINEAR);
        } else {
            // At good range, strafe
            movement.setPattern(MovementComponent.MovementPattern.RANDOM);
        }
    }

    private void processSniperBehavior(Entity entity, MovementComponent movement, Entity player,
                                       dk.sdu.mmmi.cbse.common.enemy.EnemyProperties properties, World world) {
        float currentRange = calculateDistance(entity, player);

        if (currentRange <= properties.getShootingRange()
                && hasLineOfSight(entity, player, world)) {
            // Stop and aim at player
            movement.setSpeed(0);
            double angle = calculateAngleToTarget(entity, player);
            entity.setRotation(angle);
        } else {
            // Reposition
            movement.setSpeed(properties.getSpeed());
            movement.setPattern(MovementComponent.MovementPattern.RANDOM);
        }
    }

    private boolean shouldShoot(Entity entity, Entity player, IEnemyShip enemy) {
        if (player == null) return false;

        float range = calculateDistance(entity, player);
        return range <= enemy.getProperties().getShootingRange()
                && random.nextFloat() < 0.1; // 10% chance per frame if in range
    }

    private void shoot(Entity entity, GameData gameData, World world) {
        getBulletSPIs().stream().findFirst().ifPresent(
                bulletSPI -> world.addEntity(bulletSPI.createBullet(entity, gameData))
        );
    }

    private boolean isNearEdge(Entity entity, GameData gameData) {
        return entity.getX() < EDGE_MARGIN
                || entity.getX() > gameData.getDisplayWidth() - EDGE_MARGIN
                || entity.getY() < EDGE_MARGIN
                || entity.getY() > gameData.getDisplayHeight() - EDGE_MARGIN;
    }

    private void handleScreenWrap(Entity entity, GameData gameData) {
        if (entity.getX() < 0) entity.setX(gameData.getDisplayWidth());
        if (entity.getX() > gameData.getDisplayWidth()) entity.setX(0);
        if (entity.getY() < 0) entity.setY(gameData.getDisplayHeight());
        if (entity.getY() > gameData.getDisplayHeight()) entity.setY(0);
    }

    private double calculateAngleToTarget(Entity source, Entity target) {
        double dx = target.getX() - source.getX();
        double dy = target.getY() - source.getY();
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    private float calculateDistance(Entity e1, Entity e2) {
        double dx = e2.getX() - e1.getX();
        double dy = e2.getY() - e1.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private boolean isInRange(Entity source, Entity target, float range) {
        return calculateDistance(source, target) <= range;
    }

    private boolean hasLineOfSight(Entity source, Entity target, World world) {
        // Simple line of sight check - could be enhanced with obstacle checking
        return true;
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }
}