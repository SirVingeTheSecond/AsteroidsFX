module Bullet {
    requires Common;
    requires CommonBullet;
    requires CommonCollision;
    requires Player;

    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService with
            dk.sdu.mmmi.cbse.bulletsystem.BulletSystem,
            dk.sdu.mmmi.cbse.bulletsystem.ShootSystem;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService with
            dk.sdu.mmmi.cbse.bulletsystem.BulletPlugin;

    provides dk.sdu.mmmi.cbse.common.bullet.BulletSPI with
            dk.sdu.mmmi.cbse.bulletsystem.BulletCreator;
}