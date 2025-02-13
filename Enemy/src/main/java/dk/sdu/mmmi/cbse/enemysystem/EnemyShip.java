package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class EnemyShip extends Entity {
    private final TimerComponent timerComponent;

    public EnemyShip() {
        this.timerComponent = new TimerComponent();
    }

    public TimerComponent getTimerComponent() {
        return timerComponent;
    }
}
