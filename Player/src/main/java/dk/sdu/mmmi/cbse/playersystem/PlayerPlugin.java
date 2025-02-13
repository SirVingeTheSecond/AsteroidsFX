package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPluginLifecycle;
import dk.sdu.mmmi.cbse.common.services.IEntityFactory;

public class PlayerPlugin implements IPluginLifecycle, IEntityFactory<Player> {
    private Entity player;

    @Override
    public void start(GameData gameData, World world) {
        player = createEntity(gameData);
        world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }

    @Override
    public Player createEntity(GameData gameData) {
        Player playerShip = new Player();
        playerShip.setPolygonCoordinates(-5,-5, 10,0, -5,5);
        playerShip.setX(gameData.getDisplayWidth() / 2);
        playerShip.setY(gameData.getDisplayHeight() / 2);
        playerShip.setRadius(8);
        return playerShip;
    }
}
