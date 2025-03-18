package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.Time;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.input.Input;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGameEventService;
import dk.sdu.mmmi.cbse.common.services.IRenderSystem;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

/**
 * Standardized game loop implementation.
 * Provides both variable and fixed timestep updates.
 */
public class GameLoop extends AnimationTimer {
    private final GameData gameData;
    private final World world;
    private final GraphicsContext context;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final List<IRenderSystem> renderSystems;
    private final IGameEventService eventService;

    // Fixed timestep accumulator
    private double accumulator = 0;
    private long lastFrameTime = 0;

    public GameLoop(GameData gameData,
                    World world,
                    GraphicsContext context,
                    List<IEntityProcessingService> entityProcessors,
                    List<IPostEntityProcessingService> postProcessors,
                    List<IRenderSystem> renderSystems,
                    IGameEventService eventService) {
        this.gameData = gameData;
        this.world = world;
        this.context = context;
        this.entityProcessors = entityProcessors;
        this.postProcessors = postProcessors;
        this.renderSystems = renderSystems;
        this.eventService = eventService;
    }

    @Override
    public void start() {
        lastFrameTime = System.nanoTime();
        super.start();
    }

    @Override
    public void handle(long now) {
        // Calculate delta time in seconds
        double deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
        lastFrameTime = now;

        // Cap delta time to prevent spiral of death
        if (deltaTime > 0.25) {
            deltaTime = 0.25;
        }

        // Update global time
        Time.update(deltaTime);

        // Process variable update (rendering, input)
        variableUpdate();

        // Accumulate fixed timestep
        accumulator += Time.getDeltaTime();

        // Process fixed timesteps (physics)
        while (accumulator >= Time.getFixedDeltaTime()) {
            fixedUpdate();
            accumulator -= Time.getFixedDeltaTime();
        }

        // Update input state for next frame
        Input.update();
    }

    /**
     * Variable timestep update for rendering and non-physics systems
     */
    private void variableUpdate() {
        // Process game events
        eventService.process();

        // Process all entities
        for (IEntityProcessingService processor : entityProcessors) {
            processor.process(gameData, world);
        }

        // Render
        if (context != null && renderSystems != null) {
            context.clearRect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayHeight());

            for (IRenderSystem renderSystem : renderSystems) {
                renderSystem.render(context, gameData, world);
            }
        }
    }

    /**
     * Fixed timestep update for physics and deterministic systems
     */
    private void fixedUpdate() {
        Time.fixedUpdate();

        // Process post-processing (usually collision)
        for (IPostEntityProcessingService postProcessor : postProcessors) {
            postProcessor.process(gameData, world);
        }
    }
}