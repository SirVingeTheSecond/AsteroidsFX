package dk.sdu.mmmi.cbse.common.components;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Component that contains AI-related properties.
 * This can be used by any entity with AI capabilities.
 */

// Is this Component too similar to Movement?
public class AIComponent implements Component {
    private float detectionRange;
    private float fleeThreshold; // Health percentage at which entity flees
    private Entity target; // Current target entity

    public AIComponent() {
        this.detectionRange = 300.0f;
        this.fleeThreshold = 0.3f; // Default flee at 30% health
        this.target = null;
    }

    public float getDetectionRange() {
        return detectionRange;
    }

    public void setDetectionRange(float detectionRange) {
        this.detectionRange = detectionRange;
    }

    public float getFleeThreshold() {
        return fleeThreshold;
    }

    public void setFleeThreshold(float fleeThreshold) {
        this.fleeThreshold = fleeThreshold;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    /**
     * Check if entity should flee based on its current health percentage
     * @param healthPercentage Current health as a percentage of max health
     * @return true if entity should flee, false otherwise
     */
    public boolean shouldFlee(float healthPercentage) {
        return healthPercentage <= fleeThreshold;
    }
}
