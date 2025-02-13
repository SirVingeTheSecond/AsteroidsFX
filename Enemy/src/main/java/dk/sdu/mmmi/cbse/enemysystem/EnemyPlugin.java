package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPluginLifecycle;
import dk.sdu.mmmi.cbse.common.services.IEntityFactory;

import java.util.ArrayList;
import java.util.List;

public class EnemyPlugin implements IPluginLifecycle, IEntityFactory<EnemyShip> {
    private static final int ENEMIES_TO_SPAWN = 3;
    private final List<Entity> enemies = new ArrayList<>();

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < ENEMIES_TO_SPAWN; i++) {
            Entity enemy = createEntity(gameData);
            enemies.add(enemy);
            world.addEntity(enemy);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity enemy : enemies) {
            world.removeEntity(enemy);
        }
        enemies.clear();
    }

    @Override
    public EnemyShip createEntity(GameData gameData) {
        EnemyShip enemyShip = new EnemyShip();

        // Triangle shape pointing left
        enemyShip.setPolygonCoordinates(5,-5, -10,0, 5,5);

        // Random starting position
        enemyShip.setX(Math.random() * gameData.getDisplayWidth());
        enemyShip.setY(Math.random() * gameData.getDisplayHeight());
        enemyShip.setRadius(8);

        return enemyShip;
    }
}
