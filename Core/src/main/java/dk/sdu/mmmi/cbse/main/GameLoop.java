package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.Time;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.input.Input;
import dk.sdu.mmmi.cbse.common.services.*;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Advanced game loop with separate update and render threads.
 * Uses a Unity-like approach with variable update for game logic and fixed update for physics.
 */
public class GameLoop {
    private static final Logger LOGGER = Logger.getLogger(GameLoop.class.getName());
    private static final double TARGET_FPS = 60.0;
    private static final double TARGET_FRAME_TIME = 1.0 / TARGET_FPS;
    private static final double FIXED_TIME_STEP = 0.016667; // 60 Hz physics update

    // Shared game state
    private final GameData gameData;
    private final World world;
    private final GraphicsContext context;

    // Systems
    private final List<IInputProcessingService> inputProcessors;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final List<IRenderSystem> renderSystems;
    private final IGameEventService eventService;

    // Thread management
    private ScheduledExecutorService updateExecutor;
    private RenderTimer renderTimer;
    private volatile boolean running = false;

    // Physics accumulator
    private double accumulator = 0;
    private long lastUpdateTime;

    // Thread communication
    private final ConcurrentLinkedQueue<Runnable> uiThreadTasks = new ConcurrentLinkedQueue<>();

    /**
     * Creates a new GameLoop with all required systems.
     */
    public GameLoop(GameData gameData, World world, GraphicsContext context) {
        this.gameData = gameData;
        this.world = world;
        this.context = context;

        // Get all required services using ServiceLocator
        this.inputProcessors = ServiceLocator.locateAll(IInputProcessingService.class);
        this.entityProcessors = ServiceLocator.locateAll(IEntityProcessingService.class);
        this.postProcessors = ServiceLocator.locateAll(IPostEntityProcessingService.class);
        this.renderSystems = ServiceLocator.locateAll(IRenderSystem.class);
        this.eventService = ServiceLocator.getService(IGameEventService.class);

        // Sort render systems by priority
        this.renderSystems.sort((a, b) -> Integer.compare(a.getPriority(), b.getPriority()));
    }

    /**
     * Starts the game loop with update and render threads.
     */
    public void start() {
        if (running) return;

        LOGGER.log(Level.INFO, "Starting game loop");
        running = true;
        lastUpdateTime = System.nanoTime();

        // Create and start update thread
        updateExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "GameUpdateThread");
            t.setDaemon(true);
            return t;
        });

        // Schedule the update loop at a slightly higher rate than rendering
        // to ensure we never starve the physics simulation
        updateExecutor.scheduleAtFixedRate(this::updateLoop, 0,
                (long)(TARGET_FRAME_TIME * 1000 / 1.5), TimeUnit.MILLISECONDS);

        // Start the render timer on the JavaFX thread
        renderTimer = new RenderTimer();
        renderTimer.start();

        LOGGER.log(Level.INFO, "Game loop started");
    }

    /**
     * The main update loop that runs on a background thread.
     * Handles input, game logic updates, and physics.
     */
    private void updateLoop() {
        try {
            // Calculate delta time
            long now = System.nanoTime();
            double deltaTime = (now - lastUpdateTime) / 1_000_000_000.0;
            lastUpdateTime = now;

            // Cap delta time to prevent spiral of death
            if (deltaTime > 0.25) {
                deltaTime = 0.25;
                LOGGER.log(Level.WARNING, "Delta time capped at 250ms - game running slowly");
            }

            // Update global time
            Time.update(deltaTime);

            // Process input
            processInput();

            // Process game events
            eventService.process();

            // Variable timestep update (game logic)
            processEntities();

            // Fixed timestep update (physics)
            accumulator += deltaTime;
            while (accumulator >= FIXED_TIME_STEP) {
                Time.fixedUpdate();
                processPhysics();
                accumulator -= FIXED_TIME_STEP;
            }

            // Store interpolation factor for rendering
            gameData.setInterpolation((float)(accumulator / FIXED_TIME_STEP));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in update loop", e);
        }
    }

    /**
     * Process all input systems.
     */
    private void processInput() {
        for (IInputProcessingService processor : inputProcessors) {
            processor.process(gameData);
        }
    }

    /**
     * Process all entity systems (game logic).
     */
    private void processEntities() {
        for (IEntityProcessingService processor : entityProcessors) {
            processor.process(gameData, world);
        }
    }

    /**
     * Process all physics systems (collisions, etc).
     */
    private void processPhysics() {
        for (IPostEntityProcessingService postProcessor : postProcessors) {
            postProcessor.process(gameData, world);
        }
    }

    /**
     * Schedule a task to run on the JavaFX UI thread.
     */
    public void runOnUiThread(Runnable task) {
        uiThreadTasks.add(task);
    }

    /**
     * AnimationTimer for rendering on the JavaFX thread.
     */
    private class RenderTimer extends AnimationTimer {
        private long lastFrameTime = 0;
        private int frameCount = 0;
        private long frameCountTime = 0;
        private double fps = 0;

        @Override
        public void handle(long now) {
            // Process any pending tasks for the UI thread
            Runnable task;
            while ((task = uiThreadTasks.poll()) != null) {
                task.run();
            }

            // Only render at target frame rate to avoid overwhelming the renderer
            if (lastFrameTime == 0 || (now - lastFrameTime) >= (TARGET_FRAME_TIME * 1_000_000_000)) {
                lastFrameTime = now;

                // Render the game
                renderGame();

                // Calculate FPS
                frameCount++;
                if (now - frameCountTime >= 1_000_000_000) {
                    fps = frameCount / ((now - frameCountTime) / 1_000_000_000.0);
                    frameCount = 0;
                    frameCountTime = now;
                    gameData.setCurrentFps(fps);
                }
            }

            // Update input state for next frame
            Input.update();
        }
    }

    /**
     * Renders the game using all render systems.
     */
    private void renderGame() {
        if (context != null && renderSystems != null) {
            // Clear the canvas
            context.clearRect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayHeight());

            // Let each render system draw its part
            for (IRenderSystem renderSystem : renderSystems) {
                renderSystem.render(context, gameData, world);
            }
        }
    }

    /**
     * Stops the game loop and cleans up resources.
     */
    public void stop() {
        if (!running) return;

        LOGGER.log(Level.INFO, "Stopping game loop");
        running = false;

        // Stop the update executor
        if (updateExecutor != null) {
            updateExecutor.shutdown();
            try {
                updateExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (!updateExecutor.isTerminated()) {
                    updateExecutor.shutdownNow();
                }
            }
        }

        // Stop the render timer
        if (renderTimer != null) {
            renderTimer.stop();
        }

        LOGGER.log(Level.INFO, "Game loop stopped");
    }
}