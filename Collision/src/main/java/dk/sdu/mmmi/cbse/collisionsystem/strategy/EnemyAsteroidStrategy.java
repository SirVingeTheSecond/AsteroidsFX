package dk.sdu.mmmi.cbse.collisionsystem.strategy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.enemysystem.EnemyShip;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;

public class EnemyAsteroidStrategy implements ICollisionStrategy {
    private final IGameEventService eventService;

    public EnemyAsteroidStrategy(IGameEventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public boolean canHandle(Entity entity1, Entity entity2) {
        return (entity1 instanceof EnemyShip && entity2 instanceof Asteroid) ||
                (entity2 instanceof EnemyShip && entity1 instanceof Asteroid);
    }

    @Override
    public boolean handleCollision(Entity entity1, Entity entity2, World world) {
        EnemyShip enemy = (entity1 instanceof EnemyShip) ? (EnemyShip) entity1 : (EnemyShip) entity2;
        Asteroid asteroid = (entity1 instanceof Asteroid) ? (Asteroid) entity1 : (Asteroid) entity2;

        eventService.publish(new EntityDestroyedEvent(enemy, "Enemy destroyed by asteroid"));
        world.removeEntity(enemy);
        return true;
    }
}