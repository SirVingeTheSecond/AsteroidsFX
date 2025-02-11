package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * Interface for asteroid splitting functionality.
 */
public interface IAsteroidSplitter {
    /**
     * Creates new asteroids from a split asteroid.
     *
     * Pre-conditions:
     * - Entity e (asteroid) must not be null
     * - World w must not be null
     * - Asteroid must be in a valid state to be split
     *
     * Post-conditions:
     * - New asteroids must be created with valid properties
     * - Split asteroids must maintain momentum rules
     * - Original asteroid state must not be modified
     * - World must remain in consistent state
     *
     * @param e The asteroid entity to split
     * @param w The game world
     * @throws NullPointerException if e or w is null
     * @throws IllegalStateException if asteroid cannot be split
     */
    void createSplitAsteroid(Entity e, World w);
}
