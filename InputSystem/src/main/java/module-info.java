module InputSystem {
    requires Common;
    requires javafx.graphics;

    exports dk.sdu.mmmi.cbse.inputsystem;

    provides dk.sdu.mmmi.cbse.common.services.IInputService
            with dk.sdu.mmmi.cbse.inputsystem.KeyboardInputSystem;

    provides dk.sdu.mmmi.cbse.common.services.IPluginLifecycle
            with dk.sdu.mmmi.cbse.inputsystem.InputPlugin;
}