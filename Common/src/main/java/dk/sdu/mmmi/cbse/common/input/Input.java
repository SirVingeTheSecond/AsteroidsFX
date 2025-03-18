package dk.sdu.mmmi.cbse.common.input;

import java.util.EnumMap;

/**
 * Standardized input system with Unity-like functionality.
 * Provides consistent access to keyboard state and virtual axes.
 */
public class Input {
    /**
     * Enumeration of supported key codes
     */
    public enum KeyCode {
        UP, DOWN, LEFT, RIGHT,
        SPACE, ESCAPE, ENTER, TAB,
        A, B, C, D, E, F, G, H, I, J, K, L, M,
        N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
        F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12
    }

    public enum AxisName {
        HORIZONTAL, VERTICAL, MOUSEX, MOUSEY
    }

    // Current key states
    private static final EnumMap<KeyCode, Boolean> keyStates = new EnumMap<>(KeyCode.class);

    // Previous frame key states
    private static final EnumMap<KeyCode, Boolean> previousKeyStates = new EnumMap<>(KeyCode.class);

    // Axis values (for analog input like mouse)
    private static final EnumMap<AxisName, Float> axisValues = new EnumMap<>(AxisName.class);

    static {
        // Initialize all keys to not pressed
        for (KeyCode key : KeyCode.values()) {
            keyStates.put(key, false);
            previousKeyStates.put(key, false);
        }

        // Initialize default axes
        axisValues.put(AxisName.valueOf("Horizontal"), 0f);
        axisValues.put(AxisName.valueOf("Vertical"), 0f);
        axisValues.put(AxisName.valueOf("MouseX"), 0f);
        axisValues.put(AxisName.valueOf("MouseY"), 0f);
    }

    /**
     * Update the input system for the next frame
     */
    public static void update() {
        // Save current state to previous state
        for (KeyCode key : keyStates.keySet()) {
            previousKeyStates.put(key, keyStates.get(key));
        }
    }

    /**
     * Set the state of a key
     * @param key The key to set
     * @param pressed Whether the key is pressed
     */
    public static void setKey(KeyCode key, boolean pressed) {
        keyStates.put(key, pressed);
    }

    /**
     * Set an axis value
     * @param axisName The name of the axis
     * @param value The value of the axis (-1 to 1)
     */
    public static void setAxis(String axisName, float value) {
        axisValues.put(AxisName.valueOf(axisName), value);
    }

    /**
     * Check if a key is currently pressed
     * @param key The key to check
     * @return true if the key is pressed
     */
    public static boolean getKey(KeyCode key) {
        return keyStates.getOrDefault(key, false);
    }

    /**
     * Check if a key was pressed this frame
     * @param key The key to check
     * @return true if the key was just pressed
     */
    public static boolean getKeyDown(KeyCode key) {
        return keyStates.getOrDefault(key, false) && !previousKeyStates.getOrDefault(key, false);
    }

    /**
     * Check if a key was released this frame
     * @param key The key to check
     * @return true if the key was just released
     */
    public static boolean getKeyUp(KeyCode key) {
        return !keyStates.getOrDefault(key, false) && previousKeyStates.getOrDefault(key, false);
    }

    /**
     * Get the value of an axis
     * @param axisName The name of the axis
     * @return The value of the axis (-1 to 1)
     */
    public static float getAxis(AxisName axisName) {
        return axisValues.getOrDefault(axisName, 0f);
    }

    /**
     * Create a JavaFX input handler that forwards input to this system
     * @return A JavaFX event handler for keyboard input
     */
    public static javafx.event.EventHandler<javafx.scene.input.KeyEvent> createKeyboardHandler() {
        return event -> {
            KeyCode key = mapJavaFXKey(event.getCode());
            if (key != null) {
                if (event.getEventType() == javafx.scene.input.KeyEvent.KEY_PRESSED) {
                    setKey(key, true);
                } else if (event.getEventType() == javafx.scene.input.KeyEvent.KEY_RELEASED) {
                    setKey(key, false);
                }
            }
        };
    }

    /**
     * Map JavaFX key codes to our KeyCode enum
     * @param javafxKey The JavaFX key code
     * @return The corresponding KeyCode or null if not mapped
     */
    private static KeyCode mapJavaFXKey(javafx.scene.input.KeyCode javafxKey) {
        switch (javafxKey) {
            case UP: return KeyCode.UP;
            case DOWN: return KeyCode.DOWN;
            case LEFT: return KeyCode.LEFT;
            case RIGHT: return KeyCode.RIGHT;
            case SPACE: return KeyCode.SPACE;
            case ESCAPE: return KeyCode.ESCAPE;
            case ENTER: return KeyCode.ENTER;
            case TAB: return KeyCode.TAB;
            case F1: return KeyCode.F1;
            case F2: return KeyCode.F2;
            case F3: return KeyCode.F3;
            // Add more key mappings as needed
            default:
                // Try to map letter keys
                if (javafxKey.isLetterKey()) {
                    try {
                        return KeyCode.valueOf(javafxKey.name());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                }
                return null;
        }
    }
}