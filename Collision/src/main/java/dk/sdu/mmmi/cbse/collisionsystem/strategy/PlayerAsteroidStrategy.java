package dk.sdu.mmmi.cbse.collisionsystem.strategy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.playersystem.Player;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.events.EntityDestroyedEvent;

public class PlayerAsteroidStrategy implements ICollisionStrategy {
    private final IGameEventService eventService;

    public PlayerAsteroidStrategy(IGameEventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public boolean canHandle(Entity entity1, Entity entity2) {
        return (entity1 instanceof Player && entity2 instanceof Asteroid) ||
                (entity2 instanceof Player && entity1 instanceof Asteroid);
    }

    @Override
    public boolean handleCollision(Entity entity1, Entity entity2, World world) {
        Player player = (entity1 instanceof Player) ? (Player) entity1 : (Player) entity2;
        Asteroid asteroid = (entity1 instanceof Asteroid) ? (Asteroid) entity1 : (Asteroid) entity2;

        eventService.publish(new EntityDestroyedEvent(player, "Player destroyed by asteroid"));
        world.removeEntity(player);
        return true;
    }
}
