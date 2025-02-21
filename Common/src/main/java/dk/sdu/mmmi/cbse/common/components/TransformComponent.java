package dk.sdu.mmmi.cbse.common.components;

/**
 * Component that handles entity position, rotation, and shape information.
 * Replaces direct positional attributes in Entity class for proper ECS pattern.
 */
public class TransformComponent implements Component {
    private double x;
    private double y;
    private double rotation;
    private float radius;
    private double[] polygonCoordinates;

    public TransformComponent() {
        // Default constructor
    }

    // Position getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Rotation getter and setter
    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    // Radius getter and setter for collision detection
    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    // Shape data getter and setter
    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }

    public void setPolygonCoordinates(double... coordinates) {
        this.polygonCoordinates = coordinates;
    }
}