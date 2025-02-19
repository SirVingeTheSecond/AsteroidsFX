package dk.sdu.mmmi.cbse.common.collision;

import dk.sdu.mmmi.cbse.common.data.Component;

/**
 * Component that defines collision properties for an entity
 */
public class CollisionComponent implements Component {
    private CollisionLayer layer;
    private int groups;
    private boolean active = true;

    public CollisionLayer getLayer() {
        return layer;
    }

    public void setLayer(CollisionLayer layer) {
        this.layer = layer;
    }

    public void addGroup(CollisionGroup group) {
        groups |= group.getMask();
    }

    public void removeGroup(CollisionGroup group) {
        groups &= ~group.getMask();
    }

    public boolean isInGroup(CollisionGroup group) {
        return (groups & group.getMask()) != 0;
    }

    public int getGroups() {
        return groups;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
