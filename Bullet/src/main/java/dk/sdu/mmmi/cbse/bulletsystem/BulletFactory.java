package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionLayer;
import dk.sdu.mmmi.cbse.common.collision.CollisionGroup;
import dk.sdu.mmmi.cbse.common.services.IEntityFactory;

/**
 * Factory for creating bullet entities.
 * Replaces direct Bullet class instantiation with component-based approach.
 */
public class BulletFactory implements IEntityFactory<Entity> {

    /**
     * Creates a basic bullet entity with default properties.
     */
    @Override
    public Entity createEntity(GameData gameData) {
        Entity bullet = new Entity();

        // Add transform component
        TransformComponent transform = new TransformComponent();
        transform.setPolygonCoordinates(2, -2, 2, 2, -2, 2, -2, -2);
        transform.setRadius(2);
        bullet.addComponent(transform);

        // Add bullet component with default properties
        BulletComponent bulletComponent = new BulletComponent();
        bulletComponent.setType(BulletComponent.BulletType.PLAYER);
        bulletComponent.setSpeed(2.0f);
        bulletComponent.setLifetime(600); // 10 seconds at 60 FPS
        bullet.addComponent(bulletComponent);

        // Add collision component
        CollisionComponent collision = new CollisionComponent();
        collision.setLayer(CollisionLayer.PROJECTILE);
        // Collision group will be set based on shooter type
        bullet.addComponent(collision);

        // Add tag component
        bullet.addComponent(new TagComponent(TagComponent.TAG_BULLET));

        return bullet;
    }

    /**
     * Creates a player bullet.
     */
    public Entity createPlayerBullet(double x, double y, double rotation) {
        Entity bullet = createEntity(null);

        // Set position and rotation
        TransformComponent transform = bullet.getComponent(TransformComponent.class);
        transform.setX(x);
        transform.setY(y);
        transform.setRotation(rotation);

        // Set bullet type
        BulletComponent bulletComponent = bullet.getComponent(BulletComponent.class);
        bulletComponent.setType(BulletComponent.BulletType.PLAYER);

        // Set collision group
        CollisionComponent collision = bullet.getComponent(CollisionComponent.class);
        collision.addGroup(CollisionGroup.FRIENDLY);

        return bullet;
    }

    /**
     * Creates an enemy bullet.
     */
    public Entity createEnemyBullet(double x, double y, double rotation) {
        Entity bullet = createEntity(null);

        // Set position and rotation
        TransformComponent transform = bullet.getComponent(TransformComponent.class);
        transform.setX(x);
        transform.setY(y);
        transform.setRotation(rotation);

        // Set bullet type
        BulletComponent bulletComponent = bullet.getComponent(BulletComponent.class);
        bulletComponent.setType(BulletComponent.BulletType.ENEMY);

        // Set collision group
        CollisionComponent collision = bullet.getComponent(CollisionComponent.class);
        collision.addGroup(CollisionGroup.HOSTILE);

        return bullet;
    }
}