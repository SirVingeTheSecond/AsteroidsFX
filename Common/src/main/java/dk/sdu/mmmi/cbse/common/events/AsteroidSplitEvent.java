package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Event triggered when an asteroid should be split.
 */
public class AsteroidSplitEvent extends BaseGameEvent {

    /**
     * Create a new asteroid split event
     * @param source The asteroid entity to split
     */
    public AsteroidSplitEvent(Entity source) {
        super(source);
    }
}