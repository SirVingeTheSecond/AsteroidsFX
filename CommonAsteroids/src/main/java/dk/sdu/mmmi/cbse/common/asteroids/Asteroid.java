package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 * Base asteroid entity.
 * Properties and behavior handled by components.
 */
public class Asteroid extends Entity {
    private int splitCount; // Tracks how many times asteroid has been split

    public int getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(int splitCount) {
        this.splitCount = splitCount;
    }
}