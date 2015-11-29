package com.dragonfruitstudios.brokenbonez;

import android.graphics.PointF;
import android.util.Log;

/**
 * This class implements a Euclidean vector. That is a geometric object consisting of a magnitude
 * and direction.
 */
public class Vector {
    double x;
    double y;

    double direction; // In radians.

    Vector(float x, float y, float direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    Vector(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public PointF resolve() {
        return new PointF((float)(x * Math.sin(direction)), (float)(y * Math.cos(direction)));
    }

    public Vector toReverse() {
        return new Vector(x, y, -direction);
    }
}
