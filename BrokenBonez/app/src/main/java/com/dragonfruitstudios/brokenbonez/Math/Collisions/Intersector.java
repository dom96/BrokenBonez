package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * Defines a class which supports intersection.
 */
public interface Intersector extends Drawable {

    boolean collidesWith(VectorF point);

    Manifold collisionTest(VectorF point);

    float distanceSquared(VectorF point);

    Line[] getLines();
}
