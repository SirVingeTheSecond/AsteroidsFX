package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class EntityDestroyedEvent extends BaseGameEvent {
    private final String reason;

    public EntityDestroyedEvent(Entity source, String reason) {
        super(source);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
