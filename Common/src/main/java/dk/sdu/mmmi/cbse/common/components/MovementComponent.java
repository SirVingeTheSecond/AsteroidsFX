package dk.sdu.mmmi.cbse.common.components;

/**
 * Component for entity movement behavior.
 * Defines movement patterns and properties.
 */
public class MovementComponent implements IComponent {
    // Movement properties
    private float speed;
    private float rotationSpeed;
    private float accelerationSpeed;
    private boolean isAccelerating;
    private long lastDirectionChange;

    /**
     * Movement patterns for different entity behaviors
     */
    public enum MovementPattern {
        LINEAR,  // Straight line movement
        RANDOM,  // Random direction changes
        HOMING,  // Follows a target
        PLAYER   // Responds to player input
    }

    private MovementPattern pattern;

    /**
     * Create a new movement component with LINEAR pattern
     */
    public MovementComponent() {
        this.pattern = MovementPattern.LINEAR;
    }

    // Getters and setters
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getAccelerationSpeed() {
        return accelerationSpeed;
    }

    public void setAccelerationSpeed(float accelerationSpeed) {
        this.accelerationSpeed = accelerationSpeed;
    }

    public boolean isAccelerating() {
        return isAccelerating;
    }

    public void setAccelerating(boolean accelerating) {
        isAccelerating = accelerating;
    }

    public long getLastDirectionChange() {
        return lastDirectionChange;
    }

    public void setLastDirectionChange(long lastDirectionChange) {
        this.lastDirectionChange = lastDirectionChange;
    }

    public MovementPattern getPattern() {
        return pattern;
    }

    public void setPattern(MovementPattern pattern) {
        this.pattern = pattern;
    }
}