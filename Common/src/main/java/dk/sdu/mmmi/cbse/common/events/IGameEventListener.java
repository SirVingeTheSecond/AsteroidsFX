package dk.sdu.mmmi.cbse.common.events;

/**
 * Interface for event listeners
 */
public interface IGameEventListener<T extends IGameEvent> {
    void onEvent(T event);
}
