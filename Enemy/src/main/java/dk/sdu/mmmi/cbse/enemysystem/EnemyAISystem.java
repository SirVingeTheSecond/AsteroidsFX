package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.AIComponent;
import dk.sdu.mmmi.cbse.common.components.BehaviorComponent;
import dk.sdu.mmmi.cbse.common.components.HealthComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.enemy.EnemyBehavior;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class EnemyAISystem implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        // Find player entity
        Entity player = findPlayerByTag(world);
        if (player == null) return;

        // Process all enemy entities
        for (Entity enemy : world.getEntities()) {
            // Skip entities without required components
            if (!isEnemy(enemy) || !hasRequiredComponents(enemy)) {
                continue;
            }

            AIComponent ai = enemy.getComponent(AIComponent.class);
            BehaviorComponent behaviorComponent = enemy.getComponent(BehaviorComponent.class);
            HealthComponent health = enemy.getComponent(HealthComponent.class);
            TransformComponent transform = enemy.getComponent(TransformComponent.class);
            TransformComponent playerTransform = player.getComponent(TransformComponent.class);

            // Skip if player has no transform
            if (playerTransform == null) continue;

            // Get behavior
            EnemyBehavior behavior = behaviorComponent.getBehaviorAs(EnemyBehavior.class);
            if (behavior == null) continue;

            // Check if entity should change behavior based on health
            if (health.getHealthPercentage() <= ai.getFleeThreshold() &&
                    behavior != EnemyBehavior.DEFENSIVE) {
                behaviorComponent.setBehavior(EnemyBehavior.DEFENSIVE);
                continue;
            }

            // Check if player is in detection range
            float distanceToPlayer = calculateDistance(transform, playerTransform);
            if (distanceToPlayer <= ai.getDetectionRange() &&
                    behavior == EnemyBehavior.PATROL) {
                behaviorComponent.setBehavior(EnemyBehavior.AGGRESSIVE);
                // Set player as target
                ai.setTarget(player);
            }
        }
    }

    private boolean isEnemy(Entity entity) {
        TagComponent tagComponent = entity.getComponent(TagComponent.class);
        return tagComponent != null && tagComponent.hasTag(TagComponent.TAG_ENEMY);
    }

    private boolean hasRequiredComponents(Entity entity) {
        return entity.hasComponent(AIComponent.class) &&
                entity.hasComponent(BehaviorComponent.class) &&
                entity.hasComponent(HealthComponent.class) &&
                entity.hasComponent(TransformComponent.class);
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

    private float calculateDistance(TransformComponent t1, TransformComponent t2) {
        double dx = t2.getX() - t1.getX();
        double dy = t2.getY() - t1.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}