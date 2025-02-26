package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Canvas debugCanvas;

    private final List<IDebugService> debugServices;
    private final List<IPluginLifecycle> pluginLifecycles;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final IGameEventService eventService;

    Game(List<IPluginLifecycle> pluginLifecycles,
         List<IEntityProcessingService> entityProcessors,
         List<IPostEntityProcessingService> postProcessors,
         IGameEventService eventService) {

        this.pluginLifecycles = pluginLifecycles;
        this.entityProcessors = entityProcessors;
        this.postProcessors = postProcessors;
        this.eventService = eventService;

        // Load debug services dynamically
        this.debugServices = ServiceLoader.load(IDebugService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(java.util.stream.Collectors.toList());

        // Initialize debug canvas
        debugCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        debugCanvas.setMouseTransparent(true);
        gameWindow.getChildren().add(debugCanvas);
    }

    public void start(Stage window) {
        // Setup game window
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(gameWindow);
        setupInput(scene);

        // Start all plugins
        for (IPluginLifecycle plugin : pluginLifecycles) {
            plugin.start(gameData, world);
        }

        // Create initial entity polygons
        for (Entity entity : world.getEntities()) {
            Polygon polygon = createPolygonForEntity(entity);
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        // Setup window
        window.setScene(scene);
        window.setTitle("Asteroids");
        window.show();
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

    // ... rest of the Game class implementation ...
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
                case LEFT -> gameData.getKeys().setKey(dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT, true);
                case RIGHT -> gameData.getKeys().setKey(dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT, true);
                case UP -> gameData.getKeys().setKey(dk.sdu.mmmi.cbse.common.data.GameKeys.UP, true);
                case SPACE -> gameData.getKeys().setKey(dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE, true);
                case F3 -> debugServices.forEach(service ->
                        service.setEnabled(!service.isEnabled()));
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT -> gameData.getKeys().setKey(dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT, false);
                case RIGHT -> gameData.getKeys().setKey(dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT, false);
                case UP -> gameData.getKeys().setKey(dk.sdu.mmmi.cbse.common.data.GameKeys.UP, false);
                case SPACE -> gameData.getKeys().setKey(dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE, false);
            }
        });
    }

    private void update() {
        // Process all entities
        for (IEntityProcessingService processor : entityProcessors) {
            processor.process(gameData, world);
        }

        // Process events
        eventService.process();

        // Run post-processing
        for (IPostEntityProcessingService postProcessor : postProcessors) {
            postProcessor.process(gameData, world);
        }
    }

    private void draw() {
        // Remove old polygons
        gameWindow.getChildren().removeIf(node -> node instanceof Polygon);

        // Update entity polygons
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = createPolygonForEntity(entity);
                polygons.put(entity, polygon);
            }

            // Update polygon position and rotation
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());

            if (!gameWindow.getChildren().contains(polygon)) {
                gameWindow.getChildren().add(polygon);
            }
        }

        // Clear and update debug canvas
        GraphicsContext gc = debugCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, debugCanvas.getWidth(), debugCanvas.getHeight());

        // Render debug visualizations if enabled
        boolean anyDebugEnabled = debugServices.stream()
                .anyMatch(IDebugService::isEnabled);

        if (anyDebugEnabled) {
            debugServices.forEach(service ->
                    service.render(gc, gameData, world));
        }
    }

    public void stop() {
        // Stop all plugins
        for (IPluginLifecycle plugin : pluginLifecycles) {
            plugin.stop(gameData, world);
        }
    }
}