package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Bullet extends Entity {
    private String shooterID; // ID of the entity that fired this bullet
    private BulletType type;
    private int remainingLifetime; // Current lifetime countdown

    public enum BulletType {
        PLAYER(2.0f, 600),  // 10 seconds at 60 FPS
        ENEMY(1.0f, 600);   // 10 seconds at 60 FPS

        private final float speed;
        private final int lifetime;

        BulletType(float speed, int lifetime) {
            this.speed = speed;
            this.lifetime = lifetime;
        }

        public float getSpeed() { return speed; }
        public int getLifetime() { return lifetime; }
    }

    public void setType(BulletType type) {
        this.type = type;
        this.remainingLifetime = type.getLifetime();
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
        return type.getSpeed();
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