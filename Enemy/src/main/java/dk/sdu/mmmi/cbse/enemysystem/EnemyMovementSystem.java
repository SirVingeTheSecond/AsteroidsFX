package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.MovementComponent;
import dk.sdu.mmmi.cbse.common.enemy.IEnemyShip;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class EnemyMovementSystem implements IEntityProcessingService {
    private final Random random = new Random();

    public EnemyMovementSystem() {} // Required default constructor


    @Override
    public void process(GameData gameData, World world) {
        Entity player = EnemyUtils.findPlayer(world);

        for (Entity entity : world.getEntities(EnemyShip.class)) {
            IEnemyShip enemy = (IEnemyShip) entity;
            MovementComponent movement = entity.getComponent(MovementComponent.class);

            if (movement == null) continue;

            switch (enemy.getBehavior()) {
                case PATROL:
                    updatePatrolMovement(entity, movement, gameData);
                    break;
                case AGGRESSIVE:
                    if (player != null) {
                        updateAggressiveMovement(entity, movement, player);
                    }
                    break;
                case DEFENSIVE:
                    if (player != null) {
                        updateDefensiveMovement(entity, movement, player);
                    }
                    break;
                case SNIPER:
                    if (player != null) {
                        updateSniperMovement(entity, movement, player);
                    }
                    break;
            }
        }
    }

    private void updatePatrolMovement(Entity entity, MovementComponent movement, GameData gameData) {
        if (isNearEdge(entity, gameData)) {
            double currentRotation = entity.getRotation();
            currentRotation += 90 + random.nextInt(90);
            entity.setRotation(currentRotation % 360);
        }
        movement.setPattern(MovementComponent.MovementPattern.LINEAR);
    }

    private void updateAggressiveMovement(Entity entity, MovementComponent movement, Entity player) {
        float distance = EnemyUtils.calculateDistance(entity, player);
        if (distance > 0) {
            double angle = Math.toDegrees(Math.atan2(
                    player.getY() - entity.getY(),
                    player.getX() - entity.getX()
            ));
            entity.setRotation(angle);
            movement.setPattern(MovementComponent.MovementPattern.LINEAR);
        }
    }

    private void updateDefensiveMovement(Entity entity, MovementComponent movement, Entity player) {
        float distance = EnemyUtils.calculateDistance(entity, player);
        if (distance < 150) { // Keep distance
            double angle = Math.toDegrees(Math.atan2(
                    entity.getY() - player.getY(),
                    entity.getX() - player.getX()
            ));
            entity.setRotation(angle);
        }
        movement.setPattern(MovementComponent.MovementPattern.RANDOM);
    }

    private void updateSniperMovement(Entity entity, MovementComponent movement, Entity player) {
        if (EnemyUtils.hasLineOfSight(entity, player)) {
            movement.setSpeed(0);
            double angle = Math.toDegrees(Math.atan2(
                    player.getY() - entity.getY(),
                    player.getX() - entity.getX()
            ));
            entity.setRotation(angle);
        } else {
            movement.setSpeed(1);
            movement.setPattern(MovementComponent.MovementPattern.RANDOM);
        }
    }

    private boolean isNearEdge(Entity entity, GameData gameData) {
        final float EDGE_MARGIN = 50.0f;
        return entity.getX() < EDGE_MARGIN
                || entity.getX() > gameData.getDisplayWidth() - EDGE_MARGIN
                || entity.getY() < EDGE_MARGIN
                || entity.getY() > gameData.getDisplayHeight() - EDGE_MARGIN;
    }
}

