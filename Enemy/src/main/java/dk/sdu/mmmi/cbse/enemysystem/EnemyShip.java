package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class EnemyShip extends Entity {
    private float moveTimer;
    private float shootTimer;

    public float getMoveTimer() {
        return moveTimer;
    }

    public void setMoveTimer(float timer) {
        this.moveTimer = timer;
    }

    public float getShootTimer() {
        return shootTimer;
    }

    public void setShootTimer(float timer) {
        this.shootTimer = timer;
    }
}
