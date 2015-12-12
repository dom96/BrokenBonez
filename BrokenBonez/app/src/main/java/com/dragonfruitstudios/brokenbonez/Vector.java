package com.dragonfruitstudios.brokenbonez;

import android.util.Log;

/**
 * This class implements a Euclidean vector. That is a geometric object consisting of a magnitude
 * and direction.
 * Origin is assumed to be (0, 0).
 */
public class Vector {
    public double x;
    public double y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void rotate(double radians) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double newX = this.x * cos - this.y * sin;
        double newY = this.x * sin + this.y * cos;

        this.x = newX;
        this.y = newY;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;

    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public void add(Vector v) {
        x += v.x;
        y += v.y;
    }

    public void multAdd(Vector v, double scalar) {
        x += v.x * scalar;
        y += v.y * scalar;
    }

    public void mult(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    public void normalise() {
        double mag = magnitude();
        if (mag != 0) {
            x = x / mag;
            y = y / mag;
        }
        else {
            throw new ArithmeticException("Magnitude of vector is 0.");
        }
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public double dist(double x, double y) {
        return dist_static(this.x, this.y, x, y);
    }

    static public double dist_static(double x1, double y1, double x2, double y2) {
        double x_dist = x1 - x2;
        double y_dist = y1 - y2;

        return Math.sqrt(x_dist * x_dist + y_dist * y_dist);
    }

    public double dot_product(Vector v) {
        return x * v.x + y * v.y;
    }

    @Override
    public String toString() {
        return "Vector(" + x + ", " + y + ")";
    }
}
