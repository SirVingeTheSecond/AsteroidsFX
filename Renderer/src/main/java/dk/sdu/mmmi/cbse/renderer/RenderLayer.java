package dk.sdu.mmmi.cbse.renderer;

/**
 * Defines render layers for proper z-ordering.
 * Entities are rendered in layer order from lowest to highest.
 */
public enum RenderLayer {
    BACKGROUND(0),
    WORLD_OBJECTS(100),
    ASTEROIDS(200),
    ENEMIES(300),
    PROJECTILES(400),
    PLAYER(500),
    EFFECTS(600),
    UI(1000),
    DEBUG(2000);

    private final int zIndex;

    RenderLayer(int zIndex) {
        this.zIndex = zIndex;
    }

    public int getZIndex() {
        return zIndex;
    }
}