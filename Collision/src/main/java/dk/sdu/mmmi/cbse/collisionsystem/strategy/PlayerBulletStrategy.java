package dk.sdu.mmmi.cbse.collisionsystem.strategy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;

public class PlayerBulletStrategy implements ICollisionStrategy {
    private final IGameEventService eventService;

    public PlayerBulletStrategy(IGameEventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public boolean canHandle(Entity entity1, Entity entity2) {
        return (entity1 instanceof Player && entity2 instanceof Bullet) ||
                (entity2 instanceof Player && entity1 instanceof Bullet);
    }

    @Override
    public boolean handleCollision(Entity entity1, Entity entity2, World world) {
        Player player;
        Bullet bullet;

        if (entity1 instanceof Player) {
            player = (Player) entity1;
            bullet = (Bullet) entity2;
        } else {
            player = (Player) entity2;
            bullet = (Bullet) entity1;
        }

        if (!bullet.getShooterID().equals(player.getID())) {
            eventService.publish(new EntityDestroyedEvent(player, "Player hit by bullet"));
            world.removeEntity(player);
            world.removeEntity(bullet);
            return true;
        }
        return false;
    }
}
