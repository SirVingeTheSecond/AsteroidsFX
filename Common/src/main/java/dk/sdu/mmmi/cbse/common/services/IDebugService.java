package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for debug visualization services
 */
public interface IDebugService {
    void setEnabled(boolean enabled);
    boolean isEnabled();
    void render(GraphicsContext gc, GameData gameData, World world);
}
