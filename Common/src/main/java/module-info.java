module Common {
    requires java.logging;
    requires javafx.graphics;

    exports dk.sdu.mmmi.cbse.common.services;
    exports dk.sdu.mmmi.cbse.common.data;
    exports dk.sdu.mmmi.cbse.common.events;

    provides dk.sdu.mmmi.cbse.common.services.IGameEventService with dk.sdu.mmmi.cbse.common.events.GameEventService;
}