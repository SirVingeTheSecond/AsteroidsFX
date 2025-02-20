package dk.sdu.mmmi.cbse.common.enemy;

/**
 * Defines different enemy behavior patterns
 */
public enum EnemyBehavior {
    PATROL,     // Moves in a fixed pattern
    AGGRESSIVE, // Actively seeks and engages player
    DEFENSIVE,  // Maintains distance while attacking
    SNIPER      // Stays still, shoots from distance
}