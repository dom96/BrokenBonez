package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import android.app.Instrumentation;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;

/**
 * Defines a class which supports intersection.
 */
public abstract class Intersector implements Drawable {

    /**
     * Used to report that
     */
    protected Manifold.Collection collisionNotImplemented(Intersector shape) {
        throw new RuntimeException("CollisionTest needs to be implemented for " +
                this.getClass() + " and " + shape.getClass());
    }

    public abstract Manifold.Collection collisionTest(Intersector shape);

    public abstract Intersector copy();

    /**
     * Returns the position of the center of this shape.
     *
     * Note: There are some shapes which return the top-left (TODO).
     */
    public abstract VectorF getPos();

    /**
     * Returns a rough size of the shape. For example, a circle will return the size of the
     * square which fits the circle exactly.
     *
     * This is cached by the bounding shapes for efficiency.
     */
    public abstract VectorF getSize();
}
