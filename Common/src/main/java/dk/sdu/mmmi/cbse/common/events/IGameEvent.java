package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Base interface for game events
 */
public interface IGameEvent {
    long getTimestamp();
    Entity getSource();
}