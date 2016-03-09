package com.dragonfruitstudios.brokenbonez.Math.Physics;

import android.graphics.Color;

import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Manifold;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Implements a dynamic body. That is, a body which is affected by physics. Currently only
 * circle bodies are supported.
 */
public class DynamicBody extends Body {
    Circle boundingShape;

    VectorF velocity; // Measured in px/s
    VectorF acceleration; // Measured in px/s²

    float rotation; // Measured in radians
    float angularVelocity; // Radians per second
    float angularAcceleration; // Radians per second per second

    boolean wasInAir; // Determines whether the body was in air.
    float torque; // Determines what engine power to apply to the body about its center.

    ArrayList<Manifold> lastManifolds;

    /**
     * The constructor for the DynamicBody is only accessible in the package. To create a new
     * DynamicBody you need to use the `createDynamicBody` method in your Simulator instance.
     */
    DynamicBody(Circle shape, float mass) {
        boundingShape = shape.copy();
        this.mass = mass;
        // Calculate inverse mass here so that it's not recalculated every frame.
        this.inverseMass = 1/mass;
        this.restitution = 0.1f;

        // Initialise fields used for physics calculations with sensible defaults explicitly.
        velocity = new VectorF(0, 0);
        acceleration = new VectorF(0, 0);
        rotation = 0; // Rotation is towards x-axis, so 0 means the body is pointing to the right.
        angularAcceleration = 0;
        angularVelocity = 0;
        wasInAir = true; // Assume that the body was spawned in air.
        torque = 0;

        // Initialise debugging fields.
        lastManifolds = new ArrayList<Manifold>();
    }

    void updatePos(float updateFactor) {
        // Change position based on velocity.
        getPos().multAdd(velocity, updateFactor);

        // Change angular velocity based on angular acceleration.
        angularVelocity += angularAcceleration * updateFactor;
        // Change rotation based on angular velocity.
        rotation += angularVelocity * updateFactor;
    }

    private VectorF calcAirResistance() {
        // Deceleration due to air resistance. It acts in the opposite direction to the
        // velocity.
        return new VectorF(-(Simulator.airResistance * velocity.getX()),
                -(Simulator.airResistance * velocity.getY()));
    }

    void update(float updateFactor, ArrayList<Manifold> manifolds) {
        // Save the collision info so that the normals can be drawn in the next frame.
        // (Just for debugging).
        lastManifolds = manifolds;

        if (manifolds.size() > 0) {
            // The body may be colliding with multiple objects. That's why we get multiple
            // manifolds and have to look at all of them.
            for (Manifold manifold : manifolds) {
                // Correct position
                getPos().multAdd(manifold.getNormal(), -(manifold.getPenetration()-0.1f));

                // The formula used to calculate the impulse is defined here:
                // http://gamedevelopment.tutsplus.com/tutorials/how-to-create-a-custom-2d-physics-engine-the-basics-and-impulse-resolution--gamedev-6331
                // The Impulse Engine (which the article above describes) was a great resource for
                // understanding the way that Physics Engines are implemented, but please keep in
                // mind that there was no code taken from it verbatim.

                // Calculate the impulses that the normals enact on the body.

                // Calculate relative velocity in terms of the normal direction.
                float velRelativeToNormal = velocity.dotProduct(manifold.getNormal());

                // Calculate restitution: ratio of how much energy remains to how much is lost.
                // https://en.wikipedia.org/wiki/Coefficient_of_restitution
                // Affects bounciness. The minimum of the colliding bodies is used.
                float e = Math.min(manifold.getFirstBody().getRestitution(),
                        manifold.getSecondBody().getRestitution());

                // Calculate the impulse scalar.
                float j = -(1 + e) * velRelativeToNormal;
                j /= inverseMass;

                // Apply the impulse.
                VectorF impulse = new VectorF(manifold.getNormal());
                impulse.mult(j);

                velocity.multAdd(impulse, inverseMass);
                // TODO: Apply the impulse to the Body we are colliding with too?

                if (wasInAir) {
                    wasInAir = false;
                    // Velocity = ω * radius
                    // Calculate the magnitude of the new velocity based on the bodies angular
                    // velocity. Use the magnitude to find the new vector velocity based on
                    // the direction of the old velocity.
                    float frictionalVel = Math.abs(angularVelocity) * boundingShape.getRadius() *
                        Simulator.angularVelPreserved;

                    velocity.multAdd(manifold.getNormal().rotated(-90), frictionalVel);
                } else {
                    // Calculate the body's angular velocity based on its linear velocity.
                    // i.e. make the wheels spin!
                    // Using the equation: ω = velocity / radius
                    float newAngularVelocity = velocity.magnitude() / boundingShape.getRadius();
                    // Determine the direction of rotation.
                    if (velocity.getX() < 0) {
                        newAngularVelocity = -newAngularVelocity;
                    }
                    angularVelocity = newAngularVelocity;
                }

                // Set velocity based on torque.
                // Divide updateFactor by 2 so that gravity wins and the wheels don't slide.
                velocity.multAdd(new VectorF(torque, 0), updateFactor/2);
            }
        }
        else {
            wasInAir = true;
            // Resolve forces when body is in air.

            // Acceleration due to gravity.
            acceleration.setY(Simulator.gravityScaled);
        }

        // Calculate the air resistance.
        VectorF airResistance = calcAirResistance();

        // Calculate the resultant acceleration.
        VectorF resultantAccel = new VectorF(acceleration);
        resultantAccel.add(airResistance);

        // Update the bodies' velocity based on acceleration.
        velocity.multAdd(resultantAccel, updateFactor);
    }

    public void reset() {
        velocity.set(0, 0);
        acceleration.set(0, 0);
        rotation = 0;
        angularVelocity = 0;
        angularAcceleration = 0;
        wasInAir = false;
        torque = 0;
    }

    public Circle getBoundingShape() {
        return boundingShape;
    }

    public VectorF getPos() {
        return boundingShape.getPos();
    }

    public void setPos(float x, float y) {
        boundingShape.setCenter(x, y);
    }

    public void setTorque(float torque) {
        this.torque = torque;
    }

    public VectorF getVelocity() {
        return velocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public float getRotation() {
        return rotation;
    }

    public VectorF getSize() {
        return boundingShape.getSize();
    }

    /**
     * Just for debugging.
     */
    void draw(GameView view) {
        // Draw a yellow line to show the body's rotation.
        // A rotation of `0` will show the line horizontally to the right.
        VectorF rotatedFinish = new VectorF(boundingShape.getRadius(), 0);
        rotatedFinish.rotate(rotation);
        view.drawLine(getPos(), getPos().added(rotatedFinish), Color.parseColor("#ffe961"));

        boundingShape.draw(view);

        // Just for testing.
        // Draw a green line to show the normal of each manifold.
        for (Manifold manifold : lastManifolds) {
            VectorF x = new VectorF(boundingShape.getCenter());
            x.multAdd(manifold.getNormal(), boundingShape.getRadius());
            view.drawLine(boundingShape.getCenter(), x,
                    Color.parseColor("#00c80a"));
        }
    }

    @Override
    public String toString() {
        return String.format("Vel: (%.1f, %.1f), Acc: (%.1f, %.1f), Pos: (%.1f, %.1f)",
                velocity.x, velocity.y, acceleration.x, acceleration.y, getPos().x, getPos().y);
    }

}
