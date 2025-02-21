package dk.sdu.mmmi.cbse.common.components;

/**
 * Component that represents entity health and related properties.
 * Can be used by any entity that has health, not just enemies.
 */
public class HealthComponent implements Component {
    private float health;
    private float maxHealth;

    public HealthComponent(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth; // Start at full health
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        // Clamp health between 0 and maxHealth
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        // If current health is greater than new max, set to new max
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    /**
     * Apply damage to the entity
     * @param amount Amount of damage to apply
     * @return true if the entity is still alive, false if it died
     */
    public boolean damage(float amount) {
        health = Math.max(0, health - amount);
        return health > 0;
    }

    /**
     * Heal the entity
     * @param amount Amount to heal
     */
    public void heal(float amount) {
        health = Math.min(maxHealth, health + amount);
    }

    /**
     * Get health as a percentage of max health
     * @return Percentage between 0.0 and 1.0
     */
    public float getHealthPercentage() {
        if (maxHealth <= 0) return 0;
        return health / maxHealth;
    }

    /**
     * Check if entity is alive
     * @return true if health > 0, false otherwise
     */
    public boolean isAlive() {
        return health > 0;
    }
}