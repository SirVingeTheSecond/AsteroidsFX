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
        this.health = maxHealth;
    }

    // Only getters and setters
    public float getHealth() { return health; }
    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }
    public float getMaxHealth() { return maxHealth; }
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }
    public float getHealthPercentage() {
        if (maxHealth <= 0) return 0;
        return health / maxHealth;
    }
    public boolean isAlive() { return health > 0; }
}