package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class BulletCreator implements BulletSPI {
    private final BulletFactory bulletFactory;

    public BulletCreator() {
        this.bulletFactory = new BulletFactory();
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        return bulletFactory.createBullet(shooter, gameData);
    }
}