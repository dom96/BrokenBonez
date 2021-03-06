package com.dragonfruitstudios.brokenbonez.Math.Physics;

import android.util.Log;

import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameLoop;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Intersector;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Manifold;
import com.dragonfruitstudios.brokenbonez.Math.MathUtils;

import java.util.ArrayList;

/**
 * This class simulates physics.
 */
public class Simulator {
    ArrayList<StaticBody> staticBodies;
    ArrayList<DynamicBody> dynamicBodies;
    ArrayList<Constraint> constraints;

    // Used to calculate update factor.
    private static float updateRate = 60f;

    public final static float gravity = 9.81f;
    public final static float gravityScaled = 10*gravity;
    public final static float airResistance = 0.1f; // Percentage of velocity lost due to air resistance.
    public final static float angularVelPreserved = 0.01f; // Percentage of angular velocity preserved when bike hits ground.

    public Simulator() {
        dynamicBodies = new ArrayList<DynamicBody>();
        staticBodies = new ArrayList<StaticBody>();
        constraints = new ArrayList<Constraint>();
    }

    public void update(float lastUpdate) {
        // Use a fixed update factor to make the physics simulation deterministic.
        float updateFactor = Simulator.calcUpdateFactor(lastUpdate);

        // TODO: Use Manifold.Collection to hold collection of Manifolds instead of custom
        // ArrayList?

        // Go through each dynamic body and determine if it collides with any static bodies.
        // TODO: This is O(n*m) which isn't terribly efficient. Only simulate what is on screen.
        for (DynamicBody dBody : dynamicBodies) {
            ArrayList<Manifold> collisions = new ArrayList<>();
            for (StaticBody sBody : staticBodies) {
                // Check if the two bodies are sufficiently close together for a collision to occur.
                if (!sBody.mightCollide(dBody)) {
                    // Don't test for collision if sufficiently far enough.
                    continue;
                }
                Manifold.Collection collision = sBody.collisionTest(dBody);
                if (collision.hasCollisions()) {
                    for (Manifold m : collision) {
                        collisions.add(m);
                    }
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
        view.enableCamera();
        for (StaticBody sBody : staticBodies) {
            sBody.draw(view);
        }

        for (DynamicBody dBody : dynamicBodies) {
            dBody.draw(view);
        }
        view.disableCamera();
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

    /**
     * Adds the specified shape to the simulator as a StaticBody as-is, i.e. the shape is not
     * copied. This allows you to make changes to the shape and they will be reflected in the
     * Physics engine.
     */
    public void addStaticShape(Intersector shape) {
        StaticBody body = new StaticBody();
        body.setBoundingShape(shape);
        staticBodies.add(body);
    }

    /**
     * Adds the specified constraint to this simulator as long as the simulator doesn't already
     * contain it.
     */
    public void addConstraint(Constraint constraint) {
        if (!constraints.contains(constraint)) {
            constraints.add(constraint);
        }
    }

    /**
     * Removes the specified constraint from this simulator.
     */
    public void removeConstraint(Constraint constraint) {
        constraints.remove(constraint);
    }

    /**
     * Determines whether the specified shape collides with any StaticBodies in this Simulator.
     */
    public boolean collidesWith(Intersector shape) {
        for (StaticBody sb : staticBodies) {
            // TODO: Optimise this.
            if (sb.getBoundingShape().collisionTest(shape).hasCollisions()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the update factor based on the update rate stored in this class.
     *
     * I decided against reusing GameLoop.targetFPS, as this allows for the update rate to be
     * independent of the frame rate.
     * @param lastUpdate This isn't used, but passed in case it becomes useful later on.
     */
    public static float calcUpdateFactor(float lastUpdate) {
        return 1f/updateRate;
    }

    /**
     * Sets the update rate which determines how fast the simulation advances.
     *
     * If FPS is constant then a high value will cause the simulation to become slower, and vice
     * versa for a low value.
     * @param updateRate The update rate, default is 60.
     */
    public static void setUpdateRate(float updateRate) {
        Simulator.updateRate = updateRate;
    }
}
