package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.enemy.IEnemyShip;
import dk.sdu.mmmi.cbse.common.enemy.EnemyBehavior;
import dk.sdu.mmmi.cbse.common.enemy.EnemyProperties;
import dk.sdu.mmmi.cbse.common.enemy.IEnemyEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;

public class EnemyShip extends Entity implements IEnemyShip, IEnemyEvent {
    private EnemyBehavior behavior;
    private final EnemyProperties properties;
    private final IGameEventService eventService;

    public EnemyShip(IGameEventService eventService) {
        this.eventService = eventService;
        this.properties = new EnemyProperties();
        this.behavior = EnemyBehavior.PATROL; // Default behavior
    }

    @Override
    public EnemyBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(EnemyBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public EnemyProperties getProperties() {
        return properties;
    }

    @Override
    public void onPlayerSpotted(Entity player) {
        // Change behavior when player is spotted
        if (behavior == EnemyBehavior.PATROL) {
            behavior = EnemyBehavior.AGGRESSIVE;
        }
    }

    @Override
    public void onDamageTaken(float amount) {
        properties.setHealth(properties.getHealth() - amount);

        // Change behavior when low on health
        if (properties.getHealth() < properties.getHealth() * 0.3) {
            behavior = EnemyBehavior.DEFENSIVE;
        }
    }

    @Override
    public void onDestroyed() {
        // Event handling will be done by the collision system
    }
}