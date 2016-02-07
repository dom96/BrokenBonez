package com.dragonfruitstudios.brokenbonez.BoundingShapes;

import com.dragonfruitstudios.brokenbonez.Drawable;
import com.dragonfruitstudios.brokenbonez.VectorF;

/**
 * Defines a class which supports intersection.
 */
public interface Intersector extends Drawable {

    boolean collidesWith(VectorF point);

    Manifold collisionTest(VectorF point);

    float distanceSquared(VectorF point);

    Line[] getLines();
}
