package dk.sdu.mmmi.cbse.collisionsystem.strategy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;

import java.util.ServiceLoader;

public class AsteroidBulletStrategy implements ICollisionStrategy {
    private final IGameEventService eventService;

    public AsteroidBulletStrategy() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public boolean canHandle(Entity entity1, Entity entity2) {
        return (entity1 instanceof Asteroid && entity2 instanceof Bullet) ||
                (entity2 instanceof Asteroid && entity1 instanceof Bullet);
    }

    @Override
    public boolean handleCollision(Entity entity1, Entity entity2, World world) {
        Asteroid asteroid = (entity1 instanceof Asteroid) ? (Asteroid) entity1 : (Asteroid) entity2;
        Bullet bullet = (entity1 instanceof Bullet) ? (Bullet) entity1 : (Bullet) entity2;

        eventService.publish(new EntityDestroyedEvent(asteroid, "Asteroid destroyed by bullet"));
        world.removeEntity(asteroid);
        world.removeEntity(bullet);
        return true;
    }
}
