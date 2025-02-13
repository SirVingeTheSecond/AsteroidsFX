
module Enemy {
    exports dk.sdu.mmmi.cbse.enemysystem;
    requires Common;
    requires CommonBullet;

    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;

    provides dk.sdu.mmmi.cbse.common.services.IPluginLifecycle with dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
    provides dk.sdu.mmmi.cbse.common.services.IEntityFactory with dk.sdu.mmmi.cbse.enemysystem.EnemyPlugin;
    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService with dk.sdu.mmmi.cbse.enemysystem.EnemyControlSystem;
}
