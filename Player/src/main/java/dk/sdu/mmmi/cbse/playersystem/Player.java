package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Player class - deprecated in favor of component-based approach.
 * @deprecated Use PlayerFactory to create player entities with components instead.
 */
@Deprecated
public class Player extends Entity {
    // This class is kept for backward compatibility only, will probably never use it though.
    // New code should use PlayerFactory and standard entity instances with components.
}