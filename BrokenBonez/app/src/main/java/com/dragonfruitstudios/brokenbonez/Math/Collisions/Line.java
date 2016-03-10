package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.MathUtils;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class defines a Line and implements collision detection methods to detect whether shapes
 * are colliding with the Line.
 */
public class Line extends Intersector implements Drawable {
    private VectorF start;
    private VectorF end;

    private VectorF size;

    private long timeOfLastCollision;

    private VectorF calcSize() {
        return this.size = new VectorF(end.subtracted(start).magnitude(), 1);
    }

    public Line(VectorF start, VectorF end) {
        this.start = start;
        this.end = end;
        this.size = calcSize();
    }

    public Line(float x1, float y1, float x2, float y2) {
        this.start = new VectorF(x1, y1);
        this.end = new VectorF(x2, y2);
        this.size = calcSize();
    }

    /**
     * Find the distance from this line to the specified `point`.
     * To get real distance square root the value returned by this method.
     */
    public float distanceSquared(VectorF point) {
        return distanceSquared(start, end, point);
    }

    private static boolean between(float start, float end, float p, float epsilon) {
        return MathUtils.between(start, p, end, epsilon) || MathUtils.between(end, p, start, epsilon);
    }

    public static boolean collidesWith(VectorF lineStart, VectorF lineEnd, VectorF point,
                                       float epsilon) {
        // Based on http://stackoverflow.com/a/328110/492186

        if (lineStart.isCollinear(lineEnd, point, epsilon)) {
            // The slope from `start` to `end` is the same as the slope from `point` to `end`.
            // Now need to make sure that `point` is between `start` and `end`.
            if (!MathUtils.equal(lineStart.x, lineEnd.x)) {
                return between(lineStart.x, lineEnd.x, point.x, epsilon);
            }
            else {
                return between(lineStart.y, lineEnd.y, point.y, epsilon);
            }

        }
        return false;
    }

    public static boolean collidesWith(VectorF lineStart, VectorF lineEnd, VectorF point) {
        return collidesWith(lineStart, lineEnd, point, MathUtils.defEpsilon);
    }

    public boolean collidesWith(VectorF point, float epsilon) {
        return collidesWith(start, end, point, epsilon);
    }

    /**
     * Determines whether this line collides with the specified `point`.
     */
    public boolean collidesWith(VectorF point) {
        return collidesWith(point, MathUtils.defEpsilon);
    }

    public Manifold collisionTest(VectorF point) {
        if (collidesWith(point)) {
            // Calculate the penetration depth and collision normal.
            // The depth will always be 0
            float depth = 0f;
            // Get vector for line start to finish.
            VectorF startToFinish = end.subtracted(start);
            VectorF normal = new VectorF(-startToFinish.getY(), startToFinish.getX());
            normal.normalise();
            return new Manifold(normal, depth, true);
        }
        return Manifold.noCollision();
    }

    /**
     * Checks if the specified shape collides with this Line.
     * @return A Manifold containing information about the collision.
     */
    @Override
    public Manifold.Collection collisionTest(Intersector shape) {
        return collisionNotImplemented(shape);
    }

    /**
     * Find the distance from `point` to the line segment delimited by `lineStart` and `lineEnd`.
     * To get real distance square root the value returned by this method.
     * @return The distance squared between `point` and specified line segment.
     */
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

        return point.distSquared(
                new VectorF(lineStart.x + t * (lineEnd.x - lineStart.x),
                        lineStart.y + t * (lineEnd.y - lineStart.y)
                ));
    }

    /**
     * Determines whether `point` is near this line (less than 10px away).
     */
    public boolean isNear(VectorF point) {
        float distanceSq = distanceSquared(point);
        return distanceSq < 10*10;
    }

    public float calcRotation() {
        VectorF sub = end.subtracted(start);
        return sub.angle();
    }

    public Line copy() {
        return new Line(start, end);
    }

    private void rotateCoord(VectorF coord, float ang, VectorF pos) {
        float diffX = coord.x - pos.x;
        float diffY = coord.y - pos.y;

        coord.x = (float)((diffX)*Math.cos(ang) + diffY*Math.sin(ang)) + pos.x;
        coord.y = (float)(-(diffX)*Math.sin(ang) + diffY*Math.cos(ang)) + pos.y;
    }

    /**
     * Rotates line segment around `pos`.
     * @param ang Angle in radians
     */
    public void rotate(float ang, VectorF pos) {
        // Based on: http://stackoverflow.com/a/14842362/492186
        rotateCoord(this.start, ang, pos);
        rotateCoord(this.end, ang, pos);
    }

    public VectorF getCenter() {
        return new VectorF((this.start.x + this.end.x) / 2, (this.start.y + this.end.y) / 2);
    }

    // <editor-fold desc="Getters/Setters">

    public ArrayList<Line> getLines() {
        ArrayList<Line> result = new ArrayList<Line>();
        result.add(this);
        return result;
    }

    public VectorF getStart() {
        return start;
    }

    public VectorF getFinish() {
        return end;
    }

    public VectorF getPos() {
        return start;
    }

    public VectorF getSize() {
        return size;
    }

    /**
     * This is used for debugging. Sets the last time that this Line was involved in a collision.
     */
    public void setTimeOfLastCollision(long time) {
        timeOfLastCollision = time;
    }

    // </editor-fold>

    /**
     This method is used to show where the line is on the screen, for debugging purposes only.
     */
    public void draw(GameView view) {
        // Color the line differently if it was involved in a recent collision.
        if (System.nanoTime() - timeOfLastCollision <= 1e8) {
            view.drawLine(start, end, Color.parseColor("#00c80a"));
        }
        else {
            view.drawLine(start, end, Color.parseColor("#ff1122"));
        }
    }

    /**
     * A method to turn this object into a string.
     */
    @Override
    public String toString() {
        return String.format("Line(Start: %s, Finish: %s)", start, end);
    }
}
