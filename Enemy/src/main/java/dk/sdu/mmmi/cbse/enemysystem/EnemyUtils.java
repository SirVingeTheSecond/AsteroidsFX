package dk.sdu.mmmi.cbse.enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

public class EnemyUtils {
    public static Entity findPlayer(World world) {
        return world.getEntities().stream()
                .filter(e -> e.getClass().getSimpleName().equals("Player"))
                .findFirst()
                .orElse(null);
    }

    public static float calculateDistance(Entity e1, Entity e2) {
        double dx = e2.getX() - e1.getX();
        double dy = e2.getY() - e1.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static boolean hasLineOfSight(Entity source, Entity target) {
        // Simple line of sight check - could be enhanced with obstacle checking
        return true;
    }
}
