package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Collision event between two entities
 */
public class CollisionEvent extends BaseGameEvent {
    private final Entity entity1;
    private final Entity entity2;

    public CollisionEvent(Entity source, Entity entity1, Entity entity2) {
        super(source);
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }
}