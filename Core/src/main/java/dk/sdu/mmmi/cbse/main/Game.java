package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.input.Input;
import dk.sdu.mmmi.cbse.common.services.*;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main game application class.
 * Initializes the game environment and manages the lifecycle.
 */
public class Game extends Application {
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
    private final GameData gameData = new GameData();
    private final World world = new World();
    private GameLoop gameLoop;

    @Override
    public void start(Stage primaryStage) {
        // Create game window
        Pane gameWindow = new Pane();
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: black;");

        // Create game canvas
        Canvas gameCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(gameCanvas);

        // Setup scene
        Scene scene = new Scene(gameWindow);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Asteroids ECS");
        primaryStage.setResizable(false);

        // Setup input handling
        setupInput(scene);

        // Create the game loop
        gameLoop = new GameLoop(gameData, world, gameCanvas.getGraphicsContext2D());

        // Start all game plugins using ServiceLocator
        List<IGamePluginService> plugins = ServiceLocator.locateAll(IGamePluginService.class);
        LOGGER.log(Level.INFO, "Starting {0} game plugins", plugins.size());
        plugins.forEach(plugin -> plugin.start(gameData, world));

        // Show the window
        primaryStage.show();

        // Start the game loop
        gameLoop.start();
    }

    @Override
    public void stop() {
        // Stop the game loop
        if (gameLoop != null) {
            gameLoop.stop();
        }

        // Stop all game plugins
        List<IGamePluginService> plugins = ServiceLocator.locateAll(IGamePluginService.class);
        LOGGER.log(Level.INFO, "Stopping {0} game plugins", plugins.size());
        plugins.forEach(plugin -> plugin.stop(gameData, world));
    }

    /**
     * Setup input handling for the game.
     */
    private void setupInput(Scene scene) {
        // Map JavaFX key events to our input system
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            Input.KeyCode code = mapKeyCode(e.getCode());
            if (code != null) {
                Input.setKey(code, true);
            }

            // Toggle debug mode with F3
            if (e.getCode() == KeyCode.F3) {
                gameData.setDebugMode(!gameData.isDebugMode());
            }
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            Input.KeyCode code = mapKeyCode(e.getCode());
            if (code != null) {
                Input.setKey(code, false);
            }
        });

        // Handle mouse events
        scene.setOnMouseMoved(e -> {
            Input.setAxis(Input.AxisName.MOUSEX, (float) e.getX());
            Input.setAxis(Input.AxisName.MOUSEY, (float) e.getY());
        });
    }

    /**
     * Map JavaFX key codes to our input system's key codes.
     */
    private Input.KeyCode mapKeyCode(KeyCode code) {
        switch (code) {
            case UP:    return Input.KeyCode.UP;
            case DOWN:  return Input.KeyCode.DOWN;
            case LEFT:  return Input.KeyCode.LEFT;
            case RIGHT: return Input.KeyCode.RIGHT;
            case SPACE: return Input.KeyCode.SPACE;
            case ESCAPE: return Input.KeyCode.ESCAPE;
            case W:     return Input.KeyCode.W;
            case A:     return Input.KeyCode.A;
            case S:     return Input.KeyCode.S;
            case D:     return Input.KeyCode.D;
            // Map other keys as needed
            default:    return null;
        }
    }

    /**
     * Launch the game application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}