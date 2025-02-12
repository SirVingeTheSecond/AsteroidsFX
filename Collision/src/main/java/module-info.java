module Collision {
    requires Common;
    requires javafx.graphics;

    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;

    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionsystem.CollisionSystem;
    provides dk.sdu.mmmi.cbse.common.services.IDebugService with dk.sdu.mmmi.cbse.collisionsystem.CollisionGridVisualizer;
}