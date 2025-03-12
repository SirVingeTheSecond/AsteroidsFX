package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.Vector2D;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionGroup;
import dk.sdu.mmmi.cbse.common.collision.CollisionLayer;
import dk.sdu.mmmi.cbse.common.components.ShootingComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.util.EntityBuilder;

/**
 * Factory for creating bullet entities using the EntityBuilder pattern.
 */
public class BulletFactory implements BulletSPI {

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        // Get shooter components
        TransformComponent shooterTransform = shooter.getComponent(TransformComponent.class);
        ShootingComponent shootingComponent = shooter.getComponent(ShootingComponent.class);
        TagComponent shooterTag = shooter.getComponent(TagComponent.class);

        if (shooterTransform == null) {
            return null; // Can't create bullet without shooter position
        }

        // Calculate spawn position in front of the shooter
        float spawnDistance = shooterTransform.getRadius() + 5;
        Vector2D spawnPosition = shooterTransform.getPosition().add(
                shooterTransform.getForward().scale(spawnDistance));

        // Determine bullet properties based on shooter type
        boolean isPlayerBullet = shooterTag != null && shooterTag.hasType(EntityType.PLAYER);

        // Create bullet component with appropriate properties
        BulletComponent bulletComponent = new BulletComponent();
        bulletComponent.setType(isPlayerBullet ? BulletComponent.BulletType.PLAYER : BulletComponent.BulletType.ENEMY);
        bulletComponent.setShooterID(shooter.getID());

        // If shooter has shooting component, use its properties
        if (shootingComponent != null) {
            bulletComponent.setSpeed(shootingComponent.getProjectileSpeed());
            bulletComponent.setLifetime(shootingComponent.getProjectileLifetime());
            bulletComponent.setDamage(shootingComponent.getDamage());
        }

        // Create collision component
        CollisionComponent collision = new CollisionComponent();
        collision.setLayer(CollisionLayer.PROJECTILE);

        // Set collision group based on shooter type
        if (isPlayerBullet) {
            collision.addGroup(CollisionGroup.FRIENDLY);
        } else {
            collision.addGroup(CollisionGroup.HOSTILE);
        }

        // Build the bullet entity using EntityBuilder
        return EntityBuilder.create()
                .withType(EntityType.BULLET)
                .atPosition(spawnPosition)
                .withRotation(shooterTransform.getRotation())
                .withShape(2, -2, 2, 2, -2, 2, -2, -2)
                .withRadius(2)
                .with(bulletComponent)
                .with(collision)
                .build();
    }
}