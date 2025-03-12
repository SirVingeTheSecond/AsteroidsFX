package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.components.TransformComponent;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.input.Input;
import dk.sdu.mmmi.cbse.common.services.*;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class Main {
    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Pane gameWindow = new Pane();
    private final Canvas gameCanvas;
    private final Canvas debugCanvas;

    private final List<IDebugService> debugServices;
    private final List<IGamePluginService> pluginLifecycles;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final List<IRenderSystem> renderSystems;
    private final IGameEventService eventService;

    private GameLoop gameLoop;

    public Main() {
        // Create canvases for rendering
        gameCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        debugCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        debugCanvas.setMouseTransparent(true);

        // Add canvases to the window
        gameWindow.getChildren().addAll(gameCanvas, debugCanvas);

        // Get services
        this.debugServices = new ArrayList<>(ServiceLoader.load(IDebugService.class).stream().map(ServiceLoader.Provider::get).toList());
        this.pluginLifecycles = new ArrayList<>(ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).toList());
        this.entityProcessors = new ArrayList<>(ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).toList());
        this.postProcessors = new ArrayList<>(ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).toList());
        this.renderSystems = new ArrayList<>(ServiceLoader.load(IRenderSystem.class).stream().map(ServiceLoader.Provider::get).toList());
        this.eventService = ServiceLocator.getService(IGameEventService.class);
    }

    public void start(Stage window) {
        // Setup game window
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(gameWindow);

        // Setup input - new Unity-like input system
        scene.setOnKeyPressed(Input.createKeyboardHandler());
        scene.setOnKeyReleased(Input.createKeyboardHandler());

        // Setup mouse input for analog axes
        scene.setOnMouseMoved(event -> {
            Input.setAxis("MouseX", (float) event.getX());
            Input.setAxis("MouseY", (float) event.getY());
        });

        // Start all plugins
        for (IGamePluginService plugin : pluginLifecycles) {
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

    public void stop() {
        // Stop game loop
        if (gameLoop != null) {
            gameLoop.stop();
        }

        // Stop all plugins
        for (IGamePluginService plugin : pluginLifecycles) {
            plugin.stop(gameData, world);
        }
    }
}