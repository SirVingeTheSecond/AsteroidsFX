package dk.sdu.mmmi.cbse.common.components;

/**
 * Component that handles shooting-related data for entities.
 */
public class ShootingComponent implements IComponent {
    private int cooldownMax;
    private int cooldownRemaining;
    private boolean canShoot;
    private float projectileSpeed;
    private float damage;
    private int projectileLifetime;

    public ShootingComponent() {
        this.cooldownMax = 20; // Default 20 frames between shots
        this.cooldownRemaining = 0;
        this.canShoot = true;
        this.projectileSpeed = 2.0f;
        this.damage = 10.0f;
        this.projectileLifetime = 60;
    }

    public int getCooldownMax() { return cooldownMax; }
    public void setCooldownMax(int cooldownMax) { this.cooldownMax = cooldownMax; }

    public int getCooldownRemaining() { return cooldownRemaining; }
    public void setCooldownRemaining(int cooldownRemaining) { this.cooldownRemaining = cooldownRemaining; }

    public boolean isCanShoot() { return canShoot; }
    public void setCanShoot(boolean canShoot) { this.canShoot = canShoot; }

    public float getProjectileSpeed() { return projectileSpeed; }
    public void setProjectileSpeed(float projectileSpeed) { this.projectileSpeed = projectileSpeed; }

    public float getDamage() { return damage; }
    public void setDamage(float damage) { this.damage = damage; }

    public int getProjectileLifetime() { return projectileLifetime; }
    public void setProjectileLifetime(int projectileLifetime) { this.projectileLifetime = projectileLifetime; }

    /**
     * Update the cooldown timer.
     */
    public void updateCooldown() {
        if (cooldownRemaining > 0) {
            cooldownRemaining--;
            canShoot = false;
        } else {
            canShoot = true;
        }
    }

    /**
     * Reset the cooldown timer after shooting.
     */
    public void resetCooldown() {
        cooldownRemaining = cooldownMax;
        canShoot = false;
    }
}