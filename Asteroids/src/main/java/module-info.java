module Asteroid {
    requires CommonAsteroids;
    requires CommonCollision;
    requires Common;

    exports dk.sdu.mmmi.cbse.asteroid;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.asteroid.AsteroidPlugin;
    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.asteroid.AsteroidProcessor;
    provides dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter
            with dk.sdu.mmmi.cbse.asteroid.AsteroidProcessor;
}