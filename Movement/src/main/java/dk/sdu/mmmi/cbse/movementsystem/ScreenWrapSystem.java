package dk.sdu.mmmi.cbse.movementsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

/**
 * System that handles screen wrapping for all entities with transform components.
 * This is a post-processing system that runs after all entity movement is complete.
 */
public class ScreenWrapSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            // Skip entities without transform component
            if (!entity.hasComponent(TransformComponent.class)) {
                continue;
            }

            TransformComponent transform = entity.getComponent(TransformComponent.class);
            handleScreenWrap(transform, gameData);
        }
    }

    private void handleScreenWrap(TransformComponent transform, GameData gameData) {
        double x = transform.getX();
        double y = transform.getY();

        // Wrap horizontally
        if (x < 0) {
            transform.setX(gameData.getDisplayWidth());
        } else if (x > gameData.getDisplayWidth()) {
            transform.setX(0);
        }

        // Wrap vertically
        if (y < 0) {
            transform.setY(gameData.getDisplayHeight());
        } else if (y > gameData.getDisplayHeight()) {
            transform.setY(0);
        }
    }
}