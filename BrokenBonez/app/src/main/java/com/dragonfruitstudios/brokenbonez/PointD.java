package com.dragonfruitstudios.brokenbonez;

/**
 * Simple implementation of Android's PointF class, but for doubles.
 * The real reason for implementing this class is due to this:
 * http://stackoverflow.com/questions/34010251/android-pointf-constructor-not-working-in-junit-test
 */
public class PointD {
    public double x;
    public double y;

    PointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = y;
        this.y = y;
    }

    @Override
    public String toString() {
        return "PointD(" + x + ", " + y + ")";
    }
}
