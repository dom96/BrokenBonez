package com.dragonfruitstudios.brokenbonez.Math.Physics;

import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameLoop;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Intersector;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Manifold;

import java.util.ArrayList;

/**
 * This class simulates physics.
 */
public class Simulator {
    ArrayList<StaticBody> staticBodies;
    ArrayList<DynamicBody> dynamicBodies;
    ArrayList<Constraint> constraints;

    public final static float gravity = 9.81f;
    public final static float gravityScaled = 10*gravity;

    public Simulator() {
        dynamicBodies = new ArrayList<DynamicBody>();
        staticBodies = new ArrayList<StaticBody>();
        constraints = new ArrayList<Constraint>();
    }

    public void update(float lastUpdate) {
        // Use a fixed update factor to make the physics simulation deterministic.
        float updateFactor = 1.0f/GameLoop.targetFPS;

        // Go through each dynamic body and determine if it collides with any static bodies.
        // TODO: This is O(n*m) which isn't terribly efficient. Only simulate what is on screen.
        for (DynamicBody dBody : dynamicBodies) {
            ArrayList<Manifold> collisions = new ArrayList<>();
            for (StaticBody sBody : staticBodies) {
                Manifold collision = sBody.collisionTest(dBody);
                if (collision.hasCollided()) {
                    collisions.add(collision);
                }
            }

            // Update the dynamic body with the collection of collisions.
            dBody.update(updateFactor, collisions);

            // Change the dynamic bodies position, rotation and angular velocity based on
            // its acceleration, velocity etc.
            dBody.updatePos(updateFactor);
        }

        // Solve constraints.
        for (Constraint c : constraints) {
            c.update(updateFactor);
        }
    }

    public void draw(GameView view) {
        for (StaticBody sBody : staticBodies) {
            sBody.draw(view);
        }

        for (DynamicBody dBody : dynamicBodies) {
            dBody.draw(view);
        }
    }

    /**
     * Creates a new dynamic body belonging to this Simulator. The simulator will perform all
     * updates and draws required on this body.
     * @param shape The shape of this body. This will be copied so you can reuse a single shape.
     * @param mass The mass of this body.
     */
    public DynamicBody createDynamicBody(Circle shape, float mass) {
        DynamicBody body = new DynamicBody(shape, mass);
        dynamicBodies.add(body);
        return body;
    }


    /**
     * Creates a new static body belonging to this Simulator. This body has an infinite mass and
     * does not move under the influence of gravity or other bodies. The simulator will perform all
     * updates and draws required on this body.
     * @param shape The shape of this body. This will be copied so you can reuse a single shape.
     */
    public StaticBody createStaticBody(Intersector shape) {
        StaticBody body = new StaticBody(shape);
        staticBodies.add(body);
        return body;
    }

    /**
     * Creates a new constraint between the specified bodies. A constraint will ensure that those
     * bodies never are closer than the specified separation. The simulator will perform all
     * updates required of this constraint.
     * @param bodyA The body which should be constrained to bodyB.
     * @param bodyB The body which should be constrained to bodyA.
     * @param separation The amount of pixels of separation to force between the two bodies.
     */
    public Constraint createConstraint(DynamicBody bodyA, DynamicBody bodyB, float separation) {
        Constraint c = new Constraint(bodyA, bodyB, separation);
        constraints.add(c);
        return c;
    }
}
