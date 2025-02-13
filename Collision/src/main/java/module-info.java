module Collision {
    requires Common;
    requires CommonAsteroids;
    requires Player;
    requires Enemy;
    requires CommonBullet;
    requires javafx.graphics;

    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;
    uses dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;

    exports dk.sdu.mmmi.cbse.collisionsystem;
    exports dk.sdu.mmmi.cbse.collisionsystem.collision;
    exports dk.sdu.mmmi.cbse.collisionsystem.strategy;

    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionsystem.CollisionSystem;

    provides dk.sdu.mmmi.cbse.common.services.IDebugService with dk.sdu.mmmi.cbse.collisionsystem.CollisionGridVisualizer;
}