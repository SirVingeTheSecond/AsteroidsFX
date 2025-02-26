module InputSystem {
    requires Common;
    requires javafx.graphics;  // For Scene and KeyEvent handling

    exports dk.sdu.mmmi.cbse.inputsystem;

    provides dk.sdu.mmmi.cbse.common.services.IInputService
            with dk.sdu.mmmi.cbse.inputsystem.KeyboardInputSystem;
}