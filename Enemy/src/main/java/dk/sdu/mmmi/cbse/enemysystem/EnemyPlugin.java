package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.IEnemyFactory;
import dk.sdu.mmmi.cbse.common.enemy.EnemyBehavior;
import dk.sdu.mmmi.cbse.common.enemy.EnemyProperties;
import dk.sdu.mmmi.cbse.common.services.IPluginLifecycle;
import dk.sdu.mmmi.cbse.common.components.MovementComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionComponent;
import dk.sdu.mmmi.cbse.common.collision.CollisionLayer;
import dk.sdu.mmmi.cbse.common.collision.CollisionGroup;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;

public class EnemyPlugin implements IPluginLifecycle, IEnemyFactory {
    private final List<Entity> enemies = new ArrayList<>();
    private final Random rnd = new Random();
    private final IGameEventService eventService;

    public EnemyPlugin() {
        this.eventService = ServiceLoader.load(IGameEventService.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No IGameEventService implementation found"));
    }

    @Override
    public void start(GameData gameData, World world) {
        // Create different types of enemies
        createEnemy(gameData, EnemyBehavior.PATROL, createPatrollerProperties());
        createEnemy(gameData, EnemyBehavior.AGGRESSIVE, createAggressorProperties());
        createEnemy(gameData, EnemyBehavior.SNIPER, createSniperProperties());
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity enemy : enemies) {
            world.removeEntity(enemy);
        }
        enemies.clear();
    }

    @Override
    public Entity createEnemy(GameData gameData, EnemyBehavior behavior, EnemyProperties properties) {
        EnemyShip enemy = new EnemyShip(eventService);
        enemy.setBehavior(behavior);

        // Set base properties
        enemy.setPolygonCoordinates(5,-5, -10,0, 5,5);
        enemy.setRadius(8);

        // Random starting position
        enemy.setX(rnd.nextDouble() * gameData.getDisplayWidth());
        enemy.setY(rnd.nextDouble() * gameData.getDisplayHeight());

        // Set behavior-specific properties
        setupEnemyProperties(enemy, properties);
        setupMovementComponent(enemy, behavior);
        setupCollisionComponent(enemy);

        return enemy;
    }

    private void setupEnemyProperties(EnemyShip enemy, EnemyProperties properties) {
        EnemyProperties enemyProps = enemy.getProperties();
        enemyProps.setHealth(properties.getHealth());
        enemyProps.setDamage(properties.getDamage());
        enemyProps.setSpeed(properties.getSpeed());
        enemyProps.setShootingRange(properties.getShootingRange());
        enemyProps.setScoreValue(properties.getScoreValue());
        enemyProps.setDetectionRange(properties.getDetectionRange());
    }

    private void setupMovementComponent(Entity enemy, EnemyBehavior behavior) {
        MovementComponent movement = new MovementComponent();

        switch (behavior) {
            case PATROL:
                movement.setPattern(MovementComponent.MovementPattern.LINEAR);
                movement.setSpeed(1.0f);
                break;
            case AGGRESSIVE:
                movement.setPattern(MovementComponent.MovementPattern.HOMING);
                movement.setSpeed(2.0f);
                break;
            case DEFENSIVE:
                movement.setPattern(MovementComponent.MovementPattern.RANDOM);
                movement.setSpeed(1.5f);
                break;
            case SNIPER:
                movement.setPattern(MovementComponent.MovementPattern.LINEAR);
                movement.setSpeed(0.5f);
                break;
        }

        enemy.addComponent(movement);
    }

    private void setupCollisionComponent(Entity enemy) {
        CollisionComponent collision = new CollisionComponent();
        collision.setLayer(CollisionLayer.ENEMY);
        collision.addGroup(CollisionGroup.HOSTILE);
        collision.addGroup(CollisionGroup.DESTRUCTIBLE);
        enemy.addComponent(collision);
    }

    private EnemyProperties createPatrollerProperties() {
        EnemyProperties props = new EnemyProperties();
        props.setHealth(50);
        props.setDamage(10);
        props.setSpeed(1.0f);
        props.setShootingRange(150);
        props.setScoreValue(100);
        props.setDetectionRange(200);
        return props;
    }

    private EnemyProperties createAggressorProperties() {
        EnemyProperties props = new EnemyProperties();
        props.setHealth(75);
        props.setDamage(15);
        props.setSpeed(2.0f);
        props.setShootingRange(100);
        props.setScoreValue(150);
        props.setDetectionRange(300);
        return props;
    }

    private EnemyProperties createSniperProperties() {
        EnemyProperties props = new EnemyProperties();
        props.setHealth(40);
        props.setDamage(20);
        props.setSpeed(0.5f);
        props.setShootingRange(400);
        props.setScoreValue(200);
        props.setDetectionRange(500);
        return props;
    }
}