module Common {
    requires java.logging;
    requires javafx.graphics;

    exports dk.sdu.mmmi.cbse.common.services;
    exports dk.sdu.mmmi.cbse.common.data;
    exports dk.sdu.mmmi.cbse.common.events;
    exports dk.sdu.mmmi.cbse.common.components;
    exports dk.sdu.mmmi.cbse.common.ui;

    provides dk.sdu.mmmi.cbse.common.services.IGameEventService with dk.sdu.mmmi.cbse.common.events.GameEventService;
}