package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.IEnemyShip;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class EnemyCombatSystem implements IEntityProcessingService {
    private Collection<? extends BulletSPI> bulletSPIs;
    private final Random random = new Random();

    public EnemyCombatSystem() {
        // Load BulletSPIs in constructor using ServiceLoader
        this.bulletSPIs = ServiceLoader.load(BulletSPI.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }

    @Override
    public void process(GameData gameData, World world) {
        Entity player = EnemyUtils.findPlayer(world);

        for (Entity entity : world.getEntities(EnemyShip.class)) {
            IEnemyShip enemy = (IEnemyShip) entity;

            if (shouldShoot(entity, player, enemy)) {
                shoot(entity, gameData, world);
            }
        }
    }

    private boolean shouldShoot(Entity entity, Entity player, IEnemyShip enemy) {
        if (player == null) return false;

        float range = EnemyUtils.calculateDistance(entity, player);
        return range <= enemy.getProperties().getShootingRange()
                && EnemyUtils.hasLineOfSight(entity, player)
                && random.nextFloat() < 0.1;
    }

    private void shoot(Entity entity, GameData gameData, World world) {
        bulletSPIs.stream().findFirst().ifPresent(
                bulletSPI -> world.addEntity(bulletSPI.createBullet(entity, gameData))
        );
    }
}