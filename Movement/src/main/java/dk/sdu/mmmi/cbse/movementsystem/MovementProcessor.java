package dk.sdu.mmmi.cbse.movementsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.MovementComponent;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class MovementProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);
            if (movement == null) {
                continue;
            }

            switch (movement.getPattern()) {
                case LINEAR:
                    processLinearMovement(entity, movement);
                    break;
                case RANDOM:
                    processRandomMovement(entity, movement);
                    break;
                case HOMING:
                    // ToDo: Implement homing Entity towards Player
                    break;
                case PLAYER:
                    // Player movement is handled by PlayerControlSystem
                    break;
            }

            handleScreenWrap(entity, gameData);
        }
    }

    private void processLinearMovement(Entity entity, MovementComponent movement) {
        double radians = Math.toRadians(entity.getRotation());
        double deltaX = Math.cos(radians) * movement.getSpeed();
        double deltaY = Math.sin(radians) * movement.getSpeed();

        entity.setX(entity.getX() + deltaX);
        entity.setY(entity.getY() + deltaY);
    }

    private void processRandomMovement(Entity entity, MovementComponent movement) {
        // Apply rotation
        double currentRotation = entity.getRotation();
        currentRotation += movement.getRotationSpeed();
        entity.setRotation(currentRotation);

        // Move based on current rotation and speed
        processLinearMovement(entity, movement);
    }

    private void handleScreenWrap(Entity entity, GameData gameData) {
        double x = entity.getX();
        double y = entity.getY();

        if (x < 0) {
            x = gameData.getDisplayWidth();
        } else if (x > gameData.getDisplayWidth()) {
            x = 0;
        }

        if (y < 0) {
            y = gameData.getDisplayHeight();
        } else if (y > gameData.getDisplayHeight()) {
            y = 0;
        }

        entity.setX(x);
        entity.setY(y);
    }
}