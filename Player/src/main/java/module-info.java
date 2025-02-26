module Player {
    exports dk.sdu.mmmi.cbse.playersystem;
    requires Common;
    requires CommonBullet;
    requires CommonCollision;

    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService with dk.sdu.mmmi.cbse.playersystem.PlayerControlSystem;
}
