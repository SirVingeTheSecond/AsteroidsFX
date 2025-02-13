package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {
    private final Random random = new Random();
    private static final float ENEMY_SPEED = 1.2f;
    private static final int MOVEMENT_UPDATE_INTERVAL = 180;
    private static final int SHOOTING_INTERVAL = 180;
    private static final float MAX_ROTATION_CHANGE = 45.0f;
    private static final float EDGE_MARGIN = 50.0f; // Distance from edge to start turning - prevents the enemy from getting stuck

    @Override
    public void process(GameData gameData, World world) {
        var enemies = world.getEntities(EnemyShip.class);

        for (Entity enemy : enemies) {
            EnemyShip enemyShip = (EnemyShip) enemy;
            updateTimers(enemyShip);

            // Check if near edges and update direction if needed
            if (isNearEdge(enemyShip, gameData)) {
                updateDirectionAwayFromEdge(enemyShip, gameData);
            }

            // Regular movement pattern
            if (enemyShip.getTimerComponent().getMoveTimer() <= 0) {
                updateMovementDirection(enemyShip);
            }

            // Move the enemy ship
            moveEnemyShip(enemyShip, gameData);

            // Only shoot if not near edges and timer is up
            if (enemyShip.getTimerComponent().getShootTimer() <= 0 && !isNearEdge(enemyShip, gameData)) {
                shootBullet(enemyShip, gameData, world);
                enemyShip.getTimerComponent().setShootTimer(SHOOTING_INTERVAL + random.nextInt(60));
            }
        }
    }

    private boolean isNearEdge(EnemyShip enemy, GameData gameData) {
        return enemy.getX() < EDGE_MARGIN ||
                enemy.getX() > gameData.getDisplayWidth() - EDGE_MARGIN ||
                enemy.getY() < EDGE_MARGIN ||
                enemy.getY() > gameData.getDisplayHeight() - EDGE_MARGIN;
    }

    private void updateDirectionAwayFromEdge(EnemyShip enemy, GameData gameData) {
        double centerX = gameData.getDisplayWidth() / 2.0;
        double centerY = gameData.getDisplayHeight() / 2.0;

        // Calculate angle towards center
        double dx = centerX - enemy.getX();
        double dy = centerY - enemy.getY();

        double angleToCenter = Math.toDegrees(Math.atan2(dy, dx));

        angleToCenter += (random.nextDouble() - 0.5) * 60;

        // Ensure that we are between 0 and 360 degrees
        angleToCenter = angleToCenter % 360;

        enemy.setRotation(angleToCenter);
        enemy.getTimerComponent().setMoveTimer(MOVEMENT_UPDATE_INTERVAL);
    }

    private void updateTimers(EnemyShip enemyShip) {
        var timerComponent = enemyShip.getTimerComponent();
        timerComponent.setMoveTimer(timerComponent.getMoveTimer() - 1);
        timerComponent.setShootTimer(timerComponent.getShootTimer() - 1);
    }

    private void updateMovementDirection(EnemyShip enemyShip) {
        double currentRotation = enemyShip.getRotation();
        double rotationChange = (random.nextDouble() * 2 - 1) * MAX_ROTATION_CHANGE;
        double newRotation = (currentRotation + rotationChange + 360) % 360;

        enemyShip.setRotation(newRotation);
        enemyShip.getTimerComponent().setMoveTimer(MOVEMENT_UPDATE_INTERVAL + random.nextInt(60));
    }

    private void moveEnemyShip(EnemyShip enemyShip, GameData gameData) {
        // Calculate movement based on current rotation
        double radians = Math.toRadians(enemyShip.getRotation());
        double deltaX = Math.cos(radians) * ENEMY_SPEED;
        double deltaY = Math.sin(radians) * ENEMY_SPEED;

        // Update position
        double newX = enemyShip.getX() + deltaX;
        double newY = enemyShip.getY() + deltaY;

        // Constrain to screen with a small margin
        newX = Math.max(5, Math.min(gameData.getDisplayWidth() - 5, newX));
        newY = Math.max(5, Math.min(gameData.getDisplayHeight() - 5, newY));

        enemyShip.setX(newX);
        enemyShip.setY(newY);
    }

    private void shootBullet(Entity enemy, GameData gameData, World world) {
        getBulletSPIs().stream().findFirst().ifPresent(
                bulletSPI -> world.addEntity(bulletSPI.createBullet(enemy, gameData))
        );
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }
}