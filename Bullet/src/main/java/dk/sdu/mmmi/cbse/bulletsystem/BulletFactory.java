package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.ShootingComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionLayer;
import dk.sdu.mmmi.cbse.common.collision.CollisionGroup;

/**
 * Factory for creating bullet entities.
 */
public class BulletFactory {

    /**
     * Creates a bullet entity based on a shooter
     */
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Entity();

        // Get shooter components
        TransformComponent shooterTransform = shooter.getComponent(TransformComponent.class);
        ShootingComponent shootingComponent = shooter.getComponent(ShootingComponent.class);
        TagComponent shooterTag = shooter.getComponent(TagComponent.class);

        if (shooterTransform == null) {
            return null; // Can't create bullet without shooter position
        }

        // Set up transform component
        TransformComponent transform = bullet.getComponent(TransformComponent.class);
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
            bulletComponent.setDamage(shootingComponent.getDamage());
        }

        bulletComponent.setShooterID(shooter.getID());
        bullet.addComponent(bulletComponent);

        // Add collision component
        CollisionComponent collision = new CollisionComponent();
        collision.setLayer(CollisionLayer.PROJECTILE);

        // Set collision group based on shooter type
        if (shooterTag != null && shooterTag.hasTag(TagComponent.TAG_PLAYER)) {
            collision.addGroup(CollisionGroup.FRIENDLY);
        } else {
            collision.addGroup(CollisionGroup.HOSTILE);
        }

        bullet.addComponent(collision);

        // Add tag component
        bullet.addComponent(new TagComponent(TagComponent.TAG_BULLET));

        return bullet;
    }
}