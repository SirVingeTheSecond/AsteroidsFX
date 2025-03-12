module Collision {
    requires Common;
    requires CommonCollision;
    requires javafx.graphics;
    requires java.logging;

    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;

    exports dk.sdu.mmmi.cbse.collisionsystem;

    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionSystem;

    provides dk.sdu.mmmi.cbse.common.services.IDebugService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionGridVisualizer;

    provides dk.sdu.mmmi.cbse.common.services.ICollisionService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionSystem;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionPlugin;
}