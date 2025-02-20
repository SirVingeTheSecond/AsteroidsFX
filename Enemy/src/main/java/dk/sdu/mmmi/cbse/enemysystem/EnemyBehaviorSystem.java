package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.EnemyBehavior;
import dk.sdu.mmmi.cbse.common.enemy.IEnemyShip;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class EnemyBehaviorSystem implements IEntityProcessingService {
    public EnemyBehaviorSystem() {} // Required default constructor

    @Override
    public void process(GameData gameData, World world) {
        Entity player = EnemyUtils.findPlayer(world);

        for (Entity entity : world.getEntities(EnemyShip.class)) {
            IEnemyShip enemy = (IEnemyShip) entity;
            updateBehavior(enemy, player, world);
        }
    }

    private void updateBehavior(IEnemyShip enemy, Entity player, World world) {
        if (player == null) return;

        float distanceToPlayer = EnemyUtils.calculateDistance((Entity)enemy, player);
        float health = enemy.getProperties().getHealth();
        float maxHealth = 100; // Default max health or get from properties

        if (health < maxHealth * 0.3) {
            ((EnemyShip)enemy).setBehavior(EnemyBehavior.DEFENSIVE);
        } else if (distanceToPlayer <= enemy.getProperties().getDetectionRange()) {
            ((EnemyShip)enemy).setBehavior(EnemyBehavior.AGGRESSIVE);
        }
    }
}