module Movement {
    requires Common;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService with dk.sdu.mmmi.cbse.movementsystem.MovementSystem;
}