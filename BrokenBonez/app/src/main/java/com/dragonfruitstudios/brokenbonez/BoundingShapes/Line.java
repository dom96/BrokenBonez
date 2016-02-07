package com.dragonfruitstudios.brokenbonez.BoundingShapes;

import android.graphics.Color;

import com.dragonfruitstudios.brokenbonez.Drawable;
import com.dragonfruitstudios.brokenbonez.GameView;
import com.dragonfruitstudios.brokenbonez.MathUtils;
import com.dragonfruitstudios.brokenbonez.VectorF;

public class Line implements Drawable {
    private VectorF start;
    private VectorF end;

    public Line(VectorF start, VectorF end) {
        this.start = start;
        this.end = end;
    }

    public Line(float x1, float y1, float x2, float y2) {
        this.start = new VectorF(x1, y1);
        this.end = new VectorF(x2, y2);
    }

    public float distanceSquared(VectorF point) {
        return distanceSquared(start, end, point);
    }

    private boolean between(float start, float end, float p, float epsilon) {
        return MathUtils.between(start, p, end, epsilon) || MathUtils.between(end, p, start, epsilon);
    }

    public boolean collidesWith(VectorF point, float epsilon) {
        // Based on http://stackoverflow.com/a/328110/492186

        if (start.isCollinear(end, point, epsilon)) {
            // The slope from `start` to `end` is the same as the slope from `point` to `end`.
            // Now need to make sure that `point` is between `start` and `end`.
            if (MathUtils.equal(start.x, end.x)) {
                return between(start.x, end.x, point.x, epsilon);
            }
            else {
                return between(start.y, end.y, point.y, epsilon);
            }

        }
        return false;
    }

    public boolean collidesWith(VectorF point) {
        return collidesWith(point, MathUtils.defEpsilon);
    }

    public static float distanceSquared(VectorF lineStart, VectorF lineEnd, VectorF point) {
        // Based on http://stackoverflow.com/a/1501725/492186
        final float len = lineStart.distSquared(lineEnd);
        if (len == 0.0f) { // When lineStart == lineEnd
            return point.distSquared(lineStart);
        }

        // var t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
        final float t = (point.subtracted(lineStart)).dotProduct(lineEnd.subtracted(lineStart)) / len;
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

    public boolean isNear(VectorF point) {
        float distanceSq = distanceSquared(point);
        return distanceSq < 10*10;
    }

    public VectorF getStart() {
        return start;
    }

    public VectorF getFinish() {
        return end;
    }

    /**
     This method is just for debugging purposes!
     */
    public void draw(GameView view) {
        view.drawLine(start, end, Color.parseColor("#ff1122"));

    }

    @Override
    public String toString() {
        return String.format("Line(Start: %s, Finish: %s)", start, end);
    }
}
