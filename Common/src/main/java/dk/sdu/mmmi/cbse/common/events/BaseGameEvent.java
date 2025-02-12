package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Base class for all game events
 */
public abstract class BaseGameEvent implements IGameEvent {
    private final long timestamp;
    private final Entity source;

    protected BaseGameEvent(Entity source) {
        this.timestamp = System.currentTimeMillis();
        this.source = source;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public Entity getSource() {
        return source;
    }
}