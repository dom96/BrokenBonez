package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * A simple class which defines a Triangle shape. It does so by storing line segments which
 * define the Triangle as a Polygon. You can determine whether a VectorF collides with this
 * shape using `collidesWith` and `collisionTest` methods.
 */
public class Triangle extends Polygon implements Intersector {
    /**
     * Creates a new right-angled triangle. The triangle first vertex will be positioned at `pos`,
     * the triangle will be created with the specified `width` and `height`.
     *
     * @param width Width of new triangle.
     * @param height Height of new triangle.
     * @param pos The top-left position of the new triangle.
     */
    public Triangle(VectorF pos, float width, float height) {
        super();
        VectorF rightAngleCorner = pos.clone();
        rightAngleCorner.add(0, height);
        VectorF otherCorner = pos.clone();
        otherCorner.add(width, height);
        addVertices(new VectorF[] {pos.clone(), rightAngleCorner, otherCorner});
    }
}
