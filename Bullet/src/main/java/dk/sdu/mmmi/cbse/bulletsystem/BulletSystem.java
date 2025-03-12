// BulletSystem.java (New system to handle bullet behavior)
package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class BulletSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> bulletsToRemove = new ArrayList<>();

        // Process all entities with bullet tag
        for (Entity entity : world.getEntities()) {
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (tagComponent == null || !tagComponent.hasTag(TagComponent.TAG_BULLET)) {
                continue;
            }

            BulletComponent bulletComponent = entity.getComponent(BulletComponent.class);
            TransformComponent transform = entity.getComponent(TransformComponent.class);

            if (bulletComponent == null || transform == null) {
                continue;
            }

            // Update position based on bullet speed and direction
            processBulletMovement(transform, bulletComponent);

            // Handle bullet lifetime - reduce by 1 each frame
            bulletComponent.setRemainingLifetime(bulletComponent.getRemainingLifetime() - 1);

            // Remove bullets that are either expired or out of bounds
            if (bulletComponent.getRemainingLifetime() <= 0 || isOutOfBounds(transform, gameData)) {
                bulletsToRemove.add(entity);
            }
        }

        // Remove expired bullets
        bulletsToRemove.forEach(world::removeEntity);
    }

    private void processBulletMovement(TransformComponent transform, BulletComponent bulletComponent) {
        // Update position based on bullet speed and direction
        double radians = Math.toRadians(transform.getRotation());
        double deltaX = Math.cos(radians) * bulletComponent.getSpeed();
        double deltaY = Math.sin(radians) * bulletComponent.getSpeed();

        transform.setX(transform.getX() + deltaX);
        transform.setY(transform.getY() + deltaY);
    }

    private boolean isOutOfBounds(TransformComponent transform, GameData gameData) {
        return transform.getX() < 0
                || transform.getX() > gameData.getDisplayWidth()
                || transform.getY() < 0
                || transform.getY() > gameData.getDisplayHeight();
    }
}