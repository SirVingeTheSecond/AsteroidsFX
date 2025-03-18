package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.components.PlayerComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.components.RendererComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import javafx.scene.paint.Color;

public class PlayerStateSystem implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            // Skip entities that are not a player
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (tagComponent == null || !tagComponent.hasTag(TagComponent.TAG_PLAYER)) {
                continue;
            }

            PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
            RendererComponent renderer = entity.getComponent(RendererComponent.class);

            if (playerComponent == null || renderer == null) {
                continue;
            }

            if (playerComponent.isInvulnerable()) {
                int timer = playerComponent.getInvulnerabilityTimer();
                if (timer > 0) {
                    // Visual blinking effect
                    if ((timer / 5) % 2 == 0) {
                        renderer.setStrokeColor(Color.BLUE);
                    } else {
                        renderer.setStrokeColor(Color.CYAN);
                    }

                    playerComponent.setInvulnerabilityTimer(--timer);
                } else {
                    playerComponent.setInvulnerable(false);
                    renderer.setStrokeColor(Color.GREEN); // Reset to normal color
                }
            }
        }
    }
}