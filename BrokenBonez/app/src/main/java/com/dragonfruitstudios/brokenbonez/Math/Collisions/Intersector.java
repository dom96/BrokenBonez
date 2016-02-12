package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;

/**
 * Defines a class which supports intersection.
 */
public interface Intersector extends Drawable {

    /**
     * Determines whether the underlying shape collides with the specified point.
     */
    boolean collidesWith(VectorF point);

    /**
     * Checks if the specified shape collides with this circle.
     * @return A Manifold containing information about the collision.
     */
    Manifold collisionTest(VectorF point);

    /**
     * Find the distance from `point` to the underlying shape.
     * To get real distance square root the value returned by this method.
     * @return The distance squared between `point` and specified line segment.
     */
    float distanceSquared(VectorF point);

    ArrayList<Line> getLines();
}
