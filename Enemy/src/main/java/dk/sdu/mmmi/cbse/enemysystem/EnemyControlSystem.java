package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.components.BehaviorComponent;
import dk.sdu.mmmi.cbse.common.components.HealthComponent;
import dk.sdu.mmmi.cbse.common.components.CombatComponent;
import dk.sdu.mmmi.cbse.common.components.AIComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.ShootingComponent;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.EnemyBehavior;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.events.ShootEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {
    private final Random random = new Random();
    private static final float EDGE_MARGIN = 50.0f;
    private final IGameEventService eventService;

    public EnemyControlSystem() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public void process(GameData gameData, World world) {
        // Find player by tag component
        Entity player = findPlayerByTag(world);

        // Process all entities with enemy tag
        for (Entity entity : world.getEntities()) {
            // Skip entities without required components
            if (!hasRequiredComponents(entity)) {
                continue;
            }

            TransformComponent transform = entity.getComponent(TransformComponent.class);
            MovementComponent movement = entity.getComponent(MovementComponent.class);
            BehaviorComponent behaviorComponent = entity.getComponent(BehaviorComponent.class);
            HealthComponent health = entity.getComponent(HealthComponent.class);
            CombatComponent combat = entity.getComponent(CombatComponent.class);
            AIComponent ai = entity.getComponent(AIComponent.class);

            // Get behavior as EnemyBehavior enum
            EnemyBehavior behavior = behaviorComponent.getBehaviorAs(EnemyBehavior.class);
            if (behavior == null) {
                continue; // Skip if behavior is not an EnemyBehavior
            }

            // Check if entity should change behavior based on health
            if (health.getHealthPercentage() <= ai.getFleeThreshold() &&
                    behavior != EnemyBehavior.DEFENSIVE) {
                behaviorComponent.setBehavior(EnemyBehavior.DEFENSIVE);
                behavior = EnemyBehavior.DEFENSIVE;
            }

            // Process behavior
            switch (behavior) {
                case PATROL:
                    processPatrolBehavior(entity, transform, movement, gameData);
                    break;
                case AGGRESSIVE:
                    if (player != null) {
                        processAggressiveBehavior(entity, transform, movement, player, ai.getDetectionRange());
                    }
                    break;
                case DEFENSIVE:
                    if (player != null) {
                        processDefensiveBehavior(entity, transform, movement, player, combat.getAttackRange());
                    }
                    break;
                case SNIPER:
                    if (player != null) {
                        processSniperBehavior(entity, transform, movement, player, combat.getAttackRange(), world);
                    }
                    break;
            }

            // Check for shooting
            if (player != null && shouldShoot(entity, transform, player, combat.getAttackRange())) {
                shoot(entity, gameData, world);
            }

            // Handle screen wrapping
            handleScreenWrap(transform, gameData);
        }
    }

    private boolean hasRequiredComponents(Entity entity) {
        TagComponent tagComponent = entity.getComponent(TagComponent.class);
        if (tagComponent == null || !tagComponent.hasTag(TagComponent.TAG_ENEMY)) {
            return false;
        }

        return entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(MovementComponent.class) &&
                entity.hasComponent(BehaviorComponent.class) &&
                entity.hasComponent(HealthComponent.class) &&
                entity.hasComponent(CombatComponent.class) &&
                entity.hasComponent(AIComponent.class);
    }

    private Entity findPlayerByTag(World world) {
        for (Entity entity : world.getEntities()) {
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (tagComponent != null && tagComponent.hasTag(TagComponent.TAG_PLAYER)) {
                return entity;
            }
        }
        return null;
    }

    private void processPatrolBehavior(Entity entity, TransformComponent transform, MovementComponent movement, GameData gameData) {
        // Patrol follows a simple pattern and changes direction near edges
        if (isNearEdge(transform, gameData)) {
            // Turn to avoid edge
            double currentRotation = transform.getRotation();
            currentRotation += 90 + random.nextInt(90); // Turn 90-180 degrees
            transform.setRotation(currentRotation % 360);
        }

        // Move in current direction
        movement.setPattern(MovementComponent.MovementPattern.LINEAR);
    }

    private void processAggressiveBehavior(Entity entity, TransformComponent transform, MovementComponent movement, Entity player, float detectionRange) {
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        if (playerTransform == null) return;

        if (isInRange(transform, playerTransform, detectionRange)) {
            // Direct pursuit of player
            double angle = calculateAngleToTarget(transform, playerTransform);
            transform.setRotation(angle);
            movement.setPattern(MovementComponent.MovementPattern.LINEAR);
        } else {
            // Search pattern when player out of range
            movement.setPattern(MovementComponent.MovementPattern.RANDOM);
        }
    }

    private void processDefensiveBehavior(Entity entity, TransformComponent transform, MovementComponent movement, Entity player, float attackRange) {
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        if (playerTransform == null) return;

        float optimalRange = attackRange * 0.7f; // Stay at 70% of max range
        float currentRange = calculateDistance(transform, playerTransform);

        if (currentRange < optimalRange * 0.8f) { // Too close
            // Move away from player
            double angle = calculateAngleToTarget(transform, playerTransform);
            transform.setRotation((angle + 180) % 360); // Opposite direction
            movement.setPattern(MovementComponent.MovementPattern.LINEAR);
        } else if (currentRange > optimalRange * 1.2f) { // Too far
            // Move closer to player
            double angle = calculateAngleToTarget(transform, playerTransform);
            transform.setRotation(angle);
            movement.setPattern(MovementComponent.MovementPattern.LINEAR);
        } else {
            // At good range, strafe
            movement.setPattern(MovementComponent.MovementPattern.RANDOM);
        }
    }

    private void processSniperBehavior(Entity entity, TransformComponent transform, MovementComponent movement, Entity player, float attackRange, World world) {
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        if (playerTransform == null) return;

        float currentRange = calculateDistance(transform, playerTransform);

        if (currentRange <= attackRange && hasLineOfSight(transform, playerTransform, world)) {
            // Stop and aim at player
            movement.setSpeed(0);
            double angle = calculateAngleToTarget(transform, playerTransform);
            transform.setRotation(angle);
        } else {
            // Reposition
            CombatComponent combat = entity.getComponent(CombatComponent.class);
            movement.setSpeed(combat.getAttackSpeed());
            movement.setPattern(MovementComponent.MovementPattern.RANDOM);
        }
    }

    private boolean shouldShoot(Entity entity, TransformComponent transform, Entity player, float attackRange) {
        TransformComponent playerTransform = player.getComponent(TransformComponent.class);
        if (playerTransform == null) return false;

        // Check shooting component if available
        ShootingComponent shootingComponent = entity.getComponent(ShootingComponent.class);
        if (shootingComponent != null) {
            if (!shootingComponent.canShoot()) {
                shootingComponent.updateCooldown();
                return false;
            }
        }

        float range = calculateDistance(transform, playerTransform);
        boolean shouldShoot = range <= attackRange && random.nextFloat() < 0.1; // 10% chance per frame if in range

        if (shouldShoot && shootingComponent != null) {
            shootingComponent.resetCooldown();
        }

        return shouldShoot;
    }

    private void shoot(Entity entity, GameData gameData, World world) {
        // Instead of directly calling bullet SPI, publish an event
        eventService.publish(new ShootEvent(entity));

        // For backward compatibility until ShootSystem is implemented, still create bullet directly
        getBulletSPIs().stream().findFirst().ifPresent(
                bulletSPI -> world.addEntity(bulletSPI.createBullet(entity, gameData))
        );
    }

    private boolean isNearEdge(TransformComponent transform, GameData gameData) {
        return transform.getX() < EDGE_MARGIN
                || transform.getX() > gameData.getDisplayWidth() - EDGE_MARGIN
                || transform.getY() < EDGE_MARGIN
                || transform.getY() > gameData.getDisplayHeight() - EDGE_MARGIN;
    }

    private void handleScreenWrap(TransformComponent transform, GameData gameData) {
        float buffer = 5.0f;

        if (transform.getX() < -buffer) {
            transform.setX(gameData.getDisplayWidth() + buffer);
        } else if (transform.getX() > gameData.getDisplayWidth() + buffer) {
            transform.setX(-buffer);
        }

        if (transform.getY() < -buffer) {
            transform.setY(gameData.getDisplayHeight() + buffer);
        } else if (transform.getY() > gameData.getDisplayHeight() + buffer) {
            transform.setY(-buffer);
        }
    }

    private double calculateAngleToTarget(TransformComponent source, TransformComponent target) {
        double dx = target.getX() - source.getX();
        double dy = target.getY() - source.getY();
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    private float calculateDistance(TransformComponent t1, TransformComponent t2) {
        double dx = t2.getX() - t1.getX();
        double dy = t2.getY() - t1.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private boolean isInRange(TransformComponent source, TransformComponent target, float range) {
        return calculateDistance(source, target) <= range;
    }

    private boolean hasLineOfSight(TransformComponent source, TransformComponent target, World world) {
        // Simple line of sight check - could be enhanced with obstacle checking
        return true;
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }
}