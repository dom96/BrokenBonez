package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import com.dragonfruitstudios.brokenbonez.Math.Physics.Body;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * Inspired by Randy Gaul's impulse engine. The manifold contains information about a collision.
 * Including penetration depth, the shapes involved and the collision normal.
 */

public class Manifold {
    private VectorF normal;
    private float penetration;

    private boolean collided;

    private Body bodyA;
    private Body bodyB;

    public Manifold(VectorF normal, float penetration, boolean collided) {
        this.normal = normal;
        this.penetration = penetration;
        this.collided = collided;
    }

    /**
     * Creates a new empty Manifold which specifies that no collision occurred.
     */
    public static Manifold noCollision() {
        return new Manifold(null, -1, false);
    }

    // <editor-fold desc="Getters/Setters">

    /**
     * @return A unit vector which specifies the normal to the collision line. It points towards
     * the line with which a collision took place.
     */
    public VectorF getNormal() {
        return normal;
    }

    public void setNormal(VectorF normal) {
        this.normal = normal;
    }

    /**
     * @return How far the object was in the other object when the collision took place, i.e.
     * the penetration depth.
     */
    public float getPenetration() {
        return penetration;
    }

    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }

    /**
     * @return Whether a collision took place.
     */
    public boolean hasCollided() {
        return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    }


    public void setFirstBody(Body body) {
        this.bodyA = body;
    }

    /**
     * @return The first body involved in the collision.
     */
    public Body getFirstBody() {
        if (bodyA == null) {
            throw new RuntimeException("The first body was not set.");
        }
        return bodyA;
    }

    public void setSecondBody(Body body) {
        this.bodyB = body;
    }

    /**
     * @return The second body involved in the collision.
     */
    public Body getSecondBody() {
        if (bodyB == null) {
            throw new RuntimeException("The second body was not set.");
        }
        return bodyB;
    }

    // </editor-fold>
}
