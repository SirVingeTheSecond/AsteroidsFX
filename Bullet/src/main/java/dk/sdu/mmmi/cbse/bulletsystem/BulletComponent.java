package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.components.IComponent;

/**
 * Component that contains bullet-specific properties.
 */
public class BulletComponent implements IComponent {
    private String shooterID; // ID of the entity that fired this bullet
    private BulletType type;
    private float speed;
    private int remainingLifetime; // Current lifetime countdown
    private int lifetime;
    private float damage;

    public enum BulletType {
        PLAYER,
        ENEMY
    }

    public BulletComponent() {
        this.speed = 2.0f;
        this.lifetime = 600; // 10 seconds at 60 FPS
        this.remainingLifetime = lifetime;
        this.damage = 10.0f;
        this.type = BulletType.PLAYER; // Default
    }

    public void setType(BulletType type) {
        this.type = type;
    }

    public BulletType getType() {
        return type;
    }

    public void setShooterID(String id) {
        this.shooterID = id;
    }

    public String getShooterID() {
        return shooterID;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
        this.remainingLifetime = lifetime;
    }

    public int getRemainingLifetime() {
        return remainingLifetime;
    }

    public void setRemainingLifetime(int lifetime) {
        this.remainingLifetime = lifetime;
    }

    public void reduceLifetime() {
        remainingLifetime--;
    }
}