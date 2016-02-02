package com.dragonfruitstudios.brokenbonez.BoundingShapes;

import com.dragonfruitstudios.brokenbonez.VectorF;

public class Line {
    public static float distanceSquared(VectorF lineStart, VectorF lineEnd, VectorF point) {
        // Based on http://stackoverflow.com/a/1501725/492186
        final float len = lineStart.distSquared(lineEnd);
        if (len == 0.0f) { // When lineStart == lineEnd
            return point.distSquared(lineStart);
        }

        // var t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
        final float t = (point.subtracted(lineStart)).dot_product(lineEnd.subtracted(lineStart)) / len;
        if (t < 0.0f) {
            return point.distSquared(lineStart);
        }
        else if (t > 1.0f) {
            return point.distSquared(lineEnd);
        }

        /*
        VectorF vAddT = lineStart.added(t);
        VectorF wSubV = lineEnd.subtracted(lineStart);
        vAddT.mult(wSubV); // vAddT will now contain the projection.
        */
        // TODO: try to use vector methods for this?
        return point.distSquared(
                new VectorF(lineStart.x + t * (lineEnd.x - lineStart.x),
                        lineStart.y + t * (lineEnd.y - lineStart.y)
                ));
    }

}
