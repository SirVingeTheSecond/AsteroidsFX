package dk.sdu.mmmi.cbse.common.data;

import dk.sdu.mmmi.cbse.common.components.Component;
import dk.sdu.mmmi.cbse.common.components.TransformComponent;

import java.io.Serializable;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base entity class using composition for components.
 * Provides core entity functionality and component management.
 * Following ECS principles, entities are just identifiers with components.
 */
public class Entity implements Serializable {
    private final UUID ID = UUID.randomUUID();
    private final Map<Class<?>, Component> components = new ConcurrentHashMap<>();

    public Entity() {
        // Add a transform component by default
        addComponent(new TransformComponent());
    }

    public String getID() {
        return ID.toString();
    }

    /**
     * Add a component to this entity.
     * @param component The component to add
     * @param <T> Type of component extending Component interface
     */
    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    /**
     * Get a component by type.
     * @param componentType The class of the component to get
     * @return The component if present, null otherwise
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentType) {
        return (T) components.get(componentType);
    }

    /**
     * Remove a component by type.
     * @param componentType The class of the component to remove
     */
    public <T extends Component> void removeComponent(Class<T> componentType) {
        components.remove(componentType);
    }

    /**
     * Check if this entity has a component of the specified type.
     * @param componentType The class of the component to check
     * @return true if the entity has the component, false otherwise
     */
    public <T extends Component> boolean hasComponent(Class<T> componentType) {
        return components.containsKey(componentType);
    }
}