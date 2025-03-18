package dk.sdu.mmmi.cbse.common;

/**
 * Immutable 2D vector class for geometry and physics calculations.
 */
public final class Vector2D {
    private final float x;
    private final float y;

    /**
     * Create a new vector with given x,y coordinates
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public float getY() { return y; }

    /**
     * Add another vector to this one
     *
     * @param other Vector to add
     * @return A new vector representing the sum
     */
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.getX(), this.y + other.getY());
    }

    /**
     * Subtract another vector from this one
     *
     * @param other Vector to subtract
     * @return A new vector representing the difference
     */
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.getX(), this.y - other.getY());
    }

    /**
     * Scale this vector by a scalar value
     *
     * @param scalar Value to scale by
     * @return A new scaled vector
     */
    public Vector2D scale(float scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    /**
     * Calculate dot product with another vector
     *
     * @param other Vector to dot with
     * @return Dot product result
     */
    public float dot(Vector2D other) {
        return this.x * other.getX() + this.y * other.getY();
    }

    /**
     * Calculate the magnitude (length) of this vector
     *
     * @return Vector magnitude
     */
    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the squared magnitude (faster than magnitude)
     *
     * @return Vector magnitude squared
     */
    public float magnitudeSquared() {
        return x * x + y * y;
    }

    /**
     * Normalize this vector (make it length 1)
     *
     * @return A new normalized vector
     * @throws ArithmeticException if vector length is zero
     */
    public Vector2D normalize() {
        float mag = magnitude();
        if (mag == 0) {
            throw new ArithmeticException("Cannot normalize a zero-length vector");
        }
        return new Vector2D(x / mag, y / mag);
    }

    /**
     * Calculate distance to another vector
     *
     * @param other Vector to calculate distance to
     * @return Distance between the two vectors
     */
    public float distance(Vector2D other) {
        float dx = other.x - x;
        float dy = other.y - y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculate the angle of this vector (in radians)
     *
     * @return Angle in radians
     */
    public float angle() {
        return (float) Math.atan2(y, x);
    }

    /**
     * Create a new vector by rotating this one
     *
     * @param angleInRadians Angle to rotate by in radians
     * @return New rotated vector
     */
    public Vector2D rotate(float angleInRadians) {
        float cos = (float) Math.cos(angleInRadians);
        float sin = (float) Math.sin(angleInRadians);
        return new Vector2D(
                x * cos - y * sin,
                x * sin + y * cos
        );
    }

    @Override
    public String toString() {
        return "Vector2D [x = " + x + "; y = " + y + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2D other = (Vector2D) obj;
        return Float.compare(x, other.x) == 0 && Float.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Float.hashCode(x) + Float.hashCode(y);
    }
}