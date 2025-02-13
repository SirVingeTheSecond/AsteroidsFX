package dk.sdu.mmmi.cbse.collisionsystem.strategy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.enemysystem.EnemyShip;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;

import java.util.ServiceLoader;

public class PlayerEnemyStrategy implements ICollisionStrategy {
    private final IGameEventService eventService;

    public PlayerEnemyStrategy() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public boolean canHandle(Entity entity1, Entity entity2) {
        return (entity1 instanceof Player && entity2 instanceof EnemyShip) ||
                (entity2 instanceof Player && entity1 instanceof EnemyShip);
    }

    @Override
    public boolean handleCollision(Entity entity1, Entity entity2, World world) {
        Player player = (entity1 instanceof Player) ? (Player) entity1 : (Player) entity2;
        EnemyShip enemy = (entity1 instanceof EnemyShip) ? (EnemyShip) entity1 : (EnemyShip) entity2;

        eventService.publish(new EntityDestroyedEvent(player, "Player destroyed by enemy ship collision"));
        eventService.publish(new EntityDestroyedEvent(enemy, "Enemy destroyed by player collision"));

        world.removeEntity(player);
        world.removeEntity(enemy);
        return true;
    }
}
