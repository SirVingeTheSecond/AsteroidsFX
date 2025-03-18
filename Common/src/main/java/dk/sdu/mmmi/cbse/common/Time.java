package dk.sdu.mmmi.cbse.common;

/**
 * Utility class for tracking game time in a Unity-like way.
 * Provides frameRate-independent timing tools for game systems.
 */
public final class Time {
    // Total elapsed simulation time (in seconds)
    private static double time = 0.0;

    // Delta time (time elapsed since last frame) (in seconds)
    private static double deltaTime = 0.0;

    // Fixed delta time for fixed updates (in seconds). Default for 60Hz.
    private static final double fixedDeltaTime = 0.016667;

    // Global time scale (1.0 = normal speed)
    private static double timeScale = 1.0;

    private Time() {
        // Prevent instantiation.
    }

    /**
     * Get total elapsed game time
     * @return Time in seconds
     */
    public static double getTime() {
        return time;
    }

    /**
     * Get time elapsed since last frame
     * @return Delta time in seconds
     */
    public static double getDeltaTime() {
        return deltaTime;
    }

    /**
     * Get fixed timestep interval
     * @return Fixed delta time in seconds
     */
    public static double getFixedDeltaTime() {
        return fixedDeltaTime;
    }

    /**
     * Get current time scale factor
     * @return Time scale (1.0 = normal)
     */
    public static double getTimeScale() {
        return timeScale;
    }

    /**
     * Set time scale factor
     * @param newTimeScale New time scale (>0)
     */
    public static void setTimeScale(double newTimeScale) {
        if (newTimeScale > 0) {
            timeScale = newTimeScale;
        }
    }

    /**
     * Update time values for variable-rate update
     * @param dt Time in seconds since the last frame
     */
    public static void update(double dt) {
        deltaTime = dt * timeScale;
        time += deltaTime;
    }

    /**
     * Update time for fixed-rate update
     */
    public static void fixedUpdate() {
        time += fixedDeltaTime * timeScale;
    }
}