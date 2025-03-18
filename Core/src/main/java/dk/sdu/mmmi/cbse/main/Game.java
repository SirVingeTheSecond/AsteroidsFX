package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.input.Input;
import dk.sdu.mmmi.cbse.common.services.*;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Main game class that initializes and manages the game.
 */
public class Game {
    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Pane gameWindow = new Pane();
    private final Canvas gameCanvas;
    private final Canvas debugCanvas;

    private final List<IDebugService> debugServices;
    private final List<IGamePluginService> plugins;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final List<IRenderSystem> renderSystems;
    private final IGameEventService eventService;

    private GameLoop gameLoop;

    public Game() {
        // Create canvases for rendering
        gameCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        debugCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        debugCanvas.setMouseTransparent(true);

        // Add canvases to the window
        gameWindow.getChildren().addAll(gameCanvas, debugCanvas);

        // Get services using standardized approach
        this.debugServices = loadServices(IDebugService.class);
        this.plugins = loadServices(IGamePluginService.class);
        this.entityProcessors = loadServices(IEntityProcessingService.class);
        this.postProcessors = loadServices(IPostEntityProcessingService.class);
        this.renderSystems = loadServices(IRenderSystem.class);
        this.eventService = ServiceLocator.getService(IGameEventService.class);
    }

    /**
     * Load services of a specific type
     */
    private <T> List<T> loadServices(Class<T> serviceType) {
        return new ArrayList<>(ServiceLoader.load(serviceType)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList());
    }

    /**
     * Start the game
     */
    public void start(Stage window) {
        // Setup game window
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(gameWindow);

        // Setup input system
        scene.setOnKeyPressed(Input.createKeyboardHandler());
        scene.setOnKeyReleased(Input.createKeyboardHandler());

        // Setup mouse input
        scene.setOnMouseMoved(event -> {
            Input.setAxis("MouseX", (float) event.getX());
            Input.setAxis("MouseY", (float) event.getY());
        });

        // Start all plugins
        for (IGamePluginService plugin : plugins) {
            plugin.start(gameData, world);
        }

        // Create game loop
        gameLoop = new GameLoop(
                gameData,
                world,
                gameCanvas.getGraphicsContext2D(),
                entityProcessors,
                postProcessors,
                renderSystems,
                eventService
        );

        // Setup window
        window.setScene(scene);
        window.setTitle("Asteroids");
        window.show();

        // Start the game loop
        gameLoop.start();
    }

    /**
     * Stop the game
     */
    public void stop() {
        // Stop game loop
        if (gameLoop != null) {
            gameLoop.stop();
        }

        // Stop all plugins
        for (IGamePluginService plugin : plugins) {
            plugin.stop(gameData, world);
        }
    }
}