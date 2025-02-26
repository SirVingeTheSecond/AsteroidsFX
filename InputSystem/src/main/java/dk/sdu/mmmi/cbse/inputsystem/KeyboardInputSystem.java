package dk.sdu.mmmi.cbse.inputsystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.services.IInputService;
import javafx.scene.Scene;

public class KeyboardInputSystem implements IInputService {

    public void setupInputHandling(Scene scene, GameData gameData) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT -> gameData.getKeys().setKey(GameKeys.LEFT, true);
                case RIGHT -> gameData.getKeys().setKey(GameKeys.RIGHT, true);
                case UP -> gameData.getKeys().setKey(GameKeys.UP, true);
                case SPACE -> gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT -> gameData.getKeys().setKey(GameKeys.LEFT, false);
                case RIGHT -> gameData.getKeys().setKey(GameKeys.RIGHT, false);
                case UP -> gameData.getKeys().setKey(GameKeys.UP, false);
                case SPACE -> gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });
    }

    @Override
    public void processInput(GameData gameData) {
        // Could process inputs if needed beyond direct key handling
        // Update gameData keys
        gameData.getKeys().update();
    }

    @Override
    public String getInputType() {
        return "Keyboard";
    }
}