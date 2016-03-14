package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import android.util.Log;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * A simple class which defines a Rectangle shape. Provides methods for checking whether a specified
 * VectorF collides with the rectangle: `collidesWith` and `collisionTest`.
 */
public class Rect extends Polygon {
    // TODO: Use the Android Rect as an optimisation. Override intersect methods.

    public Rect(VectorF pos, float width, float height) {
        super();
        VectorF topLeft = pos.copy();
        VectorF topRight = topLeft.added(new VectorF(width, 0));
        VectorF bottomRight = topRight.added(new VectorF(0, height));
        VectorF bottomLeft = topLeft.added(new VectorF(0, height));
        addVertices(new VectorF[] {topLeft, topRight, bottomRight, bottomLeft});
    }

    /**
     * Moves the top-left corner of this Rectangle to the specified position.
     */
    public void setPos(VectorF pos) {
        // TODO: Add tests for this.
        VectorF diff = pos.subtracted(getLines().get(0).getStart());
        for (Line l : this.getLines()) {
            l.getStart().add(diff);
            l.getFinish().add(diff);
        }
        this.recalculateBounds();
    }

    /**
     * Sets this rectangles Line's to the ones specified.
     */
    public void setLines(Line top, Line left, Line bottom, Line right) {
        this.getLines().set(0, top);
        this.getLines().set(1, left);
        this.getLines().set(2, bottom);
        this.getLines().set(3, right);
        this.recalculateBounds();
    }
}
