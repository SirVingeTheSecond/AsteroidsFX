package dk.sdu.mmmi.cbse.common.services;

/**
 * Interface for collision detection and resolution.
 * Extends post-processing to run after entity movement.
 */
public interface ICollisionService extends IPostEntityProcessingService {
    /**
     * Enable or disable debug visualization
     * @param enabled Whether debug is enabled
     */
    void setDebugEnabled(boolean enabled);

    /**
     * Check if debug visualization is enabled
     * @return true if debug is enabled
     */
    boolean isDebugEnabled();
}