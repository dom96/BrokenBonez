package com.dragonfruitstudios.brokenbonez.Math.Physics;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * This class implements a constraint between two arbitrary bodies which ensures that they
 * are always within a certain distance of each other.
 *
 * This is used to keep the wheels of the bike locked together.
 */
public class Constraint {
    DynamicBody bodyA;
    DynamicBody bodyB;

    float separation;

    Constraint(DynamicBody bodyA, DynamicBody bodyB, float separation) {
        this.bodyA = bodyA;
        this.bodyB = bodyB;
        this.separation = separation;
    }

    void update(float updateFactor) {
        // The equations used in this code have been taken from the following article:
        // http://www.wildbunny.co.uk/blog/2011/04/06/physics-engines-for-dummies/
        // (Under the Constraints section).
        // TODO: There is currently a small bug here which prevents the bike bouncing off walls
        // TODO: realistically.

        // Resolve constraint between left wheel and right wheel.
        // Find a vector from the left wheel to the right wheel.
        VectorF leftToRight = bodyB.getPos().subtracted(bodyA.getPos());
        // Calculate the distance between the two wheels.
        float distance = leftToRight.magnitude();

        // Ensure that the magnitude is not 0 which leads to an exception.
        if (distance != 0) {
            leftToRight.normalise();
        }
        else {
            leftToRight.set(1, 0);
        }

        // Calculate the velocity relative to the vector between the wheels.
        VectorF leftToRightVel = bodyB.getVelocity().subtracted(bodyA.getVelocity());
        float relativeVelocity = leftToRightVel.dotProduct(leftToRight);
        float relativeDistance = distance - separation;

        // Calculate the impulse to remove.
        float distRemove = relativeVelocity+relativeDistance;
        float impulseRemove = distRemove / (bodyA.getInverseMass() + bodyA.getInverseMass());

        // Generate impulse vectors
        VectorF impulse = leftToRight.copy();
        impulse.mult(impulseRemove);
        VectorF bodyAImpulse = impulse.copy();
        bodyAImpulse.mult(bodyA.getInverseMass());
        VectorF bodyBImpulse = impulse.copy();
        bodyBImpulse.mult(bodyB.getInverseMass());

        // Apply the impulse to the velocity of each wheel appropriately.
        bodyA.getVelocity().add(bodyAImpulse);
        bodyB.getVelocity().sub(bodyBImpulse);
    }

}
