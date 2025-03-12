module Collision {
    requires Common;
    requires CommonCollision;
    requires javafx.graphics;

    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;
    uses dk.sdu.mmmi.cbse.common.services.ICollisionService;

    exports dk.sdu.mmmi.cbse.collisionsystem;
    exports dk.sdu.mmmi.cbse.collisionsystem.collision;

    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionSystem;

    provides dk.sdu.mmmi.cbse.common.services.IDebugService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionGridVisualizer;

    provides dk.sdu.mmmi.cbse.common.services.ICollisionService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionSystem;

    provides dk.sdu.mmmi.cbse.common.services.IPluginLifecycle
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionPlugin;
}