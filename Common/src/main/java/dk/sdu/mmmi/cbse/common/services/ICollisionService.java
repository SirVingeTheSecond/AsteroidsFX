package dk.sdu.mmmi.cbse.common.services;

/**
 * Interface for collision processing and debugging
 */
public interface ICollisionService extends IPostEntityProcessingService {
    /**
     * Enable or disable collision debugging
     */
    void setDebugEnabled(boolean enabled);

    /**
     * Check if collision debugging is enabled
     */
    boolean isDebugEnabled();
}