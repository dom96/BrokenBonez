package com.dragonfruitstudios.brokenbonez;

/**
 * This class implements a Euclidean vector. That is a geometric object consisting of a magnitude
 * and direction.
 * Origin is assumed to be (0, 0).
 */
public class VectorF {
    public float x;
    public float y;

    public VectorF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public VectorF(VectorF vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public void rotate(float radians) {
        float cos = (float)Math.cos(radians);
        float sin = (float)Math.sin(radians);

        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;

        this.x = newX;
        this.y = newY;
    }

    public VectorF rotated(float radians) {
        VectorF result = new VectorF(this);
        result.rotate(radians);
        return result;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float magnitude() {
        return (float)Math.sqrt(x*x + y*y);
    }

    public void add(VectorF v) {
        x += v.x;
        y += v.y;
    }

    public void add(float scalar) {
        x += scalar;
        y += scalar;
    }

    /**
     * Returns a new copy of this vector with the vector `v` added to it.
     *
     * In comparison, the `add` method does not return a new copy.
     * @param v
     * @return
     */
    public VectorF added(VectorF v) {
        VectorF result = new VectorF(this);
        result.add(v);
        return result;
    }

    public VectorF added(float scalar) {
        VectorF result = new VectorF(this);
        result.add(scalar);
        return result;
    }

    public void sub(VectorF v) {
        x -= v.x;
        y -= v.y;
    }

    public VectorF subtracted(VectorF v) {
        VectorF result = new VectorF(this);
        result.sub(v);
        return result;
    }

    public void multAdd(VectorF v, float scalar) {
        x += v.x * scalar;
        y += v.y * scalar;
    }

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

    public float angle() {
        return (float)Math.atan2(y, x);
    }

    public float dist(float x, float y) {
        return distStatic(this.x, this.y, x, y);
    }

    static public float distStatic(float x1, float y1, float x2, float y2) {
        float x_dist = x1 - x2;
        float y_dist = y1 - y2;

        return (float)Math.sqrt(x_dist * x_dist + y_dist * y_dist);
    }

    /**
     * Useful in some circumstances when you want to avoid sqrt for efficiency.
     * @param v
     * @return
     */
    public float distSquared(VectorF v) {
        float x_dist = this.x - v.getX();
        float y_dist = this.y - v.getY();

        return x_dist * x_dist + y_dist * y_dist;
    }

    public float dot_product(VectorF v) {
        return x * v.x + y * v.y;
    }

    @Override
    public String toString() {
        return "VectorF(" + x + ", " + y + ")";
    }
}
