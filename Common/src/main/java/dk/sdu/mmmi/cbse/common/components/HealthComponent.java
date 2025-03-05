package dk.sdu.mmmi.cbse.common.components;

/**
 * Component that represents entity health and related properties.
 * Only stores data, logic should be handled by systems.
 */
public class HealthComponent implements Component {
    private float health;
    private float maxHealth;

    public HealthComponent(float maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public float getHealth() { return health; }
    public void setHealth(float health) { this.health = health; }
    public float getMaxHealth() { return maxHealth; }
    public void setMaxHealth(float maxHealth) { this.maxHealth = maxHealth; }

    public float getHealthPercentage() {
        if (maxHealth <= 0) return 0;
        return health / maxHealth;
    }
    public boolean isAlive() { return health > 0; }
}