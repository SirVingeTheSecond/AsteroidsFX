package dk.sdu.mmmi.cbse.common.data;

import dk.sdu.mmmi.cbse.common.components.Component;

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
        // Add a transform component by default for backwards compatibility
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

    // COMPATIBILITY METHODS - These will delegate to the TransformComponent
    // These methods maintain backward compatibility during refactoring

    public double getX() {
        TransformComponent transform = getComponent(TransformComponent.class);
        return transform != null ? transform.getX() : 0;
    }

    public void setX(double x) {
        TransformComponent transform = getComponent(TransformComponent.class);
        if (transform != null) {
            transform.setX(x);
        }
    }

    public double getY() {
        TransformComponent transform = getComponent(TransformComponent.class);
        return transform != null ? transform.getY() : 0;
    }

    public void setY(double y) {
        TransformComponent transform = getComponent(TransformComponent.class);
        if (transform != null) {
            transform.setY(y);
        }
    }

    public double getRotation() {
        TransformComponent transform = getComponent(TransformComponent.class);
        return transform != null ? transform.getRotation() : 0;
    }

    public void setRotation(double rotation) {
        TransformComponent transform = getComponent(TransformComponent.class);
        if (transform != null) {
            transform.setRotation(rotation);
        }
    }

    public float getRadius() {
        TransformComponent transform = getComponent(TransformComponent.class);
        return transform != null ? transform.getRadius() : 0;
    }

    public void setRadius(float radius) {
        TransformComponent transform = getComponent(TransformComponent.class);
        if (transform != null) {
            transform.setRadius(radius);
        }
    }

    public double[] getPolygonCoordinates() {
        TransformComponent transform = getComponent(TransformComponent.class);
        return transform != null ? transform.getPolygonCoordinates() : null;
    }

    public void setPolygonCoordinates(double... coordinates) {
        TransformComponent transform = getComponent(TransformComponent.class);
        if (transform != null) {
            transform.setPolygonCoordinates(coordinates);
        }
    }
}