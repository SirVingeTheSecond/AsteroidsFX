package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.components.PlayerComponent;
import dk.sdu.mmmi.cbse.common.components.TagComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class PlayerStateSystem implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {

            // Skip entities that are not a player
            TagComponent tagComponent = entity.getComponent(TagComponent.class);
            if (tagComponent != null || !tagComponent.hasTag(TagComponent.TAG_PLAYER)) {
                continue;
            }

            PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
            if (playerComponent == null) {
                continue;
            }

            if (playerComponent.isInvulnerable()) {
                int timer = playerComponent.getInvulnerabilityTimer();
                if (timer > 0) {
                    playerComponent.setInvulnerabilityTimer(--timer);
                } else {
                    playerComponent.setInvulnerable(false);
                }
            }
        }
    }
}
