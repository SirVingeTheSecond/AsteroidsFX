import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module Bullet {
    requires java.desktop;
    requires Player;
    requires CommonBullet;
    requires Common;

    provides IEntityProcessingService with dk.sdu.mmmi.cbse.bulletsystem.BulletControlSystem;
    provides IGamePluginService with dk.sdu.mmmi.cbse.bulletsystem.BulletPlugin;
    provides BulletSPI with dk.sdu.mmmi.cbse.bulletsystem.BulletControlSystem;
}