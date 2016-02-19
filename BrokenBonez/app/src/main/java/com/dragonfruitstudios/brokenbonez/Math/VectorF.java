package com.dragonfruitstudios.brokenbonez.Math;

/**
 * This class implements a Euclidean vector. That is a geometric object consisting of a magnitude
 * and direction.
 * Origin is assumed to be (0, 0).
 */
public class VectorF {

    // Public because accessing these directly is much cleaner than using the getters/setters,
    public float x;
    public float y;

    /**
     * Construct a new VectorF object with the specified `x` and `y` coordinates.
     */
    public VectorF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construct a new VectorF object based on the specified VectorF object.
     * @param vec The VectorF object to make a new copy of.
     */
    public VectorF(VectorF vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    /**
     * Creates a new copy of this VectorF.
     *
     * Handy shortcut to an otherwise verbose expression:
     *   VectorF vec = new VectorF(oldVec);
     */
    public VectorF copy() {
        return new VectorF(this);
    }

    /**
     * Rotate this instance of VectorF by `radians` amount of radians.
     * @param radians The amount of radians to rotate by.
     */
    public void rotate(float radians) {
        float cos = (float)Math.cos(radians);
        float sin = (float)Math.sin(radians);

        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;

        this.x = newX;
        this.y = newY;
    }

    /**
     * Return a new VectorF that is the same as this VectorF but rotated by `radians` amount of
     * radians.
     * @return A new rotated VectorF.
     */
    public VectorF rotated(float radians) {
        VectorF result = new VectorF(this);
        result.rotate(radians);
        return result;
    }

    /**
     * Calculates the magnitude or length of this vector from origin which is (0, 0)
     * @return The length of this vector.
     */
    public float magnitude() {
        return (float)Math.sqrt(x*x + y*y);
    }

    /**
     * Add VectorF `v` to this VectorF. The x and y coordinates of the two vectors are simply added.
     */
    public void add(VectorF v) {
        x += v.x;
        y += v.y;
    }

    /**
     * Add a decimal number `scalar` to this VectorF's x and y coordinates.
     */
    public void add(float scalar) {
        x += scalar;
        y += scalar;
    }

    /**
     * Add the specified x and y coordinates to this VectorF.
     */
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Returns a new copy of this vector with the vector `v` added to it.
     *
     * The `add` method performs the same operation but does it in-place instead of returning a new
     * copy.
     */
    public VectorF added(VectorF v) {
        VectorF result = new VectorF(this);
        result.add(v);
        return result;
    }

    /**
     * Returns a new copy of this vector with the decimal number `scalar` added to it.
     *
     * The `add` method performs the same operation but does it in-place instead of returning a new
     * copy.
     */
    public VectorF added(float scalar) {
        VectorF result = new VectorF(this);
        result.add(scalar);
        return result;
    }

    /**
     * Subtract the specified VectorF from this instance of VectorF. The VectorF's x and y values
     * are simply subtracted.
     * @param v
     */
    public void sub(VectorF v) {
        x -= v.x;
        y -= v.y;
    }

    /**
     * Same as `sub` but returns a copy of VectorF.
     */
    public VectorF subtracted(VectorF v) {
        VectorF result = new VectorF(this);
        result.sub(v);
        return result;
    }

    /**
     * Multiply the specified VectorF `v` by the decimal number `scalar` and add the resulting
     * vector to this instance of VectorF.
     * @param v The vector to add to this instance of VectorF.
     * @param scalar The number to multiply `v` before adding it to this instance of VectorF.
     */
    public void multAdd(VectorF v, float scalar) {
        x += v.x * scalar;
        y += v.y * scalar;
    }

    /**
     * Simply multiply the x and y coordinates of this vector instance by the decimal number
     * `scalar`.
     * @param scalar
     */
    public void mult(float scalar) {
        x *= scalar;
        y *= scalar;
    }

    public void mult(VectorF v) {
        x *= v.x;
        y *= v.y;
    }

    public void div(float scalar) {
        x /= scalar;
        y /= scalar;
    }

    /**
     * Convert this vector instance into a Unit vector (i.e. a vector of magnitude 1).
     */
    public void normalise() {
        float mag = magnitude();
        if (mag != 0) {
            x = x / mag;
            y = y / mag;
        }
        else {
            throw new ArithmeticException("Magnitude of vector is 0.");
        }
    }

    /**
     * Returns a new normalised Unit vector based on this vector.
     */
    public VectorF normalised() {
        VectorF result = new VectorF(this);
        result.normalise();
        return result;
    }

    /**
     * Calculate the angle that this vector makes with the horizontal.
     */
    public float angle() {
        // This answer on StackOverflow explains this well: http://gamedev.stackexchange.com/a/14603
        return (float)Math.atan2(y, x);
    }

    /**
     * Calculate the distance between this vectpr and the vector at points `x` and `y`.
     */
    public float dist(float x, float y) {
        return distStatic(this.x, this.y, x, y);
    }

    /**
     * Calculate the distance between the Vector (x1, y1) and (x2, y2). This method is static
     * and is useful when you do not want to create a new VectorF object.
     */
    static public float distStatic(float x1, float y1, float x2, float y2) {
        float x_dist = x1 - x2;
        float y_dist = y1 - y2;

        return (float)Math.sqrt(x_dist * x_dist + y_dist * y_dist);
    }

    /**
     * More efficient version of `distStatic` that does not use `sqrt`.
     * Useful in some circumstances.
     */
    static public float distStaticSquared(float x1, float y1, float x2, float y2) {
        float x_dist = x1 - x2;
        float y_dist = y1 - y2;

        return x_dist * x_dist + y_dist * y_dist;
    }


    /**
     * More efficient version of `dist` that does not use `sqrt`.
     * Useful in some circumstances.
     */
    public float distSquared(VectorF v) {
        return distStaticSquared(this.x, this.y, v.getX(), v.getY());
    }

    /**
     * Calculates the dot product between this vector instance and `v`.
     */
    public float dotProduct(VectorF v) {
        return x * v.x + y * v.y;
    }

    public float crossProduct(VectorF v) {
        return this.x * v.y - this.y * x;
    }

    /**
     * Returns true if this vector instance, b, and c all lie on the same line.
     */
    public boolean isCollinear(VectorF b, VectorF c, float epsilon) {
        return MathUtils.equal((b.x - this.x) * (c.y - this.y), (c.x - this.x) * (b.y - this.y), epsilon);
    }

    // <editor-fold desc="Getters/Setters">

    // TODO: Remove these? Since the actual fields are public?

    /**
     * Setter for the `x` field of VectorF.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Setter for the `y` field of VectorF.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Setter for both the `x` and `y` fields of VectorF.
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;

    }

    /**
     * Getter for the `x` field of this VectorF, which contains the x coordinate of this vector.
     * @return The x coordinate of this vector.
     */
    public float getX() {
        return x;
    }

    /**
     * Getter for the `y` field of this VectorF, which contains the y coordinate of this vector.
     * @return The y coordinate of this vector.
     */
    public float getY() {
        return y;
    }

    // </editor-fold>

    @Override
    public String toString() {
        return "VectorF(" + x + ", " + y + ")";
    }
}
