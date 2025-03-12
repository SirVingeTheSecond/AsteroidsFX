package dk.sdu.mmmi.cbse.enemysystem.events;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.enemy.EnemyBehavior;
import dk.sdu.mmmi.cbse.common.enemy.EnemyProperties;
import dk.sdu.mmmi.cbse.common.events.BaseGameEvent;

public class EnemySpawnedEvent extends BaseGameEvent {
    private final EnemyBehavior behavior;
    private final EnemyProperties properties;

    public EnemySpawnedEvent(Entity source, EnemyBehavior behavior, EnemyProperties properties) {
        super(source);
        this.behavior = behavior;
        this.properties = properties;
    }

    public EnemyBehavior getBehavior() { return behavior; }
    public EnemyProperties getProperties() { return properties; }
}
