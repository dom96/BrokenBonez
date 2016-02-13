package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * A simple class which defines a Rectangle shape. Provides methods for checking whether a specified
 * VectorF collides with the rectangle: `collidesWith` and `collisionTest`.
 */
public class Rect extends Polygon implements Intersector {
    public Rect(VectorF pos, float width, float height) {
        super();
        VectorF topLeft = pos.copy();
        VectorF topRight = topLeft.added(new VectorF(width, 0));
        VectorF bottomRight = topRight.added(new VectorF(0, height));
        VectorF bottomLeft = topLeft.added(new VectorF(0, height));
        addVertices(new VectorF[] {topLeft, topRight, bottomRight, bottomLeft});
    }
}
