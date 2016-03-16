package com.dragonfruitstudios.brokenbonez.Math.Physics;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public abstract class Body {
    // Determines how close two Polygons can get before `mightCollide` reports `True`.
    private final float collisionProximityFactor = 1.5f;

    float mass; // Mass of body
    float inverseMass; // Inverse mass (cached for performance).
    float restitution; // Affects bounciness of body.

    // Caching for `mightCollide`.
    float proximityX = -1;
    float proximityY = -1;

    float getRestitution() {
        return restitution;
    }

    float getInverseMass() {
        return inverseMass;
    }

    /**
     * Returns the position of the center of the body.
     */
    abstract VectorF getPos();

    abstract VectorF getSize();

    /**
     * Determines whether the specified bodies have a chance of colliding.
     */
    public boolean mightCollide(Body b) {
        // Cache these values for ultimate performance.
        if (proximityX == -1 || proximityY == -1) {
            proximityX = Math.max(this.getSize().x, b.getSize().x) * collisionProximityFactor;
            proximityY = Math.max(this.getSize().y, b.getSize().y) * collisionProximityFactor;
        }
        return Math.abs(this.getPos().x - b.getPos().x) < proximityX &&
                Math.abs(this.getPos().y - b.getPos().y) < proximityY;
    }
}
