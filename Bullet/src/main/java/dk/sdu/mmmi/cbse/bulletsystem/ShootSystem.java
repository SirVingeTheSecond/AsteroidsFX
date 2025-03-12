// ShootSystem.java (Improved)
package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.events.IGameEventListener;
import dk.sdu.mmmi.cbse.common.events.ShootEvent;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ServiceLoader;

/**
 * System that responds to ShootEvents by creating bullets.
 */
public class ShootSystem implements IEntityProcessingService, IGameEventListener<ShootEvent> {
    private final BulletSPI bulletSPI;
    private final IGameEventService eventService;
    private World world;
    private GameData gameData;

    public ShootSystem() {
        // Get required services
        this.bulletSPI = ServiceLoader.load(BulletSPI.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No BulletSPI implementation found"));

        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));

        // Register for ShootEvents
        eventService.addListener(ShootEvent.class, this);
    }

    @Override
    public void process(GameData gameData, World world) {
        // Store references for use in event handling
        this.world = world;
        this.gameData = gameData;
    }

    @Override
    public void onEvent(ShootEvent event) {
        if (world == null || gameData == null) {
            return; // Not initialized yet
        }

        Entity shooter = event.getSource();
        if (shooter == null) {
            return; // Invalid event
        }

        // Create a bullet based on the shooter
        Entity bullet = createBullet(shooter, event);
        if (bullet != null) {
            world.addEntity(bullet);
        }
    }

    private Entity createBullet(Entity shooter, ShootEvent event) {
        // Create bullet using BulletSPI
        Entity bullet = bulletSPI.createBullet(shooter, gameData);
        if (bullet == null) return null;

        // If the event has a direction override, apply it
        if (event.hasDirectionOverride()) {
            TransformComponent bulletTransform = bullet.getComponent(TransformComponent.class);
            if (bulletTransform != null) {
                bulletTransform.setRotation(event.getDirection());
            }
        }

        return bullet;
    }
}