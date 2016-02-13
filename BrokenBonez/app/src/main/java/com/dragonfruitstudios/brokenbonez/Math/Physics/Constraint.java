package com.dragonfruitstudios.brokenbonez.Math.Physics;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

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
        // Resolve constraint between left wheel and right wheel.
        // Find a vector from the left wheel to the right wheel.
        VectorF leftToRight = bodyB.getPos().subtracted(bodyA.getPos());
        // Calculate the distance between the two wheels.
        float distance = leftToRight.magnitude();
        //Log.d("Constraint", "Distance: " + distance);
        leftToRight.normalise();

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
