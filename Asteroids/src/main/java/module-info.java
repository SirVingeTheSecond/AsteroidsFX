module Asteroid {
    requires CommonAsteroids;
    requires CommonCollision;
    requires Common;

    exports dk.sdu.mmmi.cbse.asteroid;

    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;
    uses dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.asteroid.AsteroidPlugin;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.asteroid.AsteroidSystem;

    provides dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter
            with dk.sdu.mmmi.cbse.asteroid.AsteroidSplitter;
}