package dk.sdu.mmmi.cbse.collisionsystem.strategy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.enemysystem.EnemyShip;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;

import java.util.ServiceLoader;

public class EnemyBulletStrategy implements ICollisionStrategy {
    private final IGameEventService eventService;

    public EnemyBulletStrategy() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public boolean canHandle(Entity entity1, Entity entity2) {
        return (entity1 instanceof EnemyShip && entity2 instanceof Bullet) ||
                (entity2 instanceof EnemyShip && entity1 instanceof Bullet);
    }

    @Override
    public boolean handleCollision(Entity entity1, Entity entity2, World world) {
        EnemyShip enemy = (entity1 instanceof EnemyShip) ? (EnemyShip) entity1 : (EnemyShip) entity2;
        Bullet bullet = (entity1 instanceof Bullet) ? (Bullet) entity1 : (Bullet) entity2;

        if (!bullet.getShooterID().equals(enemy.getID())) {
            eventService.publish(new EntityDestroyedEvent(enemy, "Enemy destroyed by bullet"));
            world.removeEntity(enemy);
            world.removeEntity(bullet);
            return true;
        }
        return false;
    }
}
