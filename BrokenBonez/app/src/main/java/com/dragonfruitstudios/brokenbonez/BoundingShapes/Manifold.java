package com.dragonfruitstudios.brokenbonez.BoundingShapes;

import com.dragonfruitstudios.brokenbonez.VectorF;

/**
 * Inspired by Ryan Gaul's impulse engine. The manifold contains information about a collision.
 * Including penetration depth, the shapes involved and the collision normal.
 */

public class Manifold {
    private VectorF normal;
    private float penetration;

    private boolean collided;

    public Manifold(VectorF normal, float penetration, boolean collided) {
        this.normal = normal;
        this.penetration = penetration;
        this.collided = collided;
    }

    public VectorF getNormal() {
        return normal;
    }

    public void setNormal(VectorF normal) {
        this.normal = normal;
    }

    public float getPenetration() {
        return penetration;
    }

    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }

    public boolean isCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }
}
