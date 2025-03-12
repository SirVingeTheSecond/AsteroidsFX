package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.ShootingComponent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

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
            double radians = Math.toRadians(transform.getRotation());
            double deltaX = Math.cos(radians) * bulletComponent.getSpeed();
            double deltaY = Math.sin(radians) * bulletComponent.getSpeed();

            transform.setX(transform.getX() + deltaX);
            transform.setY(transform.getY() + deltaY);

            // Handle bullet lifetime - reduce by 1 each frame
            bulletComponent.reduceLifetime();

            // Remove bullets that are either expired or out of bounds
            if (bulletComponent.getRemainingLifetime() <= 0 || isOutOfBounds(transform, gameData)) {
                bulletsToRemove.add(entity);
            }
        }

        // Remove expired bullets
        bulletsToRemove.forEach(world::removeEntity);
    }

    private boolean isOutOfBounds(TransformComponent transform, GameData gameData) {
        return transform.getX() < 0
                || transform.getX() > gameData.getDisplayWidth()
                || transform.getY() < 0
                || transform.getY() > gameData.getDisplayHeight();
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Entity();

        // Get shooter components
        TransformComponent shooterTransform = shooter.getComponent(TransformComponent.class);
        ShootingComponent shootingComponent = shooter.getComponent(ShootingComponent.class);
        TagComponent shooterTag = shooter.getComponent(TagComponent.class);

        if (shooterTransform == null) {
            return null; // Can't create bullet without shooter position
        }

        // Add transform component
        TransformComponent transform = new TransformComponent();
        transform.setPolygonCoordinates(2, -2, 2, 2, -2, 2, -2, -2);
        transform.setRadius(2);

        // Calculate spawn position in front of the shooter
        double direction = Math.toRadians(shooterTransform.getRotation());
        double spawnDistance = shooterTransform.getRadius() + 5;
        double startX = shooterTransform.getX() + Math.cos(direction) * spawnDistance;
        double startY = shooterTransform.getY() + Math.sin(direction) * spawnDistance;

        transform.setX(startX);
        transform.setY(startY);
        transform.setRotation(shooterTransform.getRotation());
        bullet.addComponent(transform);

        // Create bullet component with appropriate properties
        BulletComponent bulletComponent = new BulletComponent();

        // Set bullet properties based on shooter type
        if (shooterTag != null && shooterTag.hasTag(TagComponent.TAG_PLAYER)) {
            bulletComponent.setType(BulletComponent.BulletType.PLAYER);
        } else {
            bulletComponent.setType(BulletComponent.BulletType.ENEMY);
        }

        // If shooter has shooting component, use its properties
        if (shootingComponent != null) {
            bulletComponent.setSpeed(shootingComponent.getProjectileSpeed());
            bulletComponent.setLifetime(shootingComponent.getProjectileLifetime());
        }

        bulletComponent.setShooterID(shooter.getID());
        bullet.addComponent(bulletComponent);

        // Add tag component
        bullet.addComponent(new TagComponent(TagComponent.TAG_BULLET));

        // Add collision component
        // (This would typically be added here but depends on your collision system implementation)

        return bullet;
    }
}