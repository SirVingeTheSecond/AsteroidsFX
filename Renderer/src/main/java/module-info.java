module Renderer {
    requires Common;
    requires javafx.graphics;
    requires java.logging;

    exports dk.sdu.mmmi.cbse.renderer;

    provides dk.sdu.mmmi.cbse.common.services.IRenderSystem
            with dk.sdu.mmmi.cbse.renderer.DefaultRenderSystem;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.renderer.RenderPlugin;

    provides dk.sdu.mmmi.cbse.common.services.IDebugService
            with dk.sdu.mmmi.cbse.renderer.DebugRenderSystem;

    uses dk.sdu.mmmi.cbse.common.services.IEntityRendererService;
}