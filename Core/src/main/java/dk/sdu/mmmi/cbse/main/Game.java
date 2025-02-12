package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.*;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toList;

public class Game {
    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Canvas debugCanvas;

    private final List<IDebugService> debugServices;
    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServiceList;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;
    private final IGameEventService eventService;

    Game(List<IGamePluginService> gamePluginServices,
         List<IEntityProcessingService> entityProcessingServiceList,
         List<IPostEntityProcessingService> postEntityProcessingServices,
         IGameEventService eventService) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServiceList = entityProcessingServiceList;
        this.postEntityProcessingServices = postEntityProcessingServices;
        this.eventService = eventService;

        // Load debug services
        this.debugServices = ServiceLoader.load(IDebugService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());

        debugCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(debugCanvas);
    }

    public void start(Stage window) {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(gameWindow);
        setupInput(scene);

        // Initialize all game plugins and add entities
        for (IGamePluginService iGamePlugin : gamePluginServices) {
            iGamePlugin.start(gameData, world);
        }

        // Create polygons for initial entities
        for (Entity entity : world.getEntities()) {
            Polygon polygon = createPolygonForEntity(entity);
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        window.setScene(scene);
        window.setTitle("Asteroids");
        window.show();
    }

    private Polygon createPolygonForEntity(Entity entity) {
        Polygon polygon = new Polygon(entity.getPolygonCoordinates());
        polygon.setTranslateX(entity.getX());
        polygon.setTranslateY(entity.getY());
        polygon.setRotate(entity.getRotation());
        polygon.setStroke(javafx.scene.paint.Color.WHITE);
        polygon.setFill(javafx.scene.paint.Color.TRANSPARENT);
        return polygon;
    }

    private void setupInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT -> gameData.getKeys().setKey(GameKeys.LEFT, true);
                case RIGHT -> gameData.getKeys().setKey(GameKeys.RIGHT, true);
                case UP -> gameData.getKeys().setKey(GameKeys.UP, true);
                case SPACE -> gameData.getKeys().setKey(GameKeys.SPACE, true);
                case F3 -> debugServices.forEach(service -> service.setEnabled(!service.isEnabled()));
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT -> gameData.getKeys().setKey(GameKeys.LEFT, false);
                case RIGHT -> gameData.getKeys().setKey(GameKeys.RIGHT, false);
                case UP -> gameData.getKeys().setKey(GameKeys.UP, false);
                case SPACE -> gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        }.start();
    }

    private void update() {
        // Process all entities
        for (IEntityProcessingService entityProcessorService : entityProcessingServiceList) {
            entityProcessorService.process(gameData, world);
        }

        // Process any pending events
        eventService.process();

        // Run post-processing
        for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessingServices) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private void draw() {
        // Remove previous entities
        gameWindow.getChildren().removeIf(node -> node instanceof Polygon);

        // Update entity polygons
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = createPolygonForEntity(entity);
                polygons.put(entity, polygon);
            }

            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());

            if (!gameWindow.getChildren().contains(polygon)) {
                gameWindow.getChildren().add(polygon);
            }
        }

        // Handle debug rendering
        if (!debugServices.isEmpty()) {
            javafx.scene.canvas.Canvas debugCanvas = new javafx.scene.canvas.Canvas(
                    gameData.getDisplayWidth(),
                    gameData.getDisplayHeight()
            );
            javafx.scene.canvas.GraphicsContext gc = debugCanvas.getGraphicsContext2D();

            // Render debug visualizations
            for (IDebugService debugService : debugServices) {
                debugService.render(gc, gameData, world);
            }

            // Add debug canvas on top
            if (!gameWindow.getChildren().contains(debugCanvas)) {
                gameWindow.getChildren().add(debugCanvas);
            }
        }
    }
}