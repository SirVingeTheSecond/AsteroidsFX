module Core {
    requires Common;
    requires CommonBullet;
    requires javafx.graphics;
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires java.desktop;

    exports dk.sdu.mmmi.cbse.main;

    opens dk.sdu.mmmi.cbse.main to javafx.graphics, spring.core;

    uses dk.sdu.mmmi.cbse.common.services.IPluginLifecycle;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IGameEventService;
    uses dk.sdu.mmmi.cbse.common.services.IDebugService;
}