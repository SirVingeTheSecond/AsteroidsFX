package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.events.IGameEvent;
import dk.sdu.mmmi.cbse.common.events.IGameEventListener;

/**
 * Interface for the event processing service
 */
/**
 * Interface for the event service
 */
public interface IGameEventService {
    <T extends IGameEvent> void addListener(Class<T> eventType, IGameEventListener<T> listener);
    <T extends IGameEvent> void removeListener(Class<T> eventType, IGameEventListener<T> listener);
    void publish(IGameEvent event);
    void process();
}
