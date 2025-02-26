import dk.sdu.mmmi.cbse.common.services.IPluginLifecycle;
import dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.enemy.IEnemyFactory;
import dk.sdu.mmmi.cbse.common.enemy.IEnemySpawner;


module Enemy {
    exports dk.sdu.mmmi.cbse.enemysystem;

    requires Common;
    requires CommonBullet;
    requires CommonCollision;
    requires CommonEnemy;

    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;
    uses dk.sdu.mmmi.cbse.common.enemy.IEnemyFactory;
    uses dk.sdu.mmmi.cbse.common.enemy.IEnemySpawner;

    provides dk.sdu.mmmi.cbse.common.services.IPluginLifecycle
            with dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.enemysystem.EnemyBehaviorSystem,
                    dk.sdu.mmmi.cbse.enemysystem.EnemyMovementSystem,
                    dk.sdu.mmmi.cbse.enemysystem.EnemyCombatSystem,
                    dk.sdu.mmmi.cbse.enemysystem.EnemySpawnSystem;
    provides dk.sdu.mmmi.cbse.common.enemy.IEnemyFactory
            with dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
    provides dk.sdu.mmmi.cbse.common.enemy.IEnemySpawner
            with dk.sdu.mmmi.cbse.enemysystem.EnemySpawner;
}