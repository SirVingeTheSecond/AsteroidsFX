module RenderSystem {
    requires Common;
    requires javafx.graphics;

    exports dk.sdu.mmmi.cbse.rendersystem;

    provides dk.sdu.mmmi.cbse.common.services.IRenderSystem
            with dk.sdu.mmmi.cbse.rendersystem.DefaultRenderSystem;
}