package com.dragonfruitstudios.brokenbonez.Math;

public class MathUtils {
    public static final float defEpsilon = 0.001f;

    public static boolean equal(float a, float b, float epsilon) {
        if (Math.abs(a - b) < epsilon) {
            return true;
        }
        return false;
    }

    public static boolean equal(float a, float b) {
        return equal(a, b, defEpsilon);
    }

    /**
     * Determines whether `a` <= `p` <= `b` with a specified degree of accuracy `epsilon`.
     */
    public static boolean between(float a, float p, float b, float epsilon) {
        boolean aP = a < p || equal(a, p, epsilon); // a <= p
        boolean pB = p < b || equal(p, b, epsilon); // p <= b
        return aP && pB;
    }

    public static boolean between(float a, float p, float b) {
        return between(a, p, b, defEpsilon);
    }

}
