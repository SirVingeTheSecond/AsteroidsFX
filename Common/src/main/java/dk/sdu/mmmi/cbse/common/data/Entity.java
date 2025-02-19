package dk.sdu.mmmi.cbse.common.data;

import java.io.Serializable;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base entity class using composition for components.
 * Provides core entity functionality and component management.
 */
public class Entity implements Serializable {
    private final UUID ID = UUID.randomUUID();
    private final Map<Class<?>, Component> components = new ConcurrentHashMap<>();

    // Core positional common
    private double x;
    private double y;
    private double rotation;
    private float radius;
    private double[] polygonCoordinates;

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

    // Positional getters/setters
    public void setPolygonCoordinates(double... coordinates) {
        this.polygonCoordinates = coordinates;
    }

    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}