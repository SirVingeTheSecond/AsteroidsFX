import dk.sdu.mmmi.cbse.movementsystem.MovementSystem;

module Movement {
    requires Common;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with MovementSystem;
}