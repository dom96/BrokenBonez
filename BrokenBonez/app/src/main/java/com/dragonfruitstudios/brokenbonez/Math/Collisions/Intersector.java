package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import android.app.Instrumentation;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;

/**
 * Defines a class which supports intersection.
 */

// TODO: Clean up the old collidesWith methods in the Line, Polygon etc classes.
public abstract class Intersector implements Drawable {

    protected Manifold collisionNotImplemented(Intersector shape) {
        throw new RuntimeException("CollisionTest needs to be implemented for " +
                this.getClass() + " and " + shape.getClass());
    }

    public abstract Manifold collisionTest(Intersector shape);

    public abstract Intersector copy();
}
