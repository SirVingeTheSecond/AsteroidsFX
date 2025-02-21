package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.BehaviorComponent;
import dk.sdu.mmmi.cbse.common.components.HealthComponent;
import dk.sdu.mmmi.cbse.common.components.CombatComponent;
import dk.sdu.mmmi.cbse.common.components.AIComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.enemy.EnemyBehavior;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;

/**
 * Factory for creating enemy entities.
 * This makes the EnemyShip class redundant as we just create standard entities with components representing an enemy.
 */
public class EnemyFactory {
    private final IGameEventService eventService;

    public EnemyFactory(IGameEventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Create a standard enemy entity with all required components
     * @param health Initial health value
     * @param damage Damage value
     * @param behavior Initial behavior type
     * @return A configured enemy entity
     */
    public Entity createEnemy(float health, float damage, EnemyBehavior behavior) {
        Entity enemy = new Entity();

        // Add required components
        enemy.addComponent(new TransformComponent());
        enemy.addComponent(new BehaviorComponent(behavior));
        enemy.addComponent(new HealthComponent(health));

        CombatComponent combat = new CombatComponent();
        combat.setDamage(damage);
        combat.setAttackRange(200f);
        combat.setScoreValue(100);
        enemy.addComponent(combat);

        AIComponent ai = new AIComponent();
        ai.setDetectionRange(300f);
        ai.setFleeThreshold(0.3f);
        enemy.addComponent(ai);

        // Add tag to identify as enemy
        enemy.addComponent(new TagComponent(TagComponent.TAG_ENEMY));

        return enemy;
    }

    /**
     * Create a patrol enemy type
     */
    public Entity createPatrolEnemy() {
        Entity enemy = createEnemy(50f, 10f, EnemyBehavior.PATROL);
        CombatComponent combat = enemy.getComponent(CombatComponent.class);
        combat.setAttackRange(150f);
        combat.setScoreValue(100);
        return enemy;
    }

    /**
     * Create an aggressive enemy type
     */
    public Entity createAggressiveEnemy() {
        Entity enemy = createEnemy(75f, 15f, EnemyBehavior.AGGRESSIVE);
        CombatComponent combat = enemy.getComponent(CombatComponent.class);
        combat.setAttackRange(100f);
        combat.setScoreValue(150);

        AIComponent ai = enemy.getComponent(AIComponent.class);
        ai.setDetectionRange(300f);
        return enemy;
    }

    /**
     * Create a sniper enemy type
     */
    public Entity createSniperEnemy() {
        Entity enemy = createEnemy(40f, 20f, EnemyBehavior.SNIPER);
        CombatComponent combat = enemy.getComponent(CombatComponent.class);
        combat.setAttackRange(400f);
        combat.setScoreValue(200);

        AIComponent ai = enemy.getComponent(AIComponent.class);
        ai.setDetectionRange(500f);
        return enemy;
    }
}
