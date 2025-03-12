package dk.sdu.mmmi.cbse.common.events;

import dk.sdu.mmmi.cbse.common.services.IGameEventService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameEventService implements IGameEventService {
    private static final Logger LOGGER = Logger.getLogger(GameEventService.class.getName());
    private final Map<Class<? extends IGameEvent>, List<IGameEventListener<?>>> listeners = new ConcurrentHashMap<>();
    private final List<IGameEvent> eventQueue = new ArrayList<>();

    @Override
    public <T extends IGameEvent> void addListener(Class<T> eventType, IGameEventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public <T extends IGameEvent> void removeListener(Class<T> eventType, IGameEventListener<T> listener) {
        List<IGameEventListener<?>> typeListeners = listeners.get(eventType);
        if (typeListeners != null) {
            typeListeners.remove(listener);
        }
    }

    @Override
    public void publish(IGameEvent event) {
        if (event != null) {
            synchronized(eventQueue) {
                eventQueue.add(event);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process() {
        List<IGameEvent> currentEvents;
        synchronized(eventQueue) {
            currentEvents = new ArrayList<>(eventQueue);
            eventQueue.clear();
        }

        for (IGameEvent event : currentEvents) {
            List<IGameEventListener<?>> eventListeners = listeners.get(event.getClass());
            if (eventListeners != null) {
                for (IGameEventListener listener : eventListeners) {
                    try {
                        listener.onEvent(event);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error processing event: " + e.getMessage(), e);
                    }
                }
            }
        }
    }
}