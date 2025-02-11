package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.playersystem.Player;

import java.util.ArrayList;
import java.util.List;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> bulletsToRemove = new ArrayList<>();

        for (Entity entity : world.getEntities(Bullet.class)) {
            Bullet bullet = (Bullet) entity;

            // Update position based on bullet speed and direction
            double changeX = Math.cos(Math.toRadians(bullet.getRotation())) * bullet.getSpeed();
            double changeY = Math.sin(Math.toRadians(bullet.getRotation())) * bullet.getSpeed();
            bullet.setX(bullet.getX() + changeX);
            bullet.setY(bullet.getY() + changeY);

            // Handle bullet lifetime - reduce by 1 each frame
            bullet.setRemainingLifetime(bullet.getRemainingLifetime() - 1);

            // Remove bullets that are either expired or out of bounds
            if (bullet.getRemainingLifetime() <= 0 || isOutOfBounds(bullet, gameData)) {
                bulletsToRemove.add(bullet);
            }
        }

        // Remove expired bullets
        bulletsToRemove.forEach(world::removeEntity);
    }

    private boolean isOutOfBounds(Entity bullet, GameData gameData) {
        return bullet.getX() < 0
                || bullet.getX() > gameData.getDisplayWidth()
                || bullet.getY() < 0
                || bullet.getY() > gameData.getDisplayHeight();
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet bullet = new Bullet();

        // Set bullet appearance
        bullet.setPolygonCoordinates(2, -2, 2, 2, -2, 2, -2, -2);
        bullet.setRadius(2);

        // Calculate spawn position in front of the shooter
        double direction = Math.toRadians(shooter.getRotation());
        double spawnDistance = shooter.getRadius() + 5;
        double startX = shooter.getX() + Math.cos(direction) * spawnDistance;
        double startY = shooter.getY() + Math.sin(direction) * spawnDistance;

        bullet.setX(startX);
        bullet.setY(startY);
        bullet.setRotation(shooter.getRotation());

        // Set bullet properties based on shooter type
        if (shooter instanceof Player) {
            bullet.setType(Bullet.BulletType.PLAYER);
        } else {
            bullet.setType(Bullet.BulletType.ENEMY);
        }

        bullet.setShooterID(shooter.getID());

        return bullet;
    }
}