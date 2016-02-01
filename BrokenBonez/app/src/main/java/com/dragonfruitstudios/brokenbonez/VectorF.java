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

    public void multAdd(VectorF v, float scalar) {
        x += v.x * scalar;
        y += v.y * scalar;
    }

    public void mult(float scalar) {
        x *= scalar;
        y *= scalar;
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
        return dist_static(this.x, this.y, x, y);
    }

    static public float dist_static(float x1, float y1, float x2, float y2) {
        float x_dist = x1 - x2;
        float y_dist = y1 - y2;

        return (float)Math.sqrt(x_dist * x_dist + y_dist * y_dist);
    }

    public float dot_product(VectorF v) {
        return x * v.x + y * v.y;
    }

    @Override
    public String toString() {
        return "VectorF(" + x + ", " + y + ")";
    }
}
