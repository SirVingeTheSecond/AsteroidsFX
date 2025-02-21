package dk.sdu.mmmi.cbse.common.components;

/**
 * Generic movement component for any entity that needs movement behavior.
 * Can be used by asteroids, enemies, players, or any other moving entity.
 */
public class MovementComponent implements Component {
    private float speed;
    private float rotationSpeed;
    private float accelerationSpeed;
    private boolean isAccelerating;
    private long lastDirectionChange;

    // Movement type to define different movement patterns
    public enum MovementPattern {
        LINEAR,      // Straight line movement
        RANDOM,      // Random direction changes
        HOMING,      // Follows a target
        PLAYER       // Responds to player input
    }

    private MovementPattern pattern;

    public MovementComponent() {
        this.pattern = MovementPattern.LINEAR;
    }

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