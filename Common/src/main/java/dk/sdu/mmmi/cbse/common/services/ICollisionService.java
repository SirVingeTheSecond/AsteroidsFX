package dk.sdu.mmmi.cbse.common.services;

/**
 * Interface for collision event handling
 */
public interface ICollisionService extends IPostEntityProcessingService {
    void setDebugEnabled(boolean enabled);
    boolean isDebugEnabled();
}