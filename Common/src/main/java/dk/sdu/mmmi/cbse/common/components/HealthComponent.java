package dk.sdu.mmmi.cbse.common.components;

/**
 * Component for entity health and damage.
 * Manages current and maximum health values.
 */
public class HealthComponent implements IComponent {
    private float health;
    private float maxHealth;

    /**
     * Create a new health component with full health
     * @param maxHealth The maximum health value
     */
    public HealthComponent(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    // Getters and setters
    public float getHealth() { return health; }
    public void setHealth(float health) { this.health = health; }
    public float getMaxHealth() { return maxHealth; }
    public void setMaxHealth(float maxHealth) { this.maxHealth = maxHealth; }

    /**
     * Get health as a percentage of max health
     * @return Health percentage (0.0 to 1.0)
     */
    public float getHealthPercentage() {
        if (maxHealth <= 0) return 0;
        return health / maxHealth;
    }

    /**
     * Check if entity is still alive
     * @return true if health > 0
     */
    public boolean isAlive() { return health > 0; }
}