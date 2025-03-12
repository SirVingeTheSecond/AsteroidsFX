module RenderSystem {
    requires Common;
    requires javafx.graphics;

    exports dk.sdu.mmmi.cbse.rendersystem;

    provides dk.sdu.mmmi.cbse.common.services.IRenderSystem
            with dk.sdu.mmmi.cbse.rendersystem.DefaultRenderSystem;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.rendersystem.RenderPlugin;

    provides dk.sdu.mmmi.cbse.common.services.IDebugService
            with dk.sdu.mmmi.cbse.rendersystem.DebugRenderSystem;
}