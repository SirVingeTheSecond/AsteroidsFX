package dk.sdu.mmmi.cbse.common.ui;

/**
 * Interface for UI update observers.
 */
public interface IUIObserver {
    /**
     * Called when UI needs updating.
     */
    void onUIUpdate();
}