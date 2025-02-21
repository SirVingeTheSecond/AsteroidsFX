package dk.sdu.mmmi.cbse.common.collision;

import dk.sdu.mmmi.cbse.common.components.Component;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Component that handles collision responses
 */
public class CollisionResponseComponent implements Component {
    private final Map<CollisionLayer, CollisionResponseHandler> layerResponses = new HashMap<>();
    private final Map<CollisionGroup, CollisionResponseHandler> groupResponses = new HashMap<>();

    public void addLayerResponse(CollisionLayer layer, CollisionResponseHandler handler) {
        layerResponses.put(layer, handler);
    }

    public void addGroupResponse(CollisionGroup group, CollisionResponseHandler handler) {
        groupResponses.put(group, handler);
    }

    public boolean handleCollision(Entity self, Entity other, World world) {
        CollisionComponent otherCC = other.getComponent(CollisionComponent.class);
        if (otherCC == null || !otherCC.isActive()) {
            return false;
        }

        // Check layer-specific response
        CollisionResponseHandler layerHandler = layerResponses.get(otherCC.getLayer());
        if (layerHandler != null && layerHandler.onCollision(self, other, world)) {
            return true;
        }

        // Check group-specific responses
        for (CollisionGroup group : CollisionGroup.values()) {
            if (otherCC.isInGroup(group)) {
                CollisionResponseHandler groupHandler = groupResponses.get(group);
                if (groupHandler != null && groupHandler.onCollision(self, other, world)) {
                    return true;
                }
            }
        }

        return false;
    }
}
