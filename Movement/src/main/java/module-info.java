module Movement {
    requires Common;

    exports dk.sdu.mmmi.cbse.movementsystem;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService with
            dk.sdu.mmmi.cbse.movementsystem.MovementSystem;

    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService with
            dk.sdu.mmmi.cbse.movementsystem.ScreenWrapSystem;
}